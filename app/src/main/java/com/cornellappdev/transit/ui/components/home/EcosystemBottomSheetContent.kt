package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.PlaceType
import com.cornellappdev.transit.models.ecosystem.DayOperatingHours
import com.cornellappdev.transit.models.ecosystem.DetailedEcosystemPlace
import com.cornellappdev.transit.models.ecosystem.Eatery
import com.cornellappdev.transit.models.ecosystem.Library
import com.cornellappdev.transit.models.ecosystem.Printer
import com.cornellappdev.transit.models.ecosystem.StaticPlaces
import com.cornellappdev.transit.models.ecosystem.UpliftGym
import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.ui.theme.FavoritesDividerGray
import com.cornellappdev.transit.ui.theme.robotoFamily
import com.cornellappdev.transit.ui.viewmodels.EcosystemFavoritesUiState
import com.cornellappdev.transit.ui.viewmodels.FavoritesFilterSheetState
import com.cornellappdev.transit.ui.viewmodels.FilterState
import com.cornellappdev.transit.ui.viewmodels.PrinterCardUiState
import com.cornellappdev.transit.util.ecosystem.toPlace
import kotlin.collections.isNotEmpty


/**
 * Contents of BottomSheet in HomeScreen with ecosystem integration
 * @param filters The list of filters to display
 * @param activeFilter Which filter is currently selected
 * @param onFilterClick Function called on for filter changes
 * @param staticPlaces Collection of all places to populate filters with
 * @param navigateToPlace Function called to navigate to route options
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EcosystemBottomSheetContent(
    filters: List<FilterState>,
    activeFilter: FilterState,
    onFilterClick: (FilterState) -> Unit,
    staticPlaces: StaticPlaces,
    favorites: Set<Place>,
    favoritesUiState: EcosystemFavoritesUiState,
    modifier: Modifier = Modifier,
    navigateToPlace: (Place) -> Unit,
    onDetailsClick: (DetailedEcosystemPlace) -> Unit,
    onFavoriteStarClick: (Place) -> Unit,
    showFilterSheet: Boolean,
    onFilterSheetShow: () -> Unit,
    onAddFavoritesClick: () -> Unit,
    selectedFilters: Set<FavoritesFilterSheetState>,
    appliedFilters: Set<FavoritesFilterSheetState>,
    favoritesFilterList: List<FavoritesFilterSheetState>,
    onCancelFilters: () -> Unit,
    onApplyFilters: () -> Unit,
    onFilterToggle: (FavoritesFilterSheetState) -> Unit,
    onRemoveAppliedFilter: (FavoritesFilterSheetState) -> Unit,
    operatingHoursToString: (List<DayOperatingHours>) -> AnnotatedString,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = 20.dp,
                    start = 20.dp,
                    end = 20.dp,
                    top = 2.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Near You",
                fontWeight = FontWeight(600),
                fontSize = 20.sp,
                fontFamily = robotoFamily
            )
        }

        LazyRow(modifier = Modifier.padding(bottom = 12.dp)) {
            items(filters) {
                BottomSheetFilterItem(
                    imageResId = it.iconId,
                    label = it.label,
                    isActive = activeFilter == it
                ) {
                    onFilterClick(it)
                }
            }
        }

        BottomSheetFilteredContent(
            currentFilter = activeFilter,
            staticPlaces = staticPlaces,
            favorites = favorites,
            favoritesUiState = favoritesUiState,
            navigateToPlace = navigateToPlace,
            onDetailsClick = onDetailsClick,
            onFavoriteStarClick = onFavoriteStarClick,
            onFilterButtonClick = onFilterSheetShow,
            onAddFavoritesClick = onAddFavoritesClick,
            appliedFilters = appliedFilters,
            onRemoveAppliedFilter = onRemoveAppliedFilter,
            operatingHoursToString = operatingHoursToString
        )
    }

    if (showFilterSheet) {
        ModalBottomSheet(
            onDismissRequest = onCancelFilters,
            dragHandle = null
        ) {
            FavoritesFilterBottomSheet(
                onCancelClicked = onCancelFilters,
                onApplyClicked = onApplyFilters,
                filters = favoritesFilterList,
                selectedFilters = selectedFilters,
                onFilterToggle = onFilterToggle
            )
        }
    }
}

@Composable
private fun BottomSheetFilteredContent(
    currentFilter: FilterState,
    staticPlaces: StaticPlaces,
    favorites: Set<Place>,
    favoritesUiState: EcosystemFavoritesUiState,
    navigateToPlace: (Place) -> Unit,
    onDetailsClick: (DetailedEcosystemPlace) -> Unit,
    onFavoriteStarClick: (Place) -> Unit,
    onAddFavoritesClick: () -> Unit,
    onFilterButtonClick: () -> Unit,
    appliedFilters: Set<FavoritesFilterSheetState>,
    onRemoveAppliedFilter: (FavoritesFilterSheetState) -> Unit,
    operatingHoursToString: (List<DayOperatingHours>) -> AnnotatedString
) {
    Column {
        if (currentFilter == FilterState.FAVORITES) {
            Column(modifier = Modifier.padding(horizontal = 12.dp)) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = FavoritesDividerGray
                )
                FilterRow(
                    selectedFilters = appliedFilters,
                    onFilterClick = onFilterButtonClick,
                    onRemoveFilter = onRemoveAppliedFilter
                )
                if (appliedFilters.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
        val isFilterBarHidden = currentFilter == FilterState.FAVORITES && appliedFilters.isEmpty()
        key(currentFilter, appliedFilters) {
            LazyColumn(
                contentPadding = PaddingValues(
                    start = 12.dp,
                    end = 12.dp,
                    top = if (isFilterBarHidden) 0.dp else 8.dp,
                    bottom = 120.dp // Makes bottom content visible with padding at the end
                ),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                when (currentFilter) {
                    FilterState.FAVORITES -> {
                        favoriteList(
                            favorites = favorites,
                            filteredFavorites = favoritesUiState.filteredSortedFavorites,
                            eateryByPlace = favoritesUiState.eateryByPlace,
                            libraryByPlace = favoritesUiState.libraryByPlace,
                            gymByPlace = favoritesUiState.gymByPlace,
                            printerByPlace = favoritesUiState.printerByPlace,
                            navigateToPlace = navigateToPlace,
                            onAddFavoritesClick = onAddFavoritesClick,
                            onFavoriteStarClick = onFavoriteStarClick,
                            onDetailsClick = onDetailsClick,
                            operatingHoursToString = operatingHoursToString
                        )
                    }

                    FilterState.PRINTERS -> {
                        printerList(
                            staticPlaces = staticPlaces,
                            navigateToPlace = navigateToPlace,
                            favorites = favorites,
                            onFavoriteStarClick = onFavoriteStarClick
                        )
                    }

                    FilterState.GYMS -> {
                        gymList(
                            staticPlaces = staticPlaces,
                            navigateToPlace = navigateToPlace,
                            favorites = favorites,
                            onFavoriteStarClick = onFavoriteStarClick
                        )
                    }

                    FilterState.EATERIES -> {
                        eateryList(
                            eateriesApiResponse = staticPlaces.eateries,
                            onDetailsClick = onDetailsClick,
                            favorites = favorites,
                            onFavoriteStarClick = onFavoriteStarClick,
                            operatingHoursToString = operatingHoursToString
                        )
                    }

                    FilterState.LIBRARIES -> {
                        libraryList(
                            staticPlaces,
                            navigateToPlace,
                            onDetailsClick,
                            favorites,
                            onFavoriteStarClick,
                        )
                    }
                }
            }
        }
    }
}

/**
 * LazyList scoped enumeration of favorites for bottom sheet
 */
private fun LazyListScope.favoriteList(
    favorites: Set<Place>,
    filteredFavorites: List<Place>,
    eateryByPlace: Map<Place, Eatery>,
    libraryByPlace: Map<Place, Library>,
    gymByPlace: Map<Place, UpliftGym>,
    printerByPlace: Map<Place, PrinterCardUiState>,
    navigateToPlace: (Place) -> Unit,
    onAddFavoritesClick: () -> Unit,
    onFavoriteStarClick: (Place) -> Unit,
    onDetailsClick: (DetailedEcosystemPlace) -> Unit,
    operatingHoursToString: (List<DayOperatingHours>) -> AnnotatedString
) {
    item {
        Spacer(modifier = Modifier.height(8.dp))
        AddFavoritesButton(onAddFavoritesClick = onAddFavoritesClick)
    }

    items(
        items = filteredFavorites,
        key = { place -> "${place.type}:${place.name}:${place.latitude}:${place.longitude}" }
    ) { place ->
        when (place.type) {
            PlaceType.EATERY -> {
                val matchingEatery = eateryByPlace[place]
                if (matchingEatery != null) {
                    RoundedImagePlaceCard(
                        title = matchingEatery.name,
                        subtitle = matchingEatery.location ?: "",
                        isFavorite = true,
                        onFavoriteClick = { onFavoriteStarClick(place) },
                        leftAnnotatedString = operatingHoursToString(
                            matchingEatery.formatOperatingHours()
                        )
                    ) {
                        onDetailsClick(matchingEatery)
                    }
                } else {
                    StandardCard(
                        place = place,
                        onFavoriteStarClick = onFavoriteStarClick,
                        navigateToPlace = navigateToPlace
                    )
                }
            }

            PlaceType.LIBRARY -> {
                val matchingLibrary = libraryByPlace[place]
                if (matchingLibrary != null) {
                    RoundedImagePlaceCard(
                        title = matchingLibrary.location,
                        subtitle = matchingLibrary.address,
                        isFavorite = true,
                        onFavoriteClick = { onFavoriteStarClick(place) }
                    ) {
                        onDetailsClick(matchingLibrary)
                    }
                } else {
                    StandardCard(
                        place = place,
                        onFavoriteStarClick = onFavoriteStarClick,
                        navigateToPlace = navigateToPlace
                    )
                }
            }

            PlaceType.GYM -> {
                val matchingGym = gymByPlace[place]
                if (matchingGym != null) {
                    BottomSheetLocationCard(
                        title = matchingGym.name,
                        subtitle1 = matchingGym.id,
                        isFavorite = true,
                        onFavoriteClick = { onFavoriteStarClick(place) }
                    ) {
                        onDetailsClick(matchingGym)
                    }
                } else {
                    StandardCard(
                        place = place,
                        onFavoriteStarClick = onFavoriteStarClick,
                        navigateToPlace = navigateToPlace
                    )
                }
            }

            PlaceType.PRINTER -> {
                val matchingPrinter = printerByPlace[place]
                if (matchingPrinter != null) {
                    PrinterCard(
                        title = matchingPrinter.title,
                        subtitle = matchingPrinter.subtitle,
                        inColor = matchingPrinter.inColor,
                        hasCopy = matchingPrinter.hasCopy,
                        hasScan = matchingPrinter.hasScan,
                        alertMessage = matchingPrinter.alertMessage,
                        isFavorite = place in favorites,
                        onFavoriteClick = {
                            onFavoriteStarClick(place)
                        }
                    ) {
                        navigateToPlace(place)
                    }
                } else {
                    StandardCard(
                        place = place,
                        onFavoriteStarClick = onFavoriteStarClick,
                        navigateToPlace = navigateToPlace
                    )
                }
            }

            PlaceType.BUS_STOP, PlaceType.APPLE_PLACE -> {
                StandardCard(
                    place = place,
                    onFavoriteStarClick = onFavoriteStarClick,
                    navigateToPlace = navigateToPlace
                )
            }
        }
    }
}

/**
 * LazyList scoped enumeration of gyms for bottom sheet
 */
private fun LazyListScope.gymList(
    staticPlaces: StaticPlaces,
    navigateToPlace: (Place) -> Unit,
    favorites: Set<Place>,
    onFavoriteStarClick: (Place) -> Unit
) {
    when (staticPlaces.gyms) {
        is ApiResponse.Error -> {
        }

        is ApiResponse.Pending -> {
        }

        is ApiResponse.Success -> {
            items(staticPlaces.gyms.data) { gym ->
                val place = gym.toPlace()
                BottomSheetLocationCard(
                    title = gym.name,
                    subtitle1 = gym.id,
                    isFavorite = place in favorites,
                    onFavoriteClick = { onFavoriteStarClick(place) }
                ) {
                    navigateToPlace(place)
                }
            }
        }
    }
}

/**
 * LazyList scoped enumeration of printers for bottom sheet
 */
private fun LazyListScope.printerList(
    staticPlaces: StaticPlaces,
    navigateToPlace: (Place) -> Unit,
    favorites: Set<Place>,
    onFavoriteStarClick: (Place) -> Unit
) {
    when (staticPlaces.printers) {
        is ApiResponse.Error -> {
        }

        is ApiResponse.Pending -> {
        }

        is ApiResponse.Success -> {
            items(staticPlaces.printers.data) {
                val place = it.toPlace()
                val alert = if (it.location.contains("*")) {
                    it.location.substringAfter("*").trim('*').trim()
                } else {
                    ""
                }

                PrinterCard(
                    title = it.location.substringBefore("*").trim(),
                    subtitle = it.description.substringAfter("-").trim(),
                    inColor = it.description.contains("Color", ignoreCase = true),
                    hasCopy = it.description.contains("Copy", ignoreCase = true),
                    hasScan = it.description.contains("Scan", ignoreCase = true),
                    alertMessage = alert,
                    isFavorite = place in favorites,
                    onFavoriteClick = {
                        onFavoriteStarClick(place)
                    }
                ) {
                    navigateToPlace(
                        place
                    )
                }
            }
        }
    }
}

/**
 * LazyList scoped enumeration of eateries for bottom sheet
 */
private fun LazyListScope.eateryList(
    eateriesApiResponse: ApiResponse<List<Eatery>>,
    onDetailsClick: (DetailedEcosystemPlace) -> Unit,
    favorites: Set<Place>,
    onFavoriteStarClick: (Place) -> Unit,
    operatingHoursToString: (List<DayOperatingHours>) -> AnnotatedString
) {
    when (eateriesApiResponse) {
        is ApiResponse.Error -> {
        }

        is ApiResponse.Pending -> {
            item {
                CenteredSpinningIndicator()
            }
        }

        is ApiResponse.Success -> {
            items(eateriesApiResponse.data) {
                RoundedImagePlaceCard(
                    imageUrl = it.imageUrl,
                    title = it.name,
                    subtitle = it.location ?: "",
                    isFavorite = it.toPlace() in favorites,
                    onFavoriteClick = {
                        onFavoriteStarClick(it.toPlace())
                    },
                    placeholderRes = R.drawable.olin_library,
                    leftAnnotatedString = operatingHoursToString(
                        it.formatOperatingHours()
                    )
                ) {
                    onDetailsClick(it)
                }
            }
        }
    }
}

/**
 * LazyList scoped enumeration of libraries for bottom sheet
 */
private fun LazyListScope.libraryList(
    staticPlaces: StaticPlaces,
    navigateToPlace: (Place) -> Unit,
    navigateToDetails: (DetailedEcosystemPlace) -> Unit,
    favorites: Set<Place>,
    onFavoriteStarClick: (Place) -> Unit
) {
    when (staticPlaces.libraries) {
        is ApiResponse.Error -> {
        }

        is ApiResponse.Pending -> {
        }

        is ApiResponse.Success -> {
            items(staticPlaces.libraries.data) {
                RoundedImagePlaceCard(
                    placeholderRes = R.drawable.olin_library,
                    title = it.location,
                    subtitle = it.address,
                    isFavorite = it.toPlace() in favorites,
                    onFavoriteClick = {
                        onFavoriteStarClick(it.toPlace())
                    }
                ) {
                    navigateToDetails(it)
                }
            }
        }
    }
}

@Composable
private fun StandardCard(
    place: Place,
    onFavoriteStarClick: (Place) -> Unit,
    navigateToPlace: (Place) -> Unit
) {
    BottomSheetLocationCard(
        title = place.name,
        subtitle1 = place.subLabel,
        isFavorite = true,
        onFavoriteClick = { onFavoriteStarClick(place) }
    ) {
        navigateToPlace(place)
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewEcosystemBottomSheet() {
    EcosystemBottomSheetContent(
        filters = listOf(
            FilterState.FAVORITES,
            FilterState.GYMS,
            FilterState.EATERIES,
            FilterState.LIBRARIES,
            FilterState.PRINTERS
        ),
        activeFilter = FilterState.PRINTERS,
        onFilterClick = {},
        staticPlaces = StaticPlaces(
            ApiResponse.Pending,
            ApiResponse.Pending,
            ApiResponse.Pending,
            ApiResponse.Pending
        ),
        favorites = emptySet(),
        favoritesUiState = EcosystemFavoritesUiState(),
        modifier = Modifier,
        navigateToPlace = {},
        onDetailsClick = {},
        onFavoriteStarClick = {},
        onAddFavoritesClick = {},
        showFilterSheet = false,
        onFilterSheetShow = {},
        selectedFilters = emptySet(),
        appliedFilters = emptySet(),
        favoritesFilterList = listOf(
            FavoritesFilterSheetState.GYMS,
            FavoritesFilterSheetState.EATERIES,
            FavoritesFilterSheetState.LIBRARIES,
            FavoritesFilterSheetState.PRINTERS,
            FavoritesFilterSheetState.OTHER
        ),
        onCancelFilters = {},
        onApplyFilters = {},
        onFilterToggle = {},
        onRemoveAppliedFilter = {},
        operatingHoursToString = { _ -> AnnotatedString("") }
    )
}

@Preview(showBackground = true, name = "Favorites with Applied Filters")
@Composable
private fun PreviewBottomSheetFilteredContentFavorites() {
    val mockEatery = Eatery(
        id = 1,
        name = "Trillium",
        menuSummary = "Coffee, pastries, sandwiches",
        imageUrl = null,
        location = "Kennedy Hall",
        campusArea = "Central Campus",
        onlineOrderUrl = null,
        latitude = 42.4488,
        longitude = -76.4813,
        paymentAcceptsMealSwipes = true,
        paymentAcceptsBrbs = true,
        paymentAcceptsCash = true,
        events = null
    )

    val mockLibrary = Library(
        id = 1,
        location = "Olin Library",
        address = "161 Ho Plaza",
        latitude = 42.4534,
        longitude = -76.4735
    )

    val mockGym = UpliftGym(
        name = "Noyes Community Recreation Center",
        id = "noyes-rec",
        facilityId = "1",
        hours = listOf(null, null, null, null, null, null, null),
        imageUrl = null,
        upliftCapacity = null,
        latitude = 42.4480,
        longitude = -76.4840
    )

    val mockPrinter = Printer(
        id = 1,
        location = "Mann Library",
        description = "1st Floor, near entrance",
        latitude = 42.4479,
        longitude = -76.4764
    )

    BottomSheetFilteredContent(
        currentFilter = FilterState.FAVORITES,
        staticPlaces = StaticPlaces(
            printers = ApiResponse.Success(listOf(mockPrinter)),
            libraries = ApiResponse.Success(listOf(mockLibrary)),
            eateries = ApiResponse.Success(listOf(mockEatery)),
            gyms = ApiResponse.Success(listOf(mockGym))
        ),
        favorites = setOf(
            Place(
                latitude = 42.4488,
                longitude = -76.4813,
                name = "Trillium",
                detail = "Kennedy Hall",
                type = PlaceType.EATERY
            ),
            Place(
                latitude = 42.4534,
                longitude = -76.4735,
                name = "Olin Library",
                detail = "161 Ho Plaza",
                type = PlaceType.LIBRARY
            ),
            Place(
                latitude = 42.4480,
                longitude = -76.4840,
                name = "Noyes Community Recreation Center",
                detail = "North Campus",
                type = PlaceType.GYM
            ),
            Place(
                latitude = 42.4479,
                longitude = -76.4764,
                name = "Mann Library",
                detail = "1st Floor, near entrance",
                type = PlaceType.PRINTER
            ),
            Place(
                latitude = 42.4440,
                longitude = -76.4825,
                name = "Seneca St & Fall Creek Dr",
                detail = "Bus Stop",
                type = PlaceType.BUS_STOP
            )
        ),
        favoritesUiState = EcosystemFavoritesUiState(
            filteredSortedFavorites = listOf(
                Place(
                    latitude = 42.4488,
                    longitude = -76.4813,
                    name = "Trillium",
                    detail = "Kennedy Hall",
                    type = PlaceType.EATERY
                ),
                Place(
                    latitude = 42.4534,
                    longitude = -76.4735,
                    name = "Olin Library",
                    detail = "161 Ho Plaza",
                    type = PlaceType.LIBRARY
                ),
                Place(
                    latitude = 42.4480,
                    longitude = -76.4840,
                    name = "Noyes Community Recreation Center",
                    detail = "North Campus",
                    type = PlaceType.GYM
                ),
                Place(
                    latitude = 42.4479,
                    longitude = -76.4764,
                    name = "Mann Library",
                    detail = "1st Floor, near entrance",
                    type = PlaceType.PRINTER
                ),
                Place(
                    latitude = 42.4440,
                    longitude = -76.4825,
                    name = "Seneca St & Fall Creek Dr",
                    detail = "Bus Stop",
                    type = PlaceType.BUS_STOP
                )
            ),
            eateryByPlace = listOf(mockEatery).associateBy { it.toPlace() },
            libraryByPlace = listOf(mockLibrary).associateBy { it.toPlace() },
            gymByPlace = listOf(mockGym).associateBy { it.toPlace() },
            printerByPlace = mapOf(
                mockPrinter.toPlace() to PrinterCardUiState(
                    title = "Mann Library",
                    subtitle = "near entrance",
                    inColor = false,
                    hasCopy = false,
                    hasScan = false,
                    alertMessage = ""
                )
            )
        ),
        navigateToPlace = {},
        onDetailsClick = {},
        onFavoriteStarClick = {},
        onAddFavoritesClick = {},
        onFilterButtonClick = {},
        appliedFilters = setOf(
            FavoritesFilterSheetState.EATERIES,
            FavoritesFilterSheetState.LIBRARIES,
            FavoritesFilterSheetState.GYMS,
            FavoritesFilterSheetState.PRINTERS,
            FavoritesFilterSheetState.OTHER
        ),
        onRemoveAppliedFilter = {},
        operatingHoursToString = { _ -> AnnotatedString("Open • 10am - 4pm") }
    )
}



