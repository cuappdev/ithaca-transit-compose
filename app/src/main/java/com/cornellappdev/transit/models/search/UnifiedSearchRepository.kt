package com.cornellappdev.transit.models.search

import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.RouteRepository
import com.cornellappdev.transit.models.ecosystem.EateryRepository
import com.cornellappdev.transit.models.ecosystem.GymRepository
import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.util.ecosystem.buildEcosystemSearchPlaces
import com.cornellappdev.transit.util.ecosystem.mergeAndRankSearchResults
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Centralized search data source that merges route search results with ecosystem places.
 *
 * UI and ViewModels can share this data pipeline to keep search behavior consistent.
 */
@Singleton
class UnifiedSearchRepository @Inject constructor(
    private val routeRepository: RouteRepository,
    eateryRepository: EateryRepository,
    gymRepository: GymRepository,
    coroutineScope: CoroutineScope,
) {

    val ecosystemSearchPlacesFlow: StateFlow<List<Place>> =
        combine(
            routeRepository.printerFlow,
            routeRepository.libraryFlow,
            eateryRepository.eateryFlow,
            gymRepository.gymFlow
        ) { printers, libraries, eateries, gyms ->
            buildEcosystemSearchPlaces(
                printers = printers,
                libraries = libraries,
                eateries = eateries,
                gyms = gyms
            )
        }.stateIn(
            scope = coroutineScope,
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    fun mergedSearchResults(queryFlow: Flow<String>): Flow<ApiResponse<List<Place>>> =
        combine(
            queryFlow,
            routeRepository.placeFlow,
            ecosystemSearchPlacesFlow
        ) { query, routeSearchResults, ecosystemPlaces ->
            mergeAndRankSearchResults(
                query = query,
                routeSearchResults = routeSearchResults,
                ecosystemPlaces = ecosystemPlaces
            )
        }
}



