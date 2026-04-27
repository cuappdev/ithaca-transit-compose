package com.cornellappdev.transit.util

import android.util.Log
import com.cornellappdev.transit.models.DirectionType
import com.cornellappdev.transit.models.Route
import com.cornellappdev.transit.models.RouteOptions
import java.time.Duration
import java.time.Instant

/** Log tag for aggregated route-processing diagnostics emitted by this file. */
private const val ROUTE_OPTIONS_DISPLAY_TAG = "RouteOptionsDisplay"

/**
 * Normalized reasons a route can be treated as invalid/malformed during display processing.
 *
 * Keys are stable for log aggregation and quick telemetry filtering.
 */
private sealed class RouteProcessingFailure(val key: String) {
    object InvalidDirectionStartTime : RouteProcessingFailure("invalid_direction_start_time")
    object InvalidDirectionEndTime : RouteProcessingFailure("invalid_direction_end_time")
    object InvalidRouteDepartureTime : RouteProcessingFailure("invalid_route_departure_time")
    object InvalidRouteArrivalTime : RouteProcessingFailure("invalid_route_arrival_time")
    object InvalidDirectionDuration : RouteProcessingFailure("invalid_direction_duration")
    object InvalidTripDuration : RouteProcessingFailure("invalid_trip_duration")
    object MissingDirectionsForArrivalFallback : RouteProcessingFailure("missing_directions_for_arrival_fallback")
    object UnresolvedArrivalTime : RouteProcessingFailure("unresolved_arrival_time")
}

/**
 * Aggregates parsing/validation failures during a single processing pass and logs once at the end.
 */
private class RouteProcessingDiagnostics(private val mode: String) {
    private val failureCounts = linkedMapOf<String, Int>()

    fun record(failure: RouteProcessingFailure) {
        failureCounts[failure.key] = (failureCounts[failure.key] ?: 0) + 1
    }

    fun logIfAny() {
        if (failureCounts.isEmpty()) return
        val details = failureCounts.entries.joinToString { (key, count) -> "$key=$count" }
        Log.w(ROUTE_OPTIONS_DISPLAY_TAG, "mode=$mode dropped routes due to malformed timing data: $details")
    }
}

/** Parse an ISO-8601 instant string and report parse failures while preserving null-on-failure behavior. */
private fun String.toInstantOrNull(
    diagnostics: RouteProcessingDiagnostics? = null,
    failure: RouteProcessingFailure,
): Instant? {
    return runCatching { Instant.parse(this) }
        .getOrElse {
            diagnostics?.record(failure)
            null
        }
}

/** Logical source section used to flatten and later reconstruct [RouteOptions]. */
private enum class RouteSection {
    FROM_STOP,
    BOARDING_SOON,
    WALKING,
}

/** Route paired with its originating display section while processing pipelines run. */
private data class SectionedRoute(
    val section: RouteSection,
    val route: Route,
)

/** Time-and-distance effort tuple used for walking-vs-transit preference comparisons. */
private data class RouteEffort(
    val duration: Duration,
    val distance: Double,
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
private fun Route.firstBoardingDepartureInstantOrNull(
    diagnostics: RouteProcessingDiagnostics? = null,
): Instant? {
    val firstDepartDirection = directions.firstOrNull { it.type == DirectionType.DEPART } ?: return null
    val scheduledStart = firstDepartDirection.startTime.toInstantOrNull(
        diagnostics = diagnostics,
        failure = RouteProcessingFailure.InvalidDirectionStartTime,
    ) ?: return null
    val delaySeconds = (firstDepartDirection.delay ?: 0).coerceAtLeast(0)
    return scheduledStart.plusSeconds(delaySeconds.toLong())
}

/**
 * Total duration of directions before the first DEPART segment.
 * Returns [Duration.ZERO] when route starts with transit or has no DEPART segment.
 */
private fun Route.walkingDurationBeforeFirstBoardingOrNull(
    diagnostics: RouteProcessingDiagnostics? = null,
): Duration? {
    val firstDepartIndex = directions.indexOfFirst { it.type == DirectionType.DEPART }
    if (firstDepartIndex <= 0) return Duration.ZERO

    var total = Duration.ZERO
    for (direction in directions.take(firstDepartIndex)) {
        val start = direction.startTime.toInstantOrNull(
            diagnostics = diagnostics,
            failure = RouteProcessingFailure.InvalidDirectionStartTime,
        ) ?: return null
        val end = direction.endTime.toInstantOrNull(
            diagnostics = diagnostics,
            failure = RouteProcessingFailure.InvalidDirectionEndTime,
        ) ?: return null
        val segmentDuration = Duration.between(start, end)
        if (segmentDuration.isNegative) {
            diagnostics?.record(RouteProcessingFailure.InvalidDirectionDuration)
            return null
        }
        total = total.plus(segmentDuration)
    }
    return total
}

/**
 * Leave-mode legality check:
 * - walking-only routes are always legal to start at cutoff,
 * - transit routes are legal only if user can complete initial walking before first boarding.
 */
private fun Route.isLegalForLeaveCutoff(
    cutoff: Instant,
    diagnostics: RouteProcessingDiagnostics? = null,
): Boolean {
    val firstBoardingDeparture = firstBoardingDepartureInstantOrNull(diagnostics)

    // Only walking-only routes can treat missing boarding departure as legal.
    if (firstBoardingDeparture == null) {
        return !isTransitRoute()
    }

    val initialWalkingDuration = walkingDurationBeforeFirstBoardingOrNull(diagnostics) ?: return false
    val readyToBoardAt = cutoff.plus(initialWalkingDuration)

    // Strictly before departure to avoid showing routes where the bus leaves as walking completes.
    return readyToBoardAt.isBefore(firstBoardingDeparture)
}

/** Departure instant used to rank leave-mode routes. */
private fun Route.leaveRankingDepartureInstantOrNull(
    diagnostics: RouteProcessingDiagnostics? = null,
): Instant? {
    return firstBoardingDepartureInstantOrNull(diagnostics) ?: departureTime.toInstantOrNull(
        diagnostics = diagnostics,
        failure = RouteProcessingFailure.InvalidRouteDepartureTime,
    )
}

/** Effective leave instant from user perspective; falls back to cutoff for walking-only routes. */
private fun Route.effectiveLeaveInstantOrNull(
    cutoff: Instant,
    diagnostics: RouteProcessingDiagnostics? = null,
): Instant? {
    return firstBoardingDepartureInstantOrNull(diagnostics) ?: cutoff
}

/**
 * Instant used for leave-mode horizon bounds.
 * For transit, prefer route start time; for walking-only routes, use the user cutoff.
 */
private fun Route.horizonReferenceInstantOrNull(
    cutoff: Instant,
    diagnostics: RouteProcessingDiagnostics? = null,
): Instant? {
    // Use route start time for horizon checks so initial walking/waiting doesn't over-prune options.
    return if (isTransitRoute()) {
        departureTime.toInstantOrNull(
            diagnostics = diagnostics,
            failure = RouteProcessingFailure.InvalidRouteDepartureTime,
        ) ?: effectiveLeaveInstantOrNull(cutoff, diagnostics)
    } else {
        cutoff
    }
}

/** Compute effort tuple used for walking-vs-transit preference decisions. */
private fun Route.effortOrNull(
    diagnostics: RouteProcessingDiagnostics? = null,
): RouteEffort? {
    val departureInstant = departureTime.toInstantOrNull(
        diagnostics = diagnostics,
        failure = RouteProcessingFailure.InvalidRouteDepartureTime,
    ) ?: return null
    val arrivalInstant = arrivalTime.toInstantOrNull(
        diagnostics = diagnostics,
        failure = RouteProcessingFailure.InvalidRouteArrivalTime,
    ) ?: return null
    val tripDuration = Duration.between(departureInstant, arrivalInstant)
    if (tripDuration.isNegative) {
        diagnostics?.record(RouteProcessingFailure.InvalidTripDuration)
        return null
    }
    return RouteEffort(duration = tripDuration, distance = travelDistance)
}

/** True when route contains at least one DEPART segment. */
private fun Route.isTransitRoute(): Boolean = directions.any { it.type == DirectionType.DEPART }

/**
 * Best-effort arrival instant for Arrive By checks.
 * Uses top-level route arrival first, then falls back to last direction endTime (+delay).
 */
private fun Route.effectiveArrivalInstantOrNull(
    diagnostics: RouteProcessingDiagnostics? = null,
): Instant? {
    val routeArrival = arrivalTime.toInstantOrNull(
        diagnostics = diagnostics,
        failure = RouteProcessingFailure.InvalidRouteArrivalTime,
    )
    if (routeArrival != null) return routeArrival

    val lastDirection = directions.lastOrNull()
    if (lastDirection == null) {
        diagnostics?.record(RouteProcessingFailure.MissingDirectionsForArrivalFallback)
        diagnostics?.record(RouteProcessingFailure.UnresolvedArrivalTime)
        return null
    }

    val endInstant = lastDirection.endTime.toInstantOrNull(
        diagnostics = diagnostics,
        failure = RouteProcessingFailure.InvalidDirectionEndTime,
    )
    if (endInstant == null) {
        diagnostics?.record(RouteProcessingFailure.UnresolvedArrivalTime)
        return null
    }

    val delaySeconds = (lastDirection.delay ?: 0).coerceAtLeast(0)
    return endInstant.plusSeconds(delaySeconds.toLong())
}

/** True when route arrives on or before provided cutoff (strict, no grace period). */
private fun Route.arrivesBy(
    cutoff: Instant,
    diagnostics: RouteProcessingDiagnostics? = null,
): Boolean {
    val arrivalInstant = effectiveArrivalInstantOrNull(diagnostics) ?: return false
    return !arrivalInstant.isAfter(cutoff)
}

/** Comparator for Arrive By ordering: latest departure first, then shorter distance. */
private fun compareArriveByRoutes(left: Route, right: Route): Int {
    val leftDeparture = left.departureTime.toInstantOrNull(failure = RouteProcessingFailure.InvalidRouteDepartureTime)
    val rightDeparture = right.departureTime.toInstantOrNull(failure = RouteProcessingFailure.InvalidRouteDepartureTime)

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
    diagnostics: RouteProcessingDiagnostics? = null,
): Boolean {
    val routeDeparture = horizonReferenceInstantOrNull(cutoff, diagnostics) ?: return false
    return !routeDeparture.isBefore(earliestAllowed) && !routeDeparture.isAfter(horizonEnd)
}

/**
 * Transit preference predicate when walking alternatives exist.
 * Transit is preferred if it is significantly faster, or not slower while being shorter distance.
 */
private fun Route.isPreferredTransitAgainstWalking(
    walkingEffort: RouteEffort,
    tieBuffer: Duration,
    diagnostics: RouteProcessingDiagnostics? = null,
): Boolean {
    val transitEffort = effortOrNull(diagnostics) ?: return false

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
 * Keeps routes that arrive by cutoff, then sorts by latest departure first.
 */
private fun List<Route>?.filterAndSortRoutesForArriveBy(
    cutoff: Instant,
    diagnostics: RouteProcessingDiagnostics? = null,
): List<Route>? {
    if (this == null) return null

    return this
        .filter { route -> route.arrivesBy(cutoff, diagnostics) }
        .sortedWith(::compareArriveByRoutes)
}

/**
 * Apply Arrive By policy per section.
 * Routes are filtered by arrival cutoff and sorted within each section.
 */
fun RouteOptions.filterAndSortForArriveBy(cutoff: Instant): RouteOptions {
    val diagnostics = RouteProcessingDiagnostics(mode = "arrive_by")

    val processed = RouteOptions(
        fromStop = fromStop.filterAndSortRoutesForArriveBy(cutoff, diagnostics),
        boardingSoon = boardingSoon.filterAndSortRoutesForArriveBy(cutoff, diagnostics),
        walking = walking.filterAndSortRoutesForArriveBy(cutoff, diagnostics)
    )
    diagnostics.logIfAny()

    return processed
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
    val diagnostics = RouteProcessingDiagnostics(mode = "leave_cutoff")
    val horizonEnd = cutoff.plus(Duration.ofMinutes(horizonMinutes))
    val earliestAllowed = cutoff.minus(Duration.ofMinutes(LEAVE_CUTOFF_GRACE_MINUTES))

    // Cache per-route transit status so we do not rescan directions in downstream filters.
    data class LeaveCandidate(
        val sectionedRoute: SectionedRoute,
        val isTransit: Boolean,
    )

    val eligibleCandidates = flattenBySection()
        .filter { (_, route) -> route.isLegalForLeaveCutoff(cutoff, diagnostics) }
        .filter { (_, route) -> route.isWithinLeaveHorizon(cutoff, earliestAllowed, horizonEnd, diagnostics) }
        .map { sectionedRoute ->
            LeaveCandidate(
                sectionedRoute = sectionedRoute,
                isTransit = sectionedRoute.route.isTransitRoute(),
            )
        }

    val bestWalkingEffort = eligibleCandidates
        .filter { !it.isTransit }
        .mapNotNull { it.sectionedRoute.route.effortOrNull(diagnostics) }
        .minWithOrNull(compareBy<RouteEffort> { it.duration }.thenBy { it.distance })

    val tieBuffer = Duration.ofMinutes(walkingTransitTieMinutes)

    val preferred = eligibleCandidates
        .filter { candidate ->
            if (!candidate.isTransit) return@filter true

            val walkingEffort = bestWalkingEffort ?: return@filter true
            candidate.sectionedRoute.route.isPreferredTransitAgainstWalking(walkingEffort, tieBuffer, diagnostics)
        }

    val fallbackTransit = if (preferred.any { it.isTransit }) {
        null
    } else {
        eligibleCandidates
            .asSequence()
            .filter { it.isTransit }
            .minByOrNull { it.sectionedRoute.route.effectiveLeaveInstantOrNull(cutoff, diagnostics) ?: Instant.MAX }
    }

    val ranked = (if (fallbackTransit != null) preferred + fallbackTransit else preferred)
        .sortedWith { left, right ->
            compareByEffectiveLeaveTime(left.sectionedRoute, right.sectionedRoute, cutoff)
        }

    val finalRoutes = if (maxRoutes != null) {
        val initialSlice = ranked.take(maxRoutes)
        if (fallbackTransit != null && maxRoutes > 0 && fallbackTransit !in initialSlice) {
            (initialSlice.dropLast(1) + fallbackTransit)
                .sortedWith { left, right ->
                    compareByEffectiveLeaveTime(left.sectionedRoute, right.sectionedRoute, cutoff)
                }
        } else {
            initialSlice
        }
    } else {
        ranked
    }

    diagnostics.logIfAny()

    return finalRoutes.map { it.sectionedRoute }.toRouteOptions()
}
