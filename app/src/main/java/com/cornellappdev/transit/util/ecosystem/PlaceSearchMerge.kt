package com.cornellappdev.transit.util.ecosystem

import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.ecosystem.Eatery
import com.cornellappdev.transit.models.ecosystem.Library
import com.cornellappdev.transit.models.ecosystem.Printer
import com.cornellappdev.transit.models.ecosystem.UpliftGym
import com.cornellappdev.transit.networking.ApiResponse

private data class RankedPlace(
    val place: Place,
    val score: Int,
)

private const val NO_MATCH_SCORE = Int.MAX_VALUE
private const val BACKEND_FALLBACK_SCORE = 4

fun buildEcosystemSearchPlaces(
    printers: ApiResponse<List<Printer>>,
    libraries: ApiResponse<List<Library>>,
    eateries: ApiResponse<List<Eatery>>,
    gyms: ApiResponse<List<UpliftGym>>,
): List<Place> {
    val printerPlaces = (printers as? ApiResponse.Success)?.data.orEmpty().map { it.toPlace() }
    val libraryPlaces = (libraries as? ApiResponse.Success)?.data.orEmpty().map { it.toPlace() }
    val eateryPlaces = (eateries as? ApiResponse.Success)?.data.orEmpty().map { it.toPlace() }
    val gymPlaces = (gyms as? ApiResponse.Success)?.data.orEmpty().map { it.toPlace() }

    return (printerPlaces + libraryPlaces + eateryPlaces + gymPlaces)
        .distinctBy { placeSearchStableId(it) }
}

fun mergeAndRankSearchResults(
    query: String,
    routeSearchResults: ApiResponse<List<Place>>,
    ecosystemPlaces: List<Place>,
): ApiResponse<List<Place>> {
    val normalizedQuery = query.trim().lowercase()
    if (normalizedQuery.isEmpty()) {
        return ApiResponse.Success(emptyList())
    }

    val routePlaces = (routeSearchResults as? ApiResponse.Success)?.data.orEmpty()
    val backendRanked = routePlaces.map { place ->
        val score = relevanceScore(place, normalizedQuery)
        RankedPlace(
            place = place,
            score = if (score == NO_MATCH_SCORE) BACKEND_FALLBACK_SCORE else score
        )
    }

    val ecosystemRanked = ecosystemPlaces
        .map { place -> RankedPlace(place = place, score = relevanceScore(place, normalizedQuery)) }
        .filter { it.score != NO_MATCH_SCORE }

    val dedupedRanked = mutableMapOf<String, RankedPlace>()
    (backendRanked + ecosystemRanked).forEach { rankedPlace ->
        val key = placeSearchStableId(rankedPlace.place)
        val current = dedupedRanked[key]
        if (current == null || rankedPlace.score < current.score) {
            dedupedRanked[key] = rankedPlace
        }
    }

    val merged = dedupedRanked.values
        .sortedWith(
            compareBy<RankedPlace>({ it.score }, { it.place.name.length }, { it.place.name })
        )
        .map { it.place }

    if (merged.isNotEmpty()) {
        return ApiResponse.Success(merged)
    }

    return when (routeSearchResults) {
        is ApiResponse.Pending -> ApiResponse.Pending
        is ApiResponse.Error -> ApiResponse.Error
        is ApiResponse.Success -> ApiResponse.Success(emptyList())
    }
}

private fun relevanceScore(place: Place, normalizedQuery: String): Int {
    val name = place.name.lowercase()
    val detail = place.detail.orEmpty().lowercase()

    return when {
        name.startsWith(normalizedQuery) -> 0
        name.contains(normalizedQuery) -> 1
        detail.startsWith(normalizedQuery) -> 2
        detail.contains(normalizedQuery) -> 3
        else -> NO_MATCH_SCORE
    }
}

private fun placeSearchStableId(place: Place): String {
    return listOf(
        place.type.name,
        place.name,
        place.detail.orEmpty(),
        place.latitude.toString(),
        place.longitude.toString()
    ).joinToString("|")
}

