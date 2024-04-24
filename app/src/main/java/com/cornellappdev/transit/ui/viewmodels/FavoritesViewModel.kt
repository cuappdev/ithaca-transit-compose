package com.cornellappdev.transit.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.transit.models.RouteRepository
import com.cornellappdev.transit.models.UserPreferenceRepository
import com.cornellappdev.transit.networking.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel handling favourites screen/bottomsheet UI state
 */
@HiltViewModel
class FavoritesViewModel @Inject constructor(
    routeRepository: RouteRepository,
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

    /**
     * A flow emitting all the locations and whether or not they have been favorited.
     */
    private val favoritesFlow = userPreferenceRepository.favoritesFlow
    //private val favoritesFlow: StateFlow<Set<String>> = MutableStateFlow(setOf("Gates Hall", "Duffield")).asStateFlow()

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
                    favorites.contains(stop.name)
                }
            }
        }
    }.stateIn(scope, SharingStarted.Eagerly, emptyList())

    /**
     * Asynchronous function to remove a stop from favorites
     */
    fun removeFavorite(stop: String?) {
        if (stop != null) {
            val currentFavorites = favoritesFlow.value.toMutableSet()
            currentFavorites.remove(stop)
            viewModelScope.launch {
                userPreferenceRepository.setFavorites(currentFavorites.toSet())
            }
        }
    }

    /**
     * Asynchronous function to add a stop to favorites
     */
    fun addFavorite(stop: String?) {
        if (stop != null) {
            val currentFavorites = favoritesFlow.value.toMutableSet()
            currentFavorites.add(stop)
            viewModelScope.launch {
                userPreferenceRepository.setFavorites(currentFavorites.toSet())
            }
        }
    }

}