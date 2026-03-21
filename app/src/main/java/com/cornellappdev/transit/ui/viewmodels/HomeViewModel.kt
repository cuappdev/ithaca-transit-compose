package com.cornellappdev.transit.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cornellappdev.transit.models.LocationRepository
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.PlaceType
import com.cornellappdev.transit.models.RouteRepository
import com.cornellappdev.transit.models.SelectedRouteRepository
import com.cornellappdev.transit.models.ecosystem.StaticPlaces
import com.cornellappdev.transit.models.UserPreferenceRepository
import com.cornellappdev.transit.models.search.UnifiedSearchRepository
import com.cornellappdev.transit.models.ecosystem.Eatery
import com.cornellappdev.transit.models.ecosystem.EateryRepository
import com.cornellappdev.transit.models.ecosystem.GymRepository
import com.cornellappdev.transit.models.ecosystem.Library
import com.cornellappdev.transit.models.ecosystem.Printer
import com.cornellappdev.transit.models.ecosystem.UpliftGym
import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.util.StringUtils.fromMetersToFeet
import com.cornellappdev.transit.util.calculateDistance
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.util.Locale

/**
 * ViewModel handling home screen UI state and search functionality
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val routeRepository: RouteRepository,
    private val locationRepository: LocationRepository,
    private val eateryRepository: EateryRepository,
    private val gymRepository: GymRepository,
    private val unifiedSearchRepository: UnifiedSearchRepository,
    private val userPreferenceRepository: UserPreferenceRepository,
    private val selectedRouteRepository: SelectedRouteRepository
) : ViewModel() {

    /**
     * The current query in the add favorites search bar, as a StateFlow
     */
    private val _addSearchQuery: MutableStateFlow<String> = MutableStateFlow("")
    val addSearchQuery: StateFlow<String> = _addSearchQuery.asStateFlow()

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

    private val _filterState: MutableStateFlow<FilterState> =
        MutableStateFlow(FilterState.FAVORITES)
    val filterState: StateFlow<FilterState> = _filterState.asStateFlow()

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

    private val homeQueryFlow: StateFlow<String> = searchBarUiState
        .map { state ->
            when (state) {
                is SearchBarUIState.Query -> state.queryText
                is SearchBarUIState.RecentAndFavorites -> ""
            }
        }
        .distinctUntilChanged()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ""
        )

    private val mergedHomeSearchResultsFlow: StateFlow<ApiResponse<List<Place>>> =
        unifiedSearchRepository.mergedSearchResults(homeQueryFlow)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ApiResponse.Success(emptyList())
            )

    private val _showAddFavoritesSheet = MutableStateFlow(false)
    val showAddFavoritesSheet: StateFlow<Boolean> = _showAddFavoritesSheet.asStateFlow()

    val addSearchResultsFlow: StateFlow<ApiResponse<List<Place>>> =
        unifiedSearchRepository.mergedSearchResults(_addSearchQuery)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = ApiResponse.Success(emptyList())
            )

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

    val ecosystemFavoritesUiState: StateFlow<EcosystemFavoritesUiState> = combine(
        userPreferenceRepository.favoritesFlow,
        staticPlacesFlow,
        appliedFavoritesFilters
    ) { favorites, staticPlaces, appliedFilters ->
        val allowedTypes = appliedFilters.toAllowedPlaceTypes()

        val filteredSortedFavorites = favorites.asSequence()
            .filter { allowedTypes.isEmpty() || it.type in allowedTypes }
            .sortedWith(compareBy<Place>({ it.type.ordinal }, { it.name }))
            .toList()

        val eateries = (staticPlaces.eateries as? ApiResponse.Success)?.data.orEmpty()
        val libraries = (staticPlaces.libraries as? ApiResponse.Success)?.data.orEmpty()
        val gyms = (staticPlaces.gyms as? ApiResponse.Success)?.data.orEmpty()
        val printers = (staticPlaces.printers as? ApiResponse.Success)?.data.orEmpty()

        EcosystemFavoritesUiState(
            filteredSortedFavorites = filteredSortedFavorites,
            eateryByPlace = eateries.associateBy { it.toPlace() },
            libraryByPlace = libraries.associateBy { it.toPlace() },
            gymByPlace = gyms.associateBy { it.toPlace() },
            printerByPlace = printers.associate { printer ->
                val place = printer.toPlace()
                place to printer.toPrinterCardUiState()
            }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EcosystemFavoritesUiState()
    )

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

        combine(homeQueryFlow, mergedHomeSearchResultsFlow) { query, mergedResults ->
            query to mergedResults
        }.onEach { (query, mergedResults) ->
            val currentState = _searchBarUiState.value
            if (currentState is SearchBarUIState.Query && currentState.queryText == query) {
                _searchBarUiState.value = currentState.copy(searched = mergedResults)
            }
        }.launchIn(viewModelScope)

        homeQueryFlow
            .debounce(300L)
            .filter { it.isNotBlank() }
            .onEach {
                routeRepository.makeSearch(it)
            }.launchIn(viewModelScope)

        _addSearchQuery.debounce(300L)
            .map { it.trim() }
            .distinctUntilChanged()
            .filter { it.isNotEmpty() }
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
     * Clear the query in the add favorites search bar
     */
    fun clearAddQuery() {
        onAddQueryChange("")
    }

    /**
     * Change the query in the add favorites search bar and update search results
     */
    fun onAddQueryChange(query: String) {
        _addSearchQuery.value = query
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
        _filterState.value = filterState
    }

    /**
     * Returns a distance string from a location to the current location if both exist, otherwise returns empty string
     */
    fun distanceStringIfCurrentLocationExists(latitude: Double?, longitude: Double?): String {
        val currentLocationSnapshot = currentLocation.value
        if (currentLocationSnapshot != null && latitude != null && longitude != null) {
            val distanceInMeters = calculateDistance(
                LatLng(
                    currentLocationSnapshot.latitude,
                    currentLocationSnapshot.longitude
                ), LatLng(latitude, longitude)
            ).toString()
            return " - ${distanceInMeters.fromMetersToFeet()}"
        }
        return ""
    }

    /**
     * Returns distance text with a loading placeholder when current location is not ready.
     */
    fun distanceTextOrPlaceholder(latitude: Double?, longitude: Double?): String {
        val distanceText = distanceStringIfCurrentLocationExists(latitude, longitude)
        return if (distanceText.isBlank()) " - Calculating Distance..." else distanceText
    }

    /**
     * Keeps only the first segment of a library address (text before the first comma).
     */
    fun sanitizeLibraryAddress(address: String): String {
        return address.substringBefore(",").trim()
    }

    /**
     * Maps raw printer fields to UI-ready fields for card rendering.
     */
    fun printerToCardUiState(printer: Printer): PrinterCardUiState = printer.toPrinterCardUiState()
}

/**
 * Derived favorites content for the ecosystem tabs, computed in ViewModel.
 */
data class EcosystemFavoritesUiState(
    val filteredSortedFavorites: List<Place> = emptyList(),
    val eateryByPlace: Map<Place, Eatery> = emptyMap(),
    val libraryByPlace: Map<Place, Library> = emptyMap(),
    val gymByPlace: Map<Place, UpliftGym> = emptyMap(),
    val printerByPlace: Map<Place, PrinterCardUiState> = emptyMap()
)

/**
 * UI-ready printer fields so composables don't parse backend strings.
 */
data class PrinterCardUiState(
    val title: String,
    val subtitle: String,
    val inColor: Boolean,
    val hasCopy: Boolean,
    val hasScan: Boolean,
    val alertMessage: String
)

private fun Printer.toPrinterCardUiState(): PrinterCardUiState {
    //Hard-coded way to handle closed for construction message, change when backend is updated
    val constructionAlert = "CLOSED FOR CONSTRUCTION"
    val constructionRegex = Regex("""\bCLOSED\s+FOR\s+CONSTRUCTION\b""", RegexOption.IGNORE_CASE)
    val hasConstructionAlert = constructionRegex.containsMatchIn(location)

    val rawTitle = location.substringBefore("*").trim()
    val title = rawTitle
        .replace(constructionRegex, "")
        .replace(Regex("""\s{2,}"""), " ")
        .trim(' ', '-', ',', ';', ':')

    val starAlertMessage = location.substringAfter("*", "").trim('*').trim()
    val alertMessage = if (hasConstructionAlert) constructionAlert else starAlertMessage

    return PrinterCardUiState(
        title = title,
        subtitle = description.substringAfter("-", description).trim().toTitleCaseWords(),
        inColor = description.contains("Color", ignoreCase = true),
        hasCopy = description.contains("Copy", ignoreCase = true),
        hasScan = description.contains("Scan", ignoreCase = true),
        alertMessage = alertMessage
    )
}

private fun String.toTitleCaseWords(): String {
    return split(Regex("""\s+"""))
        .filter { it.isNotBlank() }
        .joinToString(" ") { word ->
            word.lowercase(Locale.getDefault())
                .replaceFirstChar { char ->
                    if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString()
                }
        }
}

private fun Set<FavoritesFilterSheetState>.toAllowedPlaceTypes(): Set<PlaceType> = buildSet {
    if (FavoritesFilterSheetState.EATERIES in this@toAllowedPlaceTypes) add(PlaceType.EATERY)
    if (FavoritesFilterSheetState.LIBRARIES in this@toAllowedPlaceTypes) add(PlaceType.LIBRARY)
    if (FavoritesFilterSheetState.GYMS in this@toAllowedPlaceTypes) add(PlaceType.GYM)
    if (FavoritesFilterSheetState.PRINTERS in this@toAllowedPlaceTypes) add(PlaceType.PRINTER)
    if (FavoritesFilterSheetState.OTHER in this@toAllowedPlaceTypes) {
        add(PlaceType.APPLE_PLACE)
        add(PlaceType.BUS_STOP)
    }
}
