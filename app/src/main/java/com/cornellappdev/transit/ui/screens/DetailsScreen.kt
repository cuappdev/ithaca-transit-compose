package com.cornellappdev.transit.ui.screens

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cornellappdev.transit.models.MapState
import com.cornellappdev.transit.models.RouteOptionType
import com.cornellappdev.transit.models.RouteOptions
import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.ui.components.TransitPolyline
import com.cornellappdev.transit.ui.theme.DividerGray
import com.cornellappdev.transit.ui.theme.sfProDisplayFamily
import com.cornellappdev.transit.ui.viewmodels.HomeViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState

/**
 * Screen for showing a particular route
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun DetailsScreen(navController: NavHostController, homeViewModel: HomeViewModel) {

    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    //Map camera
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(homeViewModel.defaultIthaca, 12f)
    }

    //Collect flow of route through API
    val routeApiResponse = homeViewModel.lastRouteFlow.collectAsState().value

    //Map state
    val mapState = homeViewModel.mapState.collectAsState().value


    Column(modifier = Modifier.fillMaxSize()) {
        //TODO make an AppBarColors class w/ the right colors and correct icon
        TopAppBar(
            title = {
                Text(
                    text = "Route Details",
                    fontFamily = sfProDisplayFamily,
                    fontStyle = FontStyle.Normal
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                        contentDescription = ""
                    )
                }
            }

        )

        Divider(thickness = 1.dp, color = DividerGray)

        DrawableMap(mapState, routeApiResponse, cameraPositionState, permissionState)
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun DrawableMap(
    mapState: MapState,
    routeApiResponse: ApiResponse<RouteOptions>,
    cameraPositionState: CameraPositionState,
    permissionState: PermissionState
) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = permissionState.status.isGranted
        ),
        uiSettings = MapUiSettings(zoomControlsEnabled = false)
    ) {
        if (mapState.isShowing) {
            when (routeApiResponse) {
                is ApiResponse.Pending -> {

                }

                is ApiResponse.Error -> {
                    //TODO: Appropriate error
                }

                is ApiResponse.Success -> {
                    when (mapState.routeOptionType) {
                        RouteOptionType.None -> {

                        }

                        RouteOptionType.BoardingSoon -> {
                            routeApiResponse.data.boardingSoon.forEach { route ->
                                route.directions.forEach { direction ->
                                    TransitPolyline(
                                        points = direction.path
                                    )
                                }
                            }
                        }

                        RouteOptionType.FromStop -> {
                            routeApiResponse.data.fromStop.forEach { route ->
                                route.directions.forEach { direction ->
                                    TransitPolyline(
                                        points = direction.path
                                    )
                                }
                            }
                        }

                        RouteOptionType.Walking -> {
                            routeApiResponse.data.walking.forEach { route ->
                                route.directions.forEach { direction ->
                                    TransitPolyline(
                                        points = direction.path
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}