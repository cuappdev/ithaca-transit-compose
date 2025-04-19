package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.PlaceType
import com.cornellappdev.transit.models.StaticPlaces
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
    modifier: Modifier = Modifier,
    navigateToPlace: (Place) -> Unit
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
                    image = painterResource(it.iconId),
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
            navigateToPlace = navigateToPlace
        )
    }
}

@Composable
private fun BottomSheetFilteredContent(
    currentFilter: FilterState,
    staticPlaces: StaticPlaces,
    navigateToPlace: (Place) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(bottom = 90.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        when (currentFilter) {
            FilterState.FAVORITES -> {
                //TODO
            }

            FilterState.PRINTERS -> {
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

            FilterState.GYMS -> {
                //TODO
            }

            FilterState.EATERIES -> {
                //TODO
            }

            FilterState.LIBRARIES -> {
                when (staticPlaces.libraries) {
                    is ApiResponse.Error -> {
                    }

                    is ApiResponse.Pending -> {
                    }

                    is ApiResponse.Success -> {
                        items(staticPlaces.libraries.data) {
                            BottomSheetLocationCard(
                                title = it.location,
                                subtitle1 = it.address
                            ) {
                                //TODO: Navigate to library specific card
                            }
                        }
                    }
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
        staticPlaces = StaticPlaces(ApiResponse.Pending, ApiResponse.Pending),
        modifier = Modifier
    ) { }
}