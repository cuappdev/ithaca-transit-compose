package com.cornellappdev.transit.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.cornellappdev.transit.models.RouteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.cornellappdev.transit.networking.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

/**
 * ViewModel handling favourites screen/bottomsheet UI state
 */
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    routeRepository: RouteRepository,
) : ViewModel() {

    /**
     * A flow emitting all the locations and whether or not they have bene favorited.
     */
    //TODO: This is a placeholder. Replace with flow from UserPreferences
    private val favoritesFlow = MutableStateFlow(
        mapOf(
            "Gates Hall" to true,
            "Olin Library" to true,
            "Duffield Hall" to true,
            "Statler" to false
        )
    ).asStateFlow()

    /**
     * Flow of all TCAT stops
     */
    private val stopFlow = routeRepository.stopFlow

    private val scope = CoroutineScope(Dispatchers.Default)

    /**
     * A flow emitting all the locations the user has favorited as a list of stops.
     */
    val favoriteStops = stopFlow.combine(
        favoritesFlow
    ) { apiResponse, favorites ->
        when (apiResponse) {
            is ApiResponse.Error -> {
                emptyList()
            }

            is ApiResponse.Pending -> {
                emptyList()
            }

            is ApiResponse.Success -> {
                apiResponse.data.filter { stop ->
                    favorites[stop.name] == true
                }
            }
        }
    }.stateIn(scope, SharingStarted.Eagerly, emptyList())

}