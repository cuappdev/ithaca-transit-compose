package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.networking.ApiResponse

/**
 * Composable that enumerates all potential outcomes of a search for places API call [searchResult].
 * For outcomes with one or more place results, [onClick] is called when clicking on these places.
 */
@Composable
fun LoadingLocationItems(searchResult: ApiResponse<List<Place>>, onClick: (Place) -> Unit) {
    when (searchResult) {
        is ApiResponse.Error -> {
            LocationNotFound()
        }

        is ApiResponse.Pending -> {
            ProgressCircle()
        }

        is ApiResponse.Success -> {
            if (searchResult.data.isEmpty()) {
                LocationNotFound()
            } else {
                LazyColumn {
                    items(
                        searchResult.data
                    ) {
                        MenuItem(
                            type = it.type,
                            label = it.name,
                            sublabel = it.subLabel,
                            onClick = { onClick(it) }
                        )
                    }
                }
            }
        }
    }
}