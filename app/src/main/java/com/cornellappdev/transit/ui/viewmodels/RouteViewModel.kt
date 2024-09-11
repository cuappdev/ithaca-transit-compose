package com.cornellappdev.transit.ui.viewmodels

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.transit.models.RouteRepository
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalMaterial3Api::class)
class RouteViewModel @Inject constructor(
    private val routeRepository: RouteRepository,
) : ViewModel() {

    val startPl: MutableStateFlow<String> = MutableStateFlow("Current Location")

    val destPl: MutableStateFlow<String> = MutableStateFlow("")

    val time = "12:00AM"

    // Route select sheet
    /**
     * The current query in the search bar, as a StateFlow
     */
    val searchQuery: MutableStateFlow<String> = MutableStateFlow("")

    /**
     * Change the query in the search bar and update search results
     */
    fun onQueryChange(query: String) {
        searchQuery.value = query;
    }

    /**
     * Change start location
     */
    fun changeStateLocation(location: String) {
        startPl.value = location
    }

    /**
     * Change end location
     */
    fun changeEndLocation(location: String) {
        destPl.value = location
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

