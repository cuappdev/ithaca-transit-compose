package com.cornellappdev.transit.ui.viewmodels

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.MutableState
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
    val startPl: MutableStateFlow<LocationUIState> =
        MutableStateFlow(
            LocationUIState.CurrentLocation(currentLocation.value?.longitude?.let {
                currentLocation.value?.latitude?.let { it1 ->
                    LatLng(
                        it, it1
                    )
                }
            })
        )

    /**
     * Pair of the name of the ending location and the coordinates
     */
    val destPl: MutableStateFlow<LocationUIState> = MutableStateFlow(
        LocationUIState.CurrentLocation(currentLocation.value?.longitude?.let {
            currentLocation.value?.latitude?.let { it1 ->
                LatLng(
                    it, it1
                )
            }
        })
    )

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
        viewModelScope.launch {
            launch {
                userPreferenceRepository.favoritesFlow.collect {
                    if (_searchBarUiState.value is SearchBarUIState.RecentAndFavorites) {
                        _searchBarUiState.value =
                            (_searchBarUiState.value as SearchBarUIState.RecentAndFavorites).copy(
                                favorites = it
                            )
                    }
                }
            }
            launch {
                userPreferenceRepository.recentsFlow.collect {
                    if (_searchBarUiState.value is SearchBarUIState.RecentAndFavorites) {
                        _searchBarUiState.value =
                            (_searchBarUiState.value as SearchBarUIState.RecentAndFavorites).copy(
                                recents = it
                            )
                    }
                }
            }
            launch {
                routeRepository.placeFlow.collect {
                    if (_searchBarUiState.value is SearchBarUIState.Query) {
                        _searchBarUiState.value =
                            (_searchBarUiState.value as SearchBarUIState.Query).copy(
                                searched = it
                            )
                    }
                }
            }
            launch {
                currentLocation.collect {
                    if (startPl.value is LocationUIState.CurrentLocation) {
                        if (it != null) {
                            startPl.value = LocationUIState.CurrentLocation(
                                LatLng(it.latitude, it.longitude)
                            )
                        }
                    }
                    if (destPl.value is LocationUIState.CurrentLocation) {
                        if (it != null) {
                            destPl.value = LocationUIState.CurrentLocation(
                                LatLng(it.latitude, it.longitude)
                            )
                        }
                    }
                }
            }

            launch {
                // Every time startPl, destPl, or arriveBy changes, make a route request
                combine(startPl, destPl, arriveByFlow) { start, dest, arriveBy ->
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
            }
        }
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
    fun changeStartLocation(location: LocationUIState) {
        startPl.value = location
    }

    /**
     * Change end location
     */
    fun changeEndLocation(location: LocationUIState) {
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

}

