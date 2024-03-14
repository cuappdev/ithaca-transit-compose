package com.cornellappdev.transit.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.cornellappdev.transit.models.RouteRepository
import com.cornellappdev.transit.models.Stop
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

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val routeRepository: RouteRepository,
) : ViewModel() {

    /**
     * A flow emitting all the locations the user has favorited.
     */

    //TODO: This is a placeholder. Replace with flow from UserPreferences
    private val _favoritesFlow = MutableStateFlow(
        mapOf(
            "Gates hall" to true,
            "olin library" to true,
            "duffield hall" to true,
            "statler" to false
        )
    )
    private val favoritesFlow = _favoritesFlow.asStateFlow()

    val stopFlow = routeRepository.stopFlow

    private val scope = CoroutineScope(Dispatchers.Default)
    private fun fulfillsFilter(stop: Stop, favorites: Map<String, Boolean>): Boolean {

        Log.e("LOGLOGLOG", stop.name)
        if (favorites.get(stop.name) != null) {

            Log.e("LOGLOGLOG", favorites.get(stop.name).toString())
            return favorites.get(stop.name)!!

        }
        return false
    }

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
                    fulfillsFilter(stop, favorites)
                }
                //Log.e("LOGLOGLOG", "filtering")
            }
        }
    }.stateIn(scope, SharingStarted.Eagerly, emptyList())

}