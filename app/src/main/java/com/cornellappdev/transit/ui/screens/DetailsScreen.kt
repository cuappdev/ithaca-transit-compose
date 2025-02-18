package com.cornellappdev.transit.ui.screens

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.DirectionType
import com.cornellappdev.transit.models.MapState
import com.cornellappdev.transit.models.Route
import com.cornellappdev.transit.models.toTransport
import com.cornellappdev.transit.ui.components.SingleRoute
import com.cornellappdev.transit.ui.components.TransitPolyline
import com.cornellappdev.transit.ui.components.details.BusIcon
import com.cornellappdev.transit.ui.components.details.DetailsSheet
import com.cornellappdev.transit.ui.theme.DividerGray
import com.cornellappdev.transit.ui.theme.IconGray
import com.cornellappdev.transit.ui.theme.LiveGreen
import com.cornellappdev.transit.ui.theme.MetadataGray
import com.cornellappdev.transit.ui.theme.Style
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.sfProDisplayFamily
import com.cornellappdev.transit.ui.viewmodels.RouteViewModel
import com.cornellappdev.transit.util.TimeUtils
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
import io.morfly.compose.bottomsheet.material3.BottomSheetScaffold
import io.morfly.compose.bottomsheet.material3.rememberBottomSheetScaffoldState
import io.morfly.compose.bottomsheet.material3.rememberBottomSheetState


private enum class SheetValue { Collapsed, PartiallyExpanded, Expanded }

/**
 * Screen for showing a particular route
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun DetailsScreen(navController: NavHostController, routeViewModel: RouteViewModel) {

    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)


    //Map state
    val mapState = routeViewModel.mapState.collectAsState().value

    //Map camera
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            mapState.route?.startCoords ?: routeViewModel.defaultIthaca, 16.75f
        )
    }

    // Using advanced-bottomsheet-compose from https://github.com/Morfly/advanced-bottomsheet-compose
    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Collapsed,
        defineValues = {
            SheetValue.Collapsed at height(100.dp)
            // Bottom sheet offset is 50%, i.e. it takes 50% of the screen
            SheetValue.PartiallyExpanded at offset(percent = 50)
            // Wrap full height
            SheetValue.Expanded at offset(percent = 10)
        }
    )

    val scaffoldState = rememberBottomSheetScaffoldState(sheetState)

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetDragHandle = {},
        sheetContent = {
            // Bottom sheet content
            DetailsBottomSheet(mapState.route)
        },
        content = {
            // Screen content
            DetailsMainScreen(mapState, cameraPositionState, permissionState, navController)
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun DrawableMap(
    mapState: MapState,
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
            mapState.route?.directions?.forEach { direction ->
                TransitPolyline(
                    points = direction.path
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun DetailsBottomSheet(route: Route?) {

    if (route == null) {
        Text("No route selected")
        return
    }

    val busDirection = route.directions.firstOrNull { dir ->
        dir.routeId != null
    }

    Column(modifier = Modifier.height(700.dp)) {

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = DividerGray)
                .height(100.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(0.25f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (busDirection != null) {
                    BusIcon(Integer.parseInt(busDirection.routeId!!))
                } else {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.walk),
                        contentDescription = "Walking",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
            Column(
                modifier = Modifier.weight(0.75f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                if (busDirection != null) {
                    Text(
                        buildAnnotatedString {
                            append("Depart at ")
                            withStyle(
                                style = SpanStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = LiveGreen
                                )
                            ) {
                                append(TimeUtils.getHHMM(busDirection.startTime))
                            }
                            append(" from ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(busDirection.name)
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                } else {
                    Text(
                        buildAnnotatedString {
                            append("Walk to ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(route.endName)
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                }
                Text(
                    text = "Trip Duration: ${route.totalDuration} minute" + if (route.totalDuration != 1) "s" else "",
                    style = Style.paragraph,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

        }

        // Route details
        DetailsSheet(route)

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
private fun DetailsMainScreen(
    mapState: MapState,
    cameraPositionState: CameraPositionState,
    permissionState: PermissionState,
    navController: NavHostController
) {
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

        HorizontalDivider(thickness = 1.dp, color = DividerGray)

        DrawableMap(mapState, cameraPositionState, permissionState)

    }
}