package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.PlaceType

/**
 * Display for suggested searches (recents and favorites)
 */
@Composable
fun SearchSuggestions(
    favorites: List<Place>,
    recents: List<Place>,
    onFavoriteAdd: () -> Unit,
    onRecentClear: () -> Unit
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
        favorites.forEach {
            MenuItem(
                Icons.Filled.Place,
                label = it.name,
                sublabel = if (it.type == PlaceType.BUS_STOP) "BusStop" else it.detail.toString(),
                onClick = {
                })
        }
        Spacer(Modifier.height(16.dp))
        SearchCategoryHeader(
            headerText = "Recent Destinations",
            buttonText = "Clear",
            onClick = onRecentClear
        )
        recents.forEach {
            MenuItem(
                Icons.Filled.Place,
                label = it.name,
                sublabel = if (it.type == PlaceType.BUS_STOP) "Bus Stop" else it.detail.toString(),
                onClick = {
                })
        }

    }
}