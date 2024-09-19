package com.cornellappdev.transit.ui.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.transit.models.LocationRepository
import com.cornellappdev.transit.models.MapState
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.RouteOptionType
import com.cornellappdev.transit.models.RouteRepository
import com.cornellappdev.transit.networking.ApiResponse
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * ViewModel handling home screen UI state and search functionality
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val routeRepository: RouteRepository,
    private val locationRepository: LocationRepository
) : ViewModel() {

    /**
     * Flow from backend of last route fetched
     */
    val lastRouteFlow = routeRepository.lastRouteFlow

    /**
     * The current query in the search bar, as a StateFlow
     */
    val searchQuery: MutableStateFlow<String> = MutableStateFlow("")

    val placeQueryFlow: StateFlow<ApiResponse<List<Place>>> = routeRepository.placeFlow

    /**
     * The current query in the add favorites search bar, as a StateFlow
     */
    val addSearchQuery: MutableStateFlow<String> = MutableStateFlow("")

    /**
     * Default map location
     */
    val defaultIthaca = LatLng(42.44, -76.50)

    init {
        viewModelScope.launch {
            launch {
                searchQuery.collect { it ->
                    routeRepository.makeSearch(it)
                }
            }
            launch {
                addSearchQuery.collect { it ->
                    routeRepository.makeSearch(it)
                }
            }
        }
    }

    /**
     * Perform a search on the string [query]
     */
    fun onSearch(query: String) {
        Log.d("SEARCHED", query)
    }

    /**
     * True if the stop can be searched via the [query] string
     */
    private fun fulfillsQuery(stop: Place, query: String): Boolean {
        return !query.isBlank() && stop.name.lowercase().contains(query.lowercase())
    }

    /**
     * Change the query in the search bar and update search results
     */
    fun onQueryChange(query: String) {
        searchQuery.value = query;
    }

    /**
     * Change the query in the add favorites search bar and update search results
     */
    fun onAddQueryChange(query: String) {
        addSearchQuery.value = query;
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

    /**
     * Value of the current location. Can be null
     */
    val currentLocation = locationRepository.currentLocation

    /**
     * Start emitting location from [locationRepository]
     */
    fun instantiateLocation(context: Context) {
        locationRepository.instantiate(context)
    }

    // Map state

    /**
     * Emits whether a route should be showing on the map
     */
    val mapState: MutableStateFlow<MapState> =
        MutableStateFlow(MapState(isShowing = false, routeOptionType = RouteOptionType.None))

    /**
     * Set map state for home screen
     */
    fun setMapState(value: MapState) {
        mapState.value = value
    }


}