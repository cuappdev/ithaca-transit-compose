package com.cornellappdev.transit.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.transit.models.RouteRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * ViewModel handling home screen UI state and search functionality
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val routeRepository: RouteRepository
) : ViewModel() {

    //TODO: Replace with Flow from backend, this is a placeholder
    private val _placeFlow = MutableStateFlow(List(100) { "Gates Hall" })
    val placeData = _placeFlow.asStateFlow()

    /**
     * Flow of all TCAT stops
     */
    val stopFlow = routeRepository.stopFlow

    /**
     * Flow from backend of last route fetched
     */
    val lastRouteFlow = routeRepository.lastRouteFlow


    /**
     * Default map location
     */
    val defaultIthaca = LatLng(42.44, -76.50)

    /**
     * The current query in the search bar
     */
    val searchQuery = mutableStateOf("")

    /**
     * Perform a search on the string [query]
     */
    fun onSearch(query: String) {
        Log.d("SEARCHED", query)
    }

    /**
     * Change the query in the search bar
     */
    fun onQueryChange(query: String) {
        searchQuery.value = query;
    }

    /**
     * Get all TCAT stops
     */
    fun getAllStops() {
        viewModelScope.launch {
            routeRepository.getAllStops()
        }
    }

    /**
     * Load a route path for an origin and a destination and update Flow
     * @param end The latitude and longitude of the destination
     * @param time The time of the route request
     * @param destinationName The name of the destination
     * @param start The latitude and longitude of the origin
     * @param arriveBy Whether the route must complete by a certain time
     * @param originName The name of the origin
     */
    fun getRoute(
        end: LatLng,
        time: Double,
        destinationName: String,
        start: LatLng,
        arriveBy: Boolean,
        originName: String
    ) {
        viewModelScope.launch {
            routeRepository.fetchRoute(
                end = end,
                time = time,
                destinationName = destinationName,
                start = start,
                arriveBy = arriveBy,
                originName = originName
            )
        }
    }

}