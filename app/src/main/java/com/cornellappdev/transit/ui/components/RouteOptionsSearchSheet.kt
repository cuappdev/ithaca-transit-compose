package com.cornellappdev.transit.ui.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.sfProDisplayFamily
import com.cornellappdev.transit.ui.viewmodels.RouteViewModel
import com.google.android.gms.maps.model.LatLng


/**
 * Route options select sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteOptionsSearchSheet(
    routeViewModel: RouteViewModel,
    onCancelClicked: () -> Unit,
    onItemClicked: () -> Unit,
) {

    // Search bar flow
    val searchBarValue = routeViewModel.searchQuery.collectAsState().value

    // Search bar results
    val placeQueryResponse = routeViewModel.placeQueryFlow.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxHeight(0.92f)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Route Options",
                        fontFamily = sfProDisplayFamily,
                        fontStyle = FontStyle.Normal,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    TextButton(
                        onClick = onCancelClicked,
                        content = {
                            Text(
                                text = "Cancel",
                                fontFamily = sfProDisplayFamily,
                                fontStyle = FontStyle.Normal,
                                textAlign = TextAlign.Center,
                                color = SecondaryText
                            )
                        },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }

            SearchTextField(
                value = searchBarValue,
                setValue = { s -> routeViewModel.onQueryChange(s) },
                placeholderText = "Search",
                singleLine = true,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 7.dp, bottom = 11.dp),
                height = 50.dp,
                prefix = {
                    Icon(
                        Icons.Outlined.Search,
                        "Search",
                        modifier = Modifier.clickable { routeViewModel.onQueryChange("") })
                },
                suffix = {
                    Icon(
                        Icons.Outlined.Clear,
                        "Clear",
                        modifier = Modifier.clickable { routeViewModel.onQueryChange("") })
                }
            )

            LazyColumn {
                when (placeQueryResponse) {
                    is ApiResponse.Error -> {

                    }

                    is ApiResponse.Pending -> {

                    }

                    is ApiResponse.Success -> {

                        items(placeQueryResponse.data) {
                            MenuItem(
                                type = it.type,
                                label = it.name,
                                sublabel = it.subLabel,
                                onClick = {
                                    routeViewModel.getRoute(
                                        start = if(routeViewModel.startPl.value.second == null) LatLng(0.0, 0.0) else routeViewModel.startPl.value.second!!,
                                        end = if(routeViewModel.destPl.value.second == null) LatLng(0.0, 0.0) else routeViewModel.destPl.value.second!!,
                                        arriveBy = false,
                                        destinationName = routeViewModel.destPl.value.first,
                                        originName = routeViewModel.startPl.value.first,
                                        time = (System.currentTimeMillis() / 1000).toDouble() //TODO: Implement arriveBy
                                    )
                                    onItemClicked()
                                })
                        }
                    }
                }

            }
        }
    }
}