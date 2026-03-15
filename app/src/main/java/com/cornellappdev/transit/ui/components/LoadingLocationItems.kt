package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        searchResult.data
                    ) {
                        MenuItem(
                            type = it.type,
                            label = it.name,
                            sublabel = it.subLabel,
                            onClick = { onClick(it) },
                            modifier = Modifier
                        )
                    }
                }
            }
        }
    }
}