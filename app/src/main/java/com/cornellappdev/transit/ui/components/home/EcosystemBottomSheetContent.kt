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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.PlaceType
import com.cornellappdev.transit.models.ecosystem.DetailedEcosystemPlace
import com.cornellappdev.transit.models.ecosystem.StaticPlaces
import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.ui.theme.robotoFamily
import com.cornellappdev.transit.ui.viewmodels.FilterState


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
    navigateToDetails: (DetailedEcosystemPlace) -> Unit,
    addFavorite: (Place) -> Unit,
    removeFavorite: (Place) -> Unit
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
            navigateToDetails = navigateToDetails,
            addFavorite = addFavorite,
            removeFavorite = removeFavorite
        )
    }
}

@Composable
private fun BottomSheetFilteredContent(
    currentFilter: FilterState,
    staticPlaces: StaticPlaces,
    favorites: Set<Place>,
    navigateToPlace: (Place) -> Unit,
    navigateToDetails: (DetailedEcosystemPlace) -> Unit,
    addFavorite: (Place) -> Unit,
    removeFavorite: (Place) -> Unit
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
                eateryList(staticPlaces, navigateToPlace)
            }

            FilterState.LIBRARIES -> {
                libraryList(
                    staticPlaces,
                    navigateToPlace,
                    navigateToDetails,
                    favorites,
                    addFavorite,
                    removeFavorite
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
                        Place(
                            latitude = it.latitude,
                            longitude = it.longitude,
                            name = it.location,
                            detail = it.description,
                            type = PlaceType.APPLE_PLACE
                        )
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
    staticPlaces: StaticPlaces,
    navigateToPlace: (Place) -> Unit
) {
    when (staticPlaces.eateries) {
        is ApiResponse.Error -> {
        }

        is ApiResponse.Pending -> {
        }

        is ApiResponse.Success -> {
            items(staticPlaces.eateries.data) {
                BottomSheetLocationCard(
                    title = it.name,
                    subtitle1 = it.location.orEmpty()
                ) {
                    //TODO: Eatery
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
    addFavorite: (Place) -> Unit,
    removeFavorite: (Place) -> Unit
) {
    when (staticPlaces.libraries) {
        is ApiResponse.Error -> {
        }

        is ApiResponse.Pending -> {
        }

        is ApiResponse.Success -> {
            items(staticPlaces.libraries.data) {
                val place = Place(
                    latitude = it.latitude,
                    longitude = it.longitude,
                    name = it.location,
                    detail = it.address,
                    type = PlaceType.APPLE_PLACE
                )

                RoundedImagePlaceCard(
                    imageRes = R.drawable.olin_library,
                    title = it.location,
                    subtitle = it.address,
                    isFavorite = place in favorites,
                    onFavoriteClick = {
                        if (place !in favorites) {
                            addFavorite(place)
                        } else {
                            removeFavorite(place)
                        }
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
        addFavorite = {},
        navigateToPlace = {},
        navigateToDetails = {},
        removeFavorite = {}
    )
}