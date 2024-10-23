package com.cornellappdev.transit.ui.viewmodels

import android.content.Context
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.transit.models.LocationRepository
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.RouteOptions
import com.cornellappdev.transit.models.RouteRepository
import com.cornellappdev.transit.networking.ApiResponse
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalMaterial3Api::class)
class RouteViewModel @Inject constructor(
    private val routeRepository: RouteRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    /**
     * Value of the current location. Can be null
     */
    val currentLocation = locationRepository.currentLocation

    /**
     * Pair of the name of the starting location and the coordinates
     */
    val startPl: MutableStateFlow<Pair<String, LatLng?>> =
        MutableStateFlow("Current Location" to null)

    /**
     * Pair of the name of the ending location and the coordinates
     */
    val destPl: MutableStateFlow<Pair<String, LatLng?>> = MutableStateFlow("" to null)

    val time = "12:00AM"

    // Route select sheet
    /**
     * The current query in the search bar, as a StateFlow
     */
    val searchQuery: MutableStateFlow<String> = MutableStateFlow("")

    val placeQueryFlow: StateFlow<ApiResponse<List<Place>>> = routeRepository.placeFlow

    val lastRouteFlow: StateFlow<ApiResponse<RouteOptions>> = routeRepository.lastRouteFlow

    init {
        viewModelScope.launch {
            launch {
                searchQuery.collect { it ->
                    routeRepository.makeSearch(it)
                }
            }
        }
    }


    /**
     * Change the query in the search bar and update search results
     */
    fun onQueryChange(query: String) {
        searchQuery.value = query;
    }

    /**
     * Update emitted location from [locationRepository]
     */
    fun updateLocation(context: Context) {
        locationRepository.updateLocation(context)
    }

    /**
     * Change start location
     */
    fun changeStartLocation(location: String, latitude: Double, longitude: Double) {
        startPl.value = location to LatLng(latitude, longitude)
    }

    /**
     * Change end location
     */
    fun changeEndLocation(location: String, latitude: Double, longitude: Double) {
        destPl.value = location to LatLng(latitude, longitude)
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
        val currentLocationLatLng =
            currentLocation.value?.longitude?.let {
                currentLocation.value?.latitude?.let { it1 ->
                    LatLng(
                        it1, it
                    )
                }
            }
        viewModelScope.launch {
            routeRepository.fetchRoute(
                end = if (destinationName == "Current Location" && currentLocationLatLng != null) currentLocationLatLng else end,
                time = time,
                destinationName = destinationName,
                start = if (originName == "Current Location" && currentLocationLatLng != null) currentLocationLatLng else start,
                arriveBy = arriveBy,
                originName = originName
            )
        }
    }

}

