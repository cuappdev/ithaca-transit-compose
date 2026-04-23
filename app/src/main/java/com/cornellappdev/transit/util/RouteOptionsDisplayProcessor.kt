package com.cornellappdev.transit.util

import com.cornellappdev.transit.models.DirectionType
import com.cornellappdev.transit.models.Route
import com.cornellappdev.transit.models.RouteOptions
import java.time.Duration
import java.time.Instant

/** Parse an ISO-8601 instant string and return null when parsing fails. */
private fun String.toInstantOrNull(): Instant? = runCatching { Instant.parse(this) }.getOrNull()

private enum class RouteSection {
    FROM_STOP,
    BOARDING_SOON,
    WALKING,
}

private data class SectionedRoute(
    val section: RouteSection,
    val route: Route,
)

/** Flatten sectioned route options into a single list while preserving section labels. */
private fun RouteOptions.flattenBySection(): List<SectionedRoute> {
    val routes = mutableListOf<SectionedRoute>()
    fromStop?.forEach { routes += SectionedRoute(RouteSection.FROM_STOP, it) }
    boardingSoon?.forEach { routes += SectionedRoute(RouteSection.BOARDING_SOON, it) }
    walking?.forEach { routes += SectionedRoute(RouteSection.WALKING, it) }
    return routes
}

/** Rebuild sectioned [RouteOptions] from a flattened list of routes tagged with section metadata. */
private fun List<SectionedRoute>.toRouteOptions(): RouteOptions {
    val fromStopRoutes = filter { it.section == RouteSection.FROM_STOP }.map { it.route }
    val boardingSoonRoutes = filter { it.section == RouteSection.BOARDING_SOON }.map { it.route }
    val walkingRoutes = filter { it.section == RouteSection.WALKING }.map { it.route }

    return RouteOptions(
        fromStop = fromStopRoutes.takeIf { it.isNotEmpty() },
        boardingSoon = boardingSoonRoutes.takeIf { it.isNotEmpty() },
        walking = walkingRoutes.takeIf { it.isNotEmpty() },
    )
}

/**
 * Effective first bus departure for a route, including positive delay on the first DEPART segment.
 * Returns null for walking-only routes or malformed timestamps.
 */
private fun Route.firstBoardingDepartureInstantOrNull(): Instant? {
    val firstDepartDirection = directions.firstOrNull { it.type == DirectionType.DEPART } ?: return null
    val scheduledStart = firstDepartDirection.startTime.toInstantOrNull() ?: return null
    val delaySeconds = (firstDepartDirection.delay ?: 0).coerceAtLeast(0)
    return scheduledStart.plusSeconds(delaySeconds.toLong())
}

/**
 * Total duration of directions before the first DEPART segment.
 * Returns [Duration.ZERO] when route starts with transit or has no DEPART segment.
 */
private fun Route.walkingDurationBeforeFirstBoardingOrNull(): Duration? {
    val firstDepartIndex = directions.indexOfFirst { it.type == DirectionType.DEPART }
    if (firstDepartIndex <= 0) return Duration.ZERO

    var total = Duration.ZERO
    for (direction in directions.take(firstDepartIndex)) {
        val start = direction.startTime.toInstantOrNull() ?: return null
        val end = direction.endTime.toInstantOrNull() ?: return null
        val segmentDuration = Duration.between(start, end)
        if (segmentDuration.isNegative) return null
        total = total.plus(segmentDuration)
    }
    return total
}

/**
 * Leave-mode legality check:
 * - walking-only routes are always legal to start at cutoff,
 * - transit routes are legal only if user can complete initial walking before first boarding.
 */
private fun Route.isLegalForLeaveCutoff(cutoff: Instant): Boolean {
    val firstBoardingDeparture = firstBoardingDepartureInstantOrNull()

    // Walking-only routes can always start at the chosen leave cutoff.
    if (firstBoardingDeparture == null) {
        return true
    }

    val initialWalkingDuration = walkingDurationBeforeFirstBoardingOrNull() ?: return false
    val readyToBoardAt = cutoff.plus(initialWalkingDuration)

    // Strictly before departure to avoid showing routes where the bus leaves as walking completes.
    return readyToBoardAt.isBefore(firstBoardingDeparture)
}

/** Departure instant used to rank leave-mode routes. */
private fun Route.leaveRankingDepartureInstantOrNull(): Instant? {
    return firstBoardingDepartureInstantOrNull() ?: departureTime.toInstantOrNull()
}

/** Effective leave instant from user perspective; falls back to cutoff for walking-only routes. */
private fun Route.effectiveLeaveInstantOrNull(cutoff: Instant): Instant? {
    return firstBoardingDepartureInstantOrNull() ?: cutoff
}

/**
 * Instant used for leave-mode horizon bounds.
 * For transit, prefer route start time; for walking-only routes, use the user cutoff.
 */
private fun Route.horizonReferenceInstantOrNull(cutoff: Instant): Instant? {
    // Use route start time for horizon checks so initial walking/waiting doesn't over-prune options.
    return if (isTransitRoute()) {
        departureTime.toInstantOrNull() ?: effectiveLeaveInstantOrNull(cutoff)
    } else {
        cutoff
    }
}

private data class RouteEffort(
    val duration: Duration,
    val distance: Double,
)

/** Compute effort tuple used for walking-vs-transit preference decisions. */
private fun Route.effortOrNull(): RouteEffort? {
    val departureInstant = departureTime.toInstantOrNull() ?: return null
    val arrivalInstant = arrivalTime.toInstantOrNull() ?: return null
    val tripDuration = Duration.between(departureInstant, arrivalInstant)
    if (tripDuration.isNegative) return null
    return RouteEffort(duration = tripDuration, distance = travelDistance)
}

/** True when route contains at least one DEPART segment. */
private fun Route.isTransitRoute(): Boolean = directions.any { it.type == DirectionType.DEPART }

/**
 * Best-effort arrival instant for Arrive By checks.
 * Uses top-level route arrival first, then falls back to last direction endTime (+delay).
 */
private fun Route.effectiveArrivalInstantOrNull(): Instant? {
    val routeArrival = arrivalTime.toInstantOrNull()
    if (routeArrival != null) return routeArrival

    val lastDirection = directions.lastOrNull() ?: return null
    val endInstant = lastDirection.endTime.toInstantOrNull() ?: return null
    val delaySeconds = (lastDirection.delay ?: 0).coerceAtLeast(0)
    return endInstant.plusSeconds(delaySeconds.toLong())
}

/** True when route arrives on or before provided cutoff (including grace already applied by caller). */
private fun Route.arrivesBy(cutoffWithGrace: Instant): Boolean {
    val arrivalInstant = effectiveArrivalInstantOrNull() ?: return false
    return !arrivalInstant.isAfter(cutoffWithGrace)
}

/** Comparator for Arrive By ordering: latest departure first, then shorter distance. */
private fun compareArriveByRoutes(left: Route, right: Route): Int {
    val leftDeparture = left.departureTime.toInstantOrNull()
    val rightDeparture = right.departureTime.toInstantOrNull()

    return when {
        leftDeparture == null && rightDeparture == null -> 0
        leftDeparture == null -> 1
        rightDeparture == null -> -1
        else -> {
            val departureCompare = rightDeparture.compareTo(leftDeparture)
            if (departureCompare != 0) departureCompare else left.travelDistance.compareTo(right.travelDistance)
        }
    }
}

/** Check whether a route's horizon reference departure is inside the allowed leave window. */
private fun Route.isWithinLeaveHorizon(
    cutoff: Instant,
    earliestAllowed: Instant,
    horizonEnd: Instant,
): Boolean {
    val routeDeparture = horizonReferenceInstantOrNull(cutoff) ?: return false
    return !routeDeparture.isBefore(earliestAllowed) && !routeDeparture.isAfter(horizonEnd)
}

/**
 * Transit preference predicate when walking alternatives exist.
 * Transit is preferred if it is significantly faster, or not slower while being shorter distance.
 */
private fun Route.isPreferredTransitAgainstWalking(
    walkingEffort: RouteEffort,
    tieBuffer: Duration,
): Boolean {
    val transitEffort = effortOrNull() ?: return false

    val transitSignificantlyFaster =
        transitEffort.duration.plus(tieBuffer).compareTo(walkingEffort.duration) < 0
    val transitNotSlowerAndShorter =
        transitEffort.duration.compareTo(walkingEffort.duration.plus(tieBuffer)) <= 0 &&
            transitEffort.distance < walkingEffort.distance

    // Prefer transit only when it offers better effort (time and/or distance) than walking.
    return transitSignificantlyFaster || transitNotSlowerAndShorter
}

/** Comparator for leave-mode ordering: earliest effective leave time first. */
private fun compareByEffectiveLeaveTime(
    left: SectionedRoute,
    right: SectionedRoute,
    cutoff: Instant,
): Int {
    val leftDeparture = left.route.effectiveLeaveInstantOrNull(cutoff)
    val rightDeparture = right.route.effectiveLeaveInstantOrNull(cutoff)
    return when {
        leftDeparture == null && rightDeparture == null -> 0
        leftDeparture == null -> 1
        rightDeparture == null -> -1
        else -> leftDeparture.compareTo(rightDeparture)
    }
}

/**
 * Section-level Arrive By filtering and ordering.
 * Keeps routes that arrive by cutoff (with grace), then sorts by latest departure first.
 */
private fun List<Route>?.filterAndSortRoutesForArriveBy(cutoff: Instant): List<Route>? {
    if (this == null) return null

    val cutoffWithGrace = cutoff.plus(Duration.ofMinutes(ARRIVE_BY_CUTOFF_GRACE_MINUTES))

    return this
        .filter { route -> route.arrivesBy(cutoffWithGrace) }
        .sortedWith(::compareArriveByRoutes)
}

/**
 * Apply Arrive By policy per section.
 * Routes are filtered by arrival cutoff and sorted within each section.
 */
fun RouteOptions.filterAndSortForArriveBy(cutoff: Instant): RouteOptions {
    return RouteOptions(
        fromStop = fromStop.filterAndSortRoutesForArriveBy(cutoff),
        boardingSoon = boardingSoon.filterAndSortRoutesForArriveBy(cutoff),
        walking = walking.filterAndSortRoutesForArriveBy(cutoff)
    )
}

/**
 * Apply leave-mode display policy.
 *
 * Pipeline:
 * 1) flatten sections,
 * 2) filter to legal routes,
 * 3) filter to configured horizon window,
 * 4) prefer transit only when effort beats walking alternatives,
 * 5) ensure at least one transit fallback when legal transit exists,
 * 6) rank by effective leave instant,
 * 7) optionally cap route count and rehydrate sections.
 */
fun RouteOptions.filterAndSortForLeaveCutoff(
    cutoff: Instant,
    maxRoutes: Int? = null,
    horizonMinutes: Long = LEAVE_CUTOFF_HORIZON_MINUTES,
    walkingTransitTieMinutes: Long = WALKING_TRANSIT_TIE_MINUTES,
): RouteOptions {
    val horizonEnd = cutoff.plus(Duration.ofMinutes(horizonMinutes))
    val earliestAllowed = cutoff.minus(Duration.ofMinutes(LEAVE_CUTOFF_GRACE_MINUTES))

    val eligibleRoutes = flattenBySection()
        .filter { (_, route) -> route.isLegalForLeaveCutoff(cutoff) }
        .filter { (_, route) -> route.isWithinLeaveHorizon(cutoff, earliestAllowed, horizonEnd) }

    val bestWalkingEffort = eligibleRoutes
        .map { it.route }
        .filter { !it.isTransitRoute() }
        .mapNotNull { it.effortOrNull() }
        .minWithOrNull(compareBy<RouteEffort> { it.duration }.thenBy { it.distance })

    val tieBuffer = Duration.ofMinutes(walkingTransitTieMinutes)

    val preferred = eligibleRoutes
        .filter { (_, route) ->
            if (!route.isTransitRoute()) return@filter true

            val walkingEffort = bestWalkingEffort ?: return@filter true
            route.isPreferredTransitAgainstWalking(walkingEffort, tieBuffer)
        }

    val fallbackTransit = if (preferred.any { it.route.isTransitRoute() }) {
        null
    } else {
        eligibleRoutes
            .asSequence()
            .filter { it.route.isTransitRoute() }
            .minByOrNull { it.route.effectiveLeaveInstantOrNull(cutoff) ?: Instant.MAX }
    }

    val ranked = (if (fallbackTransit != null) preferred + fallbackTransit else preferred)
        .sortedWith { left, right -> compareByEffectiveLeaveTime(left, right, cutoff) }

    return (if (maxRoutes != null) ranked.take(maxRoutes) else ranked).toRouteOptions()
}






