package com.cornellappdev.transit.ui.viewmodels

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.transit.models.LocationRepository
import com.cornellappdev.transit.models.MapState
import com.cornellappdev.transit.models.RouteOptions
import com.cornellappdev.transit.models.RouteRepository
import com.cornellappdev.transit.models.UserPreferenceRepository
import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.util.TimeUtils
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
@OptIn(ExperimentalMaterial3Api::class)
class RouteViewModel @Inject constructor(
    private val routeRepository: RouteRepository,
    private val locationRepository: LocationRepository,
    private val userPreferenceRepository: UserPreferenceRepository
) : ViewModel() {

    /**
     * Value of the current location. Can be null
     */
    val currentLocation = locationRepository.currentLocation

    /**
     * Pair of the name of the starting location and the coordinates
     */
    val startPlace = routeRepository.startPlace


    /**
     * Pair of the name of the ending location and the coordinates
     */
    val destPlace = routeRepository.destPlace

    /**
     * State of the arriveBy selector
     */
    val arriveByFlow: MutableStateFlow<ArriveByUIState> = MutableStateFlow(
        ArriveByUIState.LeaveNow()
    )

    /**
     * State of date picker
     */
    val dateState: MutableState<String> = mutableStateOf(
        TimeUtils.dateFormatter.format(
            Date.from(Instant.now())
        )
    )

    /**
     * State of time picker
     */
    val timeState: MutableState<String> = mutableStateOf(
        TimeUtils.timeFormatter.format(
            Date.from(Instant.now())
        )
    )

    /**
     * State of arriveBy button
     */
    val selectedArriveByButton: MutableState<Int> = mutableIntStateOf(
        0
    )


    val lastRouteFlow: StateFlow<ApiResponse<RouteOptions>> = routeRepository.lastRouteFlow

    /**
     * Default map location
     */
    val defaultIthaca = LatLng(42.44, -76.50)

    /**
     * The current UI state of the search bar, as a MutableStateFlow
     */
    private val _searchBarUiState: MutableStateFlow<SearchBarUIState> =
        MutableStateFlow(SearchBarUIState.RecentAndFavorites(emptySet(), emptyList()))
    val searchBarUiState: StateFlow<SearchBarUIState> = _searchBarUiState.asStateFlow()

    init {
        userPreferenceRepository.favoritesFlow.onEach {
            if (_searchBarUiState.value is SearchBarUIState.RecentAndFavorites) {
                _searchBarUiState.value =
                    (_searchBarUiState.value as SearchBarUIState.RecentAndFavorites).copy(
                        favorites = it
                    )
            }
        }.launchIn(viewModelScope)

        userPreferenceRepository.recentsFlow.onEach {
            if (_searchBarUiState.value is SearchBarUIState.RecentAndFavorites) {
                _searchBarUiState.value =
                    (_searchBarUiState.value as SearchBarUIState.RecentAndFavorites).copy(
                        recents = it
                    )
            }
        }.launchIn(viewModelScope)

        routeRepository.placeFlow.onEach {
            if (_searchBarUiState.value is SearchBarUIState.Query) {
                _searchBarUiState.value =
                    (_searchBarUiState.value as SearchBarUIState.Query).copy(
                        searched = it
                    )
            }
        }.launchIn(viewModelScope)

        currentLocation.onEach {
            if (startPlace.value is LocationUIState.CurrentLocation) {
                if (it != null) {
                    routeRepository.setStartLocation(
                        LocationUIState.CurrentLocation(
                            LatLng(it.latitude, it.longitude)
                        )
                    )
                }
            }
            if (destPlace.value is LocationUIState.CurrentLocation) {
                if (it != null) {
                    routeRepository.setEndLocation(
                        LocationUIState.CurrentLocation(
                            LatLng(it.latitude, it.longitude)
                        )
                    )
                }
            }
        }.launchIn(viewModelScope)

        combine(startPlace, destPlace, arriveByFlow) { start, dest, arriveBy ->
            Triple(start, dest, arriveBy)
        }.onEach {
            // Every time startPlace, destPlace, or arriveBy changes, make a route request
            combine(startPlace, destPlace, arriveByFlow) { start, dest, arriveBy ->
                Triple(start, dest, arriveBy)
            }.collect {
                val startState = it.first
                val endState = it.second
                val arriveByState = it.third
                getCoordinatesFromLocationState(it.second)?.let { end ->
                    getCoordinatesFromLocationState(it.first)?.let { start ->
                        getRoute(
                            end = end,
                            start = start,
                            arriveBy = arriveByState is ArriveByUIState.ArriveBy,
                            destinationName = if (endState is LocationUIState.Place) endState.name else "Current Location",
                            originName = if (startState is LocationUIState.Place) startState.name else "Current Location",
                            time = (arriveByState.date.time / 1000).toDouble()
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }


    /**
     * Change the query in the search bar and update search results
     */
    fun onQueryChange(query: String) {
        if (query == "") {
            _searchBarUiState.value = SearchBarUIState.RecentAndFavorites(
                userPreferenceRepository.favoritesFlow.value,
                userPreferenceRepository.recentsFlow.value
            )
        } else {
            _searchBarUiState.value = SearchBarUIState.Query(
                ApiResponse.Pending, query
            )
        }
        routeRepository.makeSearch(query)
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
    fun setStartLocation(location: LocationUIState) {
        routeRepository.setStartLocation(location)
    }

    /**
     * Change end location
     */
    fun setEndLocation(location: LocationUIState) {
        routeRepository.setEndLocation(location)
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
     * Change the arriveBy parameter for routes
     */
    fun changeArriveBy(arriveBy: ArriveByUIState) {
        arriveByFlow.value = arriveBy
    }

    /**
     * Emits whether a route should be showing on the map
     */
    val mapState: MutableStateFlow<MapState> =
        MutableStateFlow(
            MapState(
                isShowing = false,
                route = null
            )
        )

    /**
     * Set map state for home screen
     */
    fun setMapState(value: MapState) {
        mapState.value = value
    }

    /**
     * Swap start and destination locations
     */
    fun swapLocations() {
        val temp = startPlace.value
        routeRepository.setStartLocation(destPlace.value)
        routeRepository.setEndLocation(temp)
    }

}

