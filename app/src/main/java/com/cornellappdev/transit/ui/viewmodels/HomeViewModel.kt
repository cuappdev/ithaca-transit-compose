package com.cornellappdev.transit.ui.viewmodels

import android.content.Context
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.transit.models.LocationRepository
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.RouteRepository
import com.cornellappdev.transit.models.SelectedRouteRepository
import com.cornellappdev.transit.models.ecosystem.StaticPlaces
import com.cornellappdev.transit.models.UserPreferenceRepository
import com.cornellappdev.transit.models.ecosystem.DayOperatingHours
import com.cornellappdev.transit.models.ecosystem.EateryRepository
import com.cornellappdev.transit.models.ecosystem.GymRepository
import com.cornellappdev.transit.models.ecosystem.UpliftCapacity
import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.ui.theme.AccentClosed
import com.cornellappdev.transit.ui.theme.AccentOpen
import com.cornellappdev.transit.ui.theme.AccentOrange
import com.cornellappdev.transit.ui.theme.LateRed
import com.cornellappdev.transit.ui.theme.LiveGreen
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.robotoFamily
import com.cornellappdev.transit.util.HIGH_CAPACITY_THRESHOLD
import com.cornellappdev.transit.util.MEDIUM_CAPACITY_THRESHOLD
import com.cornellappdev.transit.util.TimeUtils.getOpenStatus
import com.cornellappdev.transit.util.TimeUtils.toPascalCaseString
import com.cornellappdev.transit.util.calculateDistanceString
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.math.log


/**
 * ViewModel handling home screen UI state and search functionality
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val routeRepository: RouteRepository,
    private val locationRepository: LocationRepository,
    private val eateryRepository: EateryRepository,
    private val gymRepository: GymRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val selectedRouteRepository: SelectedRouteRepository
) : ViewModel() {

    /**
     * The current query in the add favorites search bar, as a StateFlow
     */
    val addSearchQuery: MutableStateFlow<String> = MutableStateFlow("")

    /**
     * The list of queried places retrieved from the route repository, as a StateFlow.
     */
    val placeQueryFlow: StateFlow<ApiResponse<List<Place>>> = routeRepository.placeFlow

    /**
     * The current UI state of the search bar, as a MutableStateFlow
     */
    private val _searchBarUiState: MutableStateFlow<SearchBarUIState> =
        MutableStateFlow(SearchBarUIState.RecentAndFavorites(emptySet(), emptyList()))
    val searchBarUiState: StateFlow<SearchBarUIState> = _searchBarUiState.asStateFlow()

    /**
     * Default map location
     */
    val defaultIthaca = LatLng(42.44, -76.50)

    val filterList = listOf(
        FilterState.FAVORITES,
        FilterState.GYMS,
        FilterState.EATERIES,
        FilterState.LIBRARIES,
        FilterState.PRINTERS
    )

    val filterState: MutableStateFlow<FilterState> = MutableStateFlow(FilterState.FAVORITES)

    private val _showFilterSheet = MutableStateFlow(false)
    val showFilterSheet: StateFlow<Boolean> = _showFilterSheet.asStateFlow()

    fun toggleFilterSheet(show: Boolean) {
        _showFilterSheet.value = show
    }

    val staticPlacesFlow =
        combine(
            routeRepository.printerFlow,
            routeRepository.libraryFlow,
            eateryRepository.eateryFlow,
            gymRepository.gymFlow
        ) { printers, libraries, eateries, gyms ->
            StaticPlaces(
                printers,
                libraries,
                eateries,
                gyms
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = StaticPlaces(
                ApiResponse.Pending,
                ApiResponse.Pending,
                ApiResponse.Pending,
                ApiResponse.Pending
            )
        )

    private val _showAddFavoritesSheet = MutableStateFlow(false)
    val showAddFavoritesSheet: StateFlow<Boolean> = _showAddFavoritesSheet.asStateFlow()

    fun toggleAddFavoritesSheet(show: Boolean) {
        _showAddFavoritesSheet.value = show
    }

    val favoritesFilterList = listOf(
        FavoritesFilterSheetState.GYMS,
        FavoritesFilterSheetState.EATERIES,
        FavoritesFilterSheetState.LIBRARIES,
        FavoritesFilterSheetState.PRINTERS,
        FavoritesFilterSheetState.OTHER
    )

    private val _selectedFavoritesFilters =
        MutableStateFlow<Set<FavoritesFilterSheetState>>(emptySet())
    val selectedFavoritesFilters: StateFlow<Set<FavoritesFilterSheetState>> =
        _selectedFavoritesFilters.asStateFlow()

    private val _appliedFavoritesFilters =
        MutableStateFlow<Set<FavoritesFilterSheetState>>(emptySet())
    val appliedFavoritesFilters: StateFlow<Set<FavoritesFilterSheetState>> =
        _appliedFavoritesFilters.asStateFlow()

    fun toggleFavoritesFilter(filter: FavoritesFilterSheetState) {
        _selectedFavoritesFilters.value = if (filter in _selectedFavoritesFilters.value) {
            _selectedFavoritesFilters.value - filter
        } else {
            _selectedFavoritesFilters.value + filter
        }
    }

    fun applyFavoritesFilters() {
        // Save the current selection as applied filters
        _appliedFavoritesFilters.value = _selectedFavoritesFilters.value
        toggleFilterSheet(false)
    }

    fun removeAppliedFilter(filter: FavoritesFilterSheetState) {
        _selectedFavoritesFilters.value -= filter
        _appliedFavoritesFilters.value = _appliedFavoritesFilters.value - filter
    }

    fun cancelFavoritesFilters() {
        // Restore the previously applied filters
        _selectedFavoritesFilters.value = _appliedFavoritesFilters.value
        toggleFilterSheet(false)
    }

    fun openFilterSheet() {
        // Initialize selected filters with currently applied filters
        _selectedFavoritesFilters.value = _appliedFavoritesFilters.value
        toggleFilterSheet(true)
    }


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

        searchBarUiState
            .debounce(300L)
            .filterIsInstance<SearchBarUIState.Query>()
            .map { it.queryText }
            .distinctUntilChanged()
            .onEach {
                routeRepository.makeSearch(it)
            }.launchIn(viewModelScope)

        addSearchQuery.debounce(300L).distinctUntilChanged().onEach {
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
     * Clear the query in the add favorites search bar
     */
    fun clearAddQuery() {
        onAddQueryChange("")
    }

    /**
     * Change the query in the add favorites search bar and update search results
     */
    fun onAddQueryChange(query: String) {
        addSearchQuery.value = query
    }

    /**
     * Asynchronous function to add a stop to recents
     */
    fun addRecent(stop: Place?) {
        if (stop != null) {
            viewModelScope.launch {
                userPreferenceRepository.setRecents(stop)
            }
        }
    }

    /**
     * Asynchronous function to clear set of recents
     */
    fun clearRecents() {
        viewModelScope.launch {
            userPreferenceRepository.clearRecents()
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

    /**
     * Value of the current location. Can be null
     */
    val currentLocation = locationRepository.currentLocation

    /**
     * Change start location
     */
    fun changeStartLocation(location: LocationUIState) {
        selectedRouteRepository.setStartPlace(location)
    }

    /**
     * Change end location
     */
    fun changeEndLocation(location: LocationUIState) {
        selectedRouteRepository.setDestPlace(location)
    }

    /**
     * Start emitting location from [locationRepository]
     */
    fun instantiateLocation(context: Context) {
        locationRepository.instantiate(context)
    }

    /**
     * Prepares the ViewModel to navigate from the current location to [place].
     * Adds the place to recents and resets search fields
     */
    fun beginRouteOptions(place: Place) {
        addRecent(place)
        changeStartLocation(
            LocationUIState.CurrentLocation
        )
        changeEndLocation(
            LocationUIState.Place(
                place.name,
                LatLng(
                    place.latitude,
                    place.longitude
                )
            )
        )
        onQueryChange("")
    }

    /**
     * Set the filter selected on the bottom sheet for categories of places
     */
    fun setCategoryFilter(filterState: FilterState) {
        this.filterState.value = filterState
    }

    /**
     * Returns a distance string from a location to the current location if both exist, otherwise returns empty string
     */
    fun distanceStringIfCurrentLocationExists(latitude: Double?, longitude: Double?): String {
        val currentLocationSnapshot = currentLocation.value
        if (currentLocationSnapshot != null && latitude != null && longitude != null) {
            return " - " +
                    calculateDistanceString(
                        LatLng(
                            currentLocationSnapshot.latitude,
                            currentLocationSnapshot.longitude
                        ), LatLng(latitude, longitude)
                    )

        }
        return ""
    }
}