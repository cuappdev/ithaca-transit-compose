package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.ui.viewmodels.LocationUIState
import com.google.android.gms.maps.model.LatLng

/**
 * Display for suggested searches (recents and favorites)
 */
@Composable
fun SearchSuggestions(
    favorites: Set<Place>,
    recents: List<Place>,
    onFavoriteAdd: () -> Unit,
    onRecentClear: () -> Unit,
    changeStartLocation: (LocationUIState) -> Unit,
    changeEndLocation: (LocationUIState) -> Unit,
    navController: NavController,
    onStopPressed: (Place) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .scrollable(rememberScrollState(), orientation = Orientation.Vertical)
    ) {
        SearchCategoryHeader(
            headerText = "Favorite Destinations",
            buttonText = "Add",
            onClick = onFavoriteAdd
        )
        favorites.take(minOf(5, favorites.size)).forEach {
            MenuItem(
                type = it.type,
                label = it.name,
                sublabel = it.subLabel,
                onClick = {
                    onStopPressed(it)
                    changeStartLocation(
                        LocationUIState.CurrentLocation
                    )
                    changeEndLocation(
                        LocationUIState.Place(
                            it.name,
                            LatLng(
                                it.latitude,
                                it.longitude
                            )
                        )
                    )
                    navController.navigate("route")
                }
            )
        }
        Spacer(Modifier.height(16.dp))
        SearchCategoryHeader(
            headerText = "Recent Destinations",
            buttonText = "Clear",
            onClick = onRecentClear
        )
        recents.forEach {
            MenuItem(
                type = it.type,
                label = it.name,
                sublabel = it.subLabel,
                onClick = {
                    onStopPressed(it)
                    changeStartLocation(
                        LocationUIState.CurrentLocation
                    )
                    changeEndLocation(
                        LocationUIState.Place(
                            it.name,
                            LatLng(
                                it.latitude,
                                it.longitude
                            )
                        )
                    )
                    navController.navigate("route")
                }
            )
        }
    }
}