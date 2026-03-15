package com.cornellappdev.transit.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.transit.models.DirectionType
import com.cornellappdev.transit.models.LocationRepository
import com.cornellappdev.transit.models.MapState
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.Route
import com.cornellappdev.transit.models.RouteOptions
import com.cornellappdev.transit.models.RouteRepository
import com.cornellappdev.transit.models.SelectedRouteRepository
import com.cornellappdev.transit.models.UserPreferenceRepository
import com.cornellappdev.transit.models.ecosystem.EateryRepository
import com.cornellappdev.transit.models.ecosystem.GymRepository
import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.util.TimeUtils
import com.cornellappdev.transit.util.ecosystem.buildEcosystemSearchPlaces
import com.cornellappdev.transit.util.ecosystem.mergeAndRankSearchResults
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date
import javax.inject.Inject

@HiltViewModel
@OptIn(FlowPreview::class)
class RouteViewModel @Inject constructor(
    private val routeRepository: RouteRepository,
    private val locationRepository: LocationRepository,
    private val eateryRepository: EateryRepository,
    private val gymRepository: GymRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val selectedRouteRepository: SelectedRouteRepository
) : ViewModel() {

    /**
     * Value of the current location. Can be null
     */
    val currentLocation = locationRepository.currentLocation

    /**
     * The starting and ending locations in route options
     */
    val selectedRoute = selectedRouteRepository.selectedRoute

    /**
     * State of the arriveBy selector
     */
    private val _arriveByFlow: MutableStateFlow<ArriveByUIState> = MutableStateFlow(
        ArriveByUIState.LeaveNow()
    )
    val arriveByFlow: StateFlow<ArriveByUIState> = _arriveByFlow.asStateFlow()

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
     * Emits whether a route should be showing on the map
     */
    private val _mapState: MutableStateFlow<MapState> =
        MutableStateFlow(
            MapState(
                isShowing = false,
                route = null
            )
        )
    val mapState: StateFlow<MapState> = _mapState.asStateFlow()

    /**
     * Emits details of a route
     */
    private val _detailsState: MutableStateFlow<List<DirectionDetails>> =
        MutableStateFlow(
            emptyList()
        )
    val detailsState: StateFlow<List<DirectionDetails>> = _detailsState.asStateFlow()

    /**
     * The current UI state of the search bar, as a MutableStateFlow
     */
    private val _searchBarUiState: MutableStateFlow<SearchBarUIState> =
        MutableStateFlow(SearchBarUIState.RecentAndFavorites(emptySet(), emptyList()))
    val searchBarUiState: StateFlow<SearchBarUIState> = _searchBarUiState.asStateFlow()

    private val ecosystemSearchPlacesFlow: StateFlow<List<Place>> =
        combine(
            routeRepository.printerFlow,
            routeRepository.libraryFlow,
            eateryRepository.eateryFlow,
            gymRepository.gymFlow
        ) { printers, libraries, eateries, gyms ->
            buildEcosystemSearchPlaces(
                printers = printers,
                libraries = libraries,
                eateries = eateries,
                gyms = gyms
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

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

        combine(
            searchBarUiState
                .filterIsInstance<SearchBarUIState.Query>()
                .map { it.queryText }
                .distinctUntilChanged(),
            routeRepository.placeFlow,
            ecosystemSearchPlacesFlow
        ) { query, routeSearchResults, ecosystemPlaces ->
            query to mergeAndRankSearchResults(
                query = query,
                routeSearchResults = routeSearchResults,
                ecosystemPlaces = ecosystemPlaces
            )
        }.onEach { (query, mergedResults) ->
            val currentState = _searchBarUiState.value
            if (currentState is SearchBarUIState.Query && currentState.queryText == query) {
                _searchBarUiState.value = currentState.copy(searched = mergedResults)
            }
        }.launchIn(viewModelScope)


        combine(selectedRoute, _arriveByFlow) { startAndEnd, arriveBy ->
            val startState = startAndEnd.startPlace
            val endState = startAndEnd.endPlace
            getLatestOptions(startState, endState, arriveBy)
        }.launchIn(viewModelScope)

        _mapState.onEach {
            if (it.route == null) {
                _detailsState.value = emptyList()
            } else {
                _detailsState.value = it.route.directions.map { direction ->
                    DirectionDetails(
                        startTime = TimeUtils.getHHMM(
                            direction.startTime
                        ),
                        endTime = TimeUtils.getHHMM(direction.endTime),
                        movementDescription = if (direction.type == DirectionType.DEPART) {
                            (if (direction.stayOnBusForTransfer == true)
                                "Bus becomes" else "Board")
                        } else {
                            "Walk to"
                        },
                        destination = direction.name,
                        directionType = direction.type,
                        busNumber = direction.routeId ?: "",
                        numStops = direction.stops.size - 1, // Ignore origin stop
                        duration = TimeUtils.minuteDifference(
                            direction.startTime,
                            direction.endTime
                        ),
                        stops = direction.stops,
                        busTransfer = direction.stayOnBusForTransfer ?: false,
                        delay = direction.delay ?: 0,
                        delayedStartTime = if (direction.delay != null && direction.delay > 0) TimeUtils.getHHMM(
                            TimeUtils.addSecondsToTime(
                                direction.startTime,
                                direction.delay
                            )
                        ) else null,
                        delayedEndTime = if (direction.delay != null && direction.delay > 0) TimeUtils.getHHMM(
                            TimeUtils.addSecondsToTime(
                                direction.endTime,
                                direction.delay
                            )
                        ) else null
                    )
                }
            }

        }.launchIn(viewModelScope)

        searchBarUiState
            .debounce(300L)
            .filterIsInstance<SearchBarUIState.Query>()
            .map { it.queryText }
            .distinctUntilChanged()
            .onEach {
                routeRepository.makeSearch(it)
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
    }

    /**
     * Change the start location by calling [SelectedRouteRepository.setStartPlace]
     * @param location The new starting location as a LocationUIState
     */
    fun setStartPlace(location: LocationUIState) {
        selectedRouteRepository.setStartPlace(location)
    }

    /**
     * Change the start location by calling [SelectedRouteRepository.setDestPlace]
     * @param location The new starting location as a LocationUIState
     */
    fun setDestPlace(location: LocationUIState) {
        selectedRouteRepository.setDestPlace(location)
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
    private fun getRoute(
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
        _arriveByFlow.value = arriveBy
    }

    /**
     * Set map state for home screen
     */
    fun setMapState(value: MapState) {
        _mapState.value = value
    }

    /**
     * Swap start and destination locations
     */
    fun swapLocations() {
        selectedRouteRepository.swapPlaces()
    }

    /**
     * Retrieve coordinates for a place or for the current location
     */
    private fun getCoordinates(location: LocationUIState): LatLng? {
        when (location) {
            is LocationUIState.CurrentLocation -> {
                return currentLocation.value?.latitude?.let { lat ->
                    currentLocation.value?.longitude?.let { long ->
                        LatLng(
                            lat,
                            long
                        )
                    }
                }
            }

            is LocationUIState.Place -> {
                return location.coordinates
            }
        }
    }

    /**
     * Get map bounds for an entire route
     */
    fun getLatLngBounds(route: Route): LatLngBounds {

        var bounds = LatLngBounds.builder()

        route.directions.forEach { direction ->
            direction.path.forEach { latLng ->
                bounds = bounds.include(latLng)
            }
        }

        return bounds.build()
    }

    /**
     * Get latest route options given start location, end location, and arrive by information
     */
    private fun getLatestOptions(
        startState: LocationUIState,
        endState: LocationUIState,
        arriveByState: ArriveByUIState
    ) {
        getCoordinates(endState)?.let { end ->
            getCoordinates(startState)?.let { start ->
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

    /**
     * Refresh route options on UI
     */
    fun refreshOptions() {
        getLatestOptions(
            selectedRoute.value.startPlace,
            selectedRoute.value.endPlace,
            _arriveByFlow.value
        )
    }


}

