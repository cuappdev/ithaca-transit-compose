package com.cornellappdev.transit.ui.screens

import android.Manifest
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.RouteOptionType
import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.ui.components.TransitPolyline
import com.cornellappdev.transit.ui.theme.DividerGrey
import com.cornellappdev.transit.ui.theme.IconGrey
import com.cornellappdev.transit.ui.theme.MetadataGrey
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.sfProDisplayFamily
import com.cornellappdev.transit.ui.viewmodels.HomeViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

/**
 * Screen for showing a particular route
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun DetailsScreen(navController: NavHostController, homeViewModel : HomeViewModel) {

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

        Divider(thickness = 1.dp, color = DividerGrey)
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
}