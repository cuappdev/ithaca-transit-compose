package com.cornellappdev.transit.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.transit.models.RouteRepository
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.UserPreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
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
     * A flow emitting all the locations the user has favorited as a list of stops.
     */
    val favoritesStops = userPreferenceRepository.favoritesFlow

    /**
     * Asynchronous function to remove a stop from favorites
     */
    fun removeFavorite(stop: Place?) {
        if (stop != null) {
            val currentFavorites = favoritesStops.value.toMutableSet()
            currentFavorites.remove(stop)
            viewModelScope.launch {
                userPreferenceRepository.setFavorites(currentFavorites.toSet())
            }
        }
    }

    /**
     * Asynchronous function to add a stop to favorites
     */
    fun addFavorite(stop: Place?) {
        if (stop != null) {
            val currentFavorites = favoritesStops.value.toMutableSet()
            currentFavorites.add(stop)
            viewModelScope.launch {
                userPreferenceRepository.setFavorites(currentFavorites.toSet())
            }
        }
    }

}