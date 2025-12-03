package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.ecosystem.DayOperatingHours
import com.cornellappdev.transit.models.ecosystem.DetailedEcosystemPlace
import com.cornellappdev.transit.models.ecosystem.Eatery
import com.cornellappdev.transit.models.ecosystem.StaticPlaces
import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.ui.theme.robotoFamily
import com.cornellappdev.transit.ui.viewmodels.FilterState
import com.cornellappdev.transit.ui.viewmodels.HomeViewModel
import com.cornellappdev.transit.util.ecosystem.toPlace


/**
 * Contents of BottomSheet in HomeScreen with ecosystem integration
 * @param filters The list of filters to display
 * @param activeFilter Which filter is currently selected
 * @param onFilterClick Function called on for filter changes
 * @param staticPlaces Collection of all places to populate filters with
 * @param navigateToPlace Function called to navigate to route options
 */
@Composable
fun EcosystemBottomSheetContent(
    filters: List<FilterState>,
    activeFilter: FilterState,
    onFilterClick: (FilterState) -> Unit,
    staticPlaces: StaticPlaces,
    favorites: Set<Place>,
    modifier: Modifier = Modifier,
    navigateToPlace: (Place) -> Unit,
    onDetailsClick: (DetailedEcosystemPlace) -> Unit,
    onFavoriteStarClick: (Place) -> Unit
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

        LazyRow {
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
            navigateToPlace = navigateToPlace,
            onDetailsClick = onDetailsClick,
            onFavoriteStarClick = onFavoriteStarClick
        )
    }
}

@Composable
private fun BottomSheetFilteredContent(
    homeViewModel: HomeViewModel = hiltViewModel(),
    currentFilter: FilterState,
    staticPlaces: StaticPlaces,
    favorites: Set<Place>,
    navigateToPlace: (Place) -> Unit,
    onDetailsClick: (DetailedEcosystemPlace) -> Unit,
    onFavoriteStarClick: (Place) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 90.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        when (currentFilter) {
            FilterState.FAVORITES -> {
                favoriteList(favorites, navigateToPlace)
            }

            FilterState.PRINTERS -> {
                printerList(staticPlaces, navigateToPlace)
            }

            FilterState.GYMS -> {
                gymList(staticPlaces, navigateToPlace)
            }

            FilterState.EATERIES -> {
                eateryList(
                    eateriesApiResponse = staticPlaces.eateries,
                    onDetailsClick = onDetailsClick,
                    favorites = favorites,
                    onFavoriteStarClick = onFavoriteStarClick,
                    operatingHoursToString = homeViewModel::isOpenAnnotatedStringFromOperatingHours
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

/**
 * LazyList scoped enumeration of favorites for bottom sheet
 */
private fun LazyListScope.favoriteList(
    favorites: Set<Place>,
    navigateToPlace: (Place) -> Unit
) {
    items(favorites.toList()) {
        BottomSheetLocationCard(
            title = it.name,
            subtitle1 = it.subLabel
        ) {
            //TODO: Eatery
        }
    }
}

/**
 * LazyList scoped enumeration of gyms for bottom sheet
 */
private fun LazyListScope.gymList(
    staticPlaces: StaticPlaces,
    navigateToPlace: (Place) -> Unit
) {
    when (staticPlaces.gyms) {
        is ApiResponse.Error -> {
        }

        is ApiResponse.Pending -> {
        }

        is ApiResponse.Success -> {
            items(staticPlaces.gyms.data) {
                BottomSheetLocationCard(
                    title = it.name,
                    subtitle1 = it.id
                ) {
                    //TODO: Eatery
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
    navigateToPlace: (Place) -> Unit
) {
    when (staticPlaces.printers) {
        is ApiResponse.Error -> {
        }

        is ApiResponse.Pending -> {
        }

        is ApiResponse.Success -> {
            items(staticPlaces.printers.data) {
                BottomSheetLocationCard(
                    title = it.location,
                    subtitle1 = it.description
                ) {
                    navigateToPlace(
                        it.toPlace()
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

@Preview
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
        modifier = Modifier,
        navigateToPlace = {},
        onDetailsClick = {},
        onFavoriteStarClick = {}
    )
}