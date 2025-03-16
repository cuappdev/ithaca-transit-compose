package com.cornellappdev.transit.ui.screens

import android.Manifest
import android.os.Build
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import com.cornellappdev.transit.ui.components.details.BusIcon
import com.cornellappdev.transit.ui.components.details.DirectionItem
import com.cornellappdev.transit.ui.components.details.StopItem
import com.cornellappdev.transit.ui.theme.DetailsHeaderGray
import com.cornellappdev.transit.ui.theme.DividerGray
import com.cornellappdev.transit.ui.theme.LateRed
import com.cornellappdev.transit.ui.theme.LiveGreen
import com.cornellappdev.transit.ui.theme.MetadataGray
import com.cornellappdev.transit.ui.theme.Style
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.robotoFamily
import com.cornellappdev.transit.ui.viewmodels.DirectionDetails
import com.cornellappdev.transit.ui.viewmodels.RouteViewModel
import com.cornellappdev.transit.util.TimeUtils
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import io.morfly.compose.bottomsheet.material3.BottomSheetScaffold
import io.morfly.compose.bottomsheet.material3.rememberBottomSheetScaffoldState
import io.morfly.compose.bottomsheet.material3.rememberBottomSheetState


private enum class SheetValue { Collapsed, PartiallyExpanded, Expanded }

/**
 * Screen for showing a particular route
 */
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun DetailsScreen(navController: NavHostController, routeViewModel: RouteViewModel) {

    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)


    //Map state
    val mapState = routeViewModel.mapState.collectAsState().value

    val mapDetails = routeViewModel.detailsState.collectAsState().value

    //Map camera
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            routeViewModel.defaultIthaca, 15f
        )
    }

    // Update camera to fit both start & end points
    LaunchedEffect(mapState.route?.startCoords, mapState.route?.directions?.last()?.endLocation) {
        val startCoords = mapState.route?.startCoords
        val endCoords = mapState.route?.directions?.last()?.endLocation

        if (startCoords != null && endCoords != null) {
            val bounds = routeViewModel.getLatLngBounds(startCoords, endCoords)
            val padding = 180
            cameraPositionState.animate(CameraUpdateFactory.newLatLngBounds(bounds, padding))
        }
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
            DetailsBottomSheet(
                mapDetails,
                mapState.route?.totalDuration ?: 0,
                mapState.route?.busDelayed ?: false
            )
        },
        content = {
            // Screen content
            DetailsMainScreen(
                mapState,
                cameraPositionState,
                permissionState,
                navController,
                routeViewModel
            )
        }
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun DrawableMap(
    mapState: MapState,
    cameraPositionState: CameraPositionState,
    permissionState: PermissionState,
    lineSize: Float,
    dotSize: Float
) {
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            isMyLocationEnabled = permissionState.status.isGranted
        ),
        uiSettings = MapUiSettings(zoomControlsEnabled = false)
    ) {
        if (mapState.isShowing && mapState.route?.directions != null) {
            mapState.route.directions.forEach { direction ->
                TransitPolyline(direction.path, direction.type, lineSize, dotSize)
            }
            Marker(
                state = MarkerState(
                    position = mapState.route.directions.last().endLocation
                )
            )
        }
    }
}

@Composable
private fun TransitPolyline(
    points: List<LatLng>,
    directionType: DirectionType,
    lineSize: Float,
    dotSize: Float
) {

    when (directionType) {
        DirectionType.WALK -> {
            Polyline(
                points = points,
                color = Color.Gray,
                width = lineSize,
                pattern = listOf(Dot(), Gap(10f)),
            )
        }

        DirectionType.DEPART -> {
            Polyline(
                points = points,
                color = TransitBlue,
                width = lineSize,
                pattern = listOf(Dash(1f))
            )
            Circle(
                center = points.first(),
                radius = dotSize.toDouble(),
                fillColor = Color.White,
                strokeColor = Color.Black,
                strokeWidth = 6f
            )
            Circle(
                center = points.last(),
                radius = dotSize.toDouble(),
                fillColor = Color.White,
                strokeColor = Color.Black,
                strokeWidth = 6f
            )
        }
    }
}

/**
 * Wrapper of Route Details bottom sheet, including header
 */
@Composable
private fun DetailsBottomSheet(
    directionDetails: List<DirectionDetails>,
    totalDuration: Int,
    isDelayed: Boolean
) {

    val busDirection = directionDetails.firstOrNull { dir ->
        dir.busNumber != ""
    }

    Column(
        modifier = Modifier
            .height(700.dp)
            .background(Color.White)
    ) {

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = DetailsHeaderGray)
                .height(100.dp)
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(0.25f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (busDirection != null) {
                    BusIcon(Integer.parseInt(busDirection.busNumber))
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
                                    color = if (isDelayed) LateRed else LiveGreen
                                )
                            ) {
                                append(busDirection.delayedStartTime ?: busDirection.startTime)
                            }
                            append(" from ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(busDirection.destination)
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                        color = Color.Black
                    )
                } else {
                    Text(
                        buildAnnotatedString {
                            append("Walk to ")
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(directionDetails.last().destination)
                            }
                        },
                        modifier = Modifier.padding(horizontal = 12.dp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false)),
                        color = Color.Black
                    )
                }
                Text(
                    text = "Trip Duration: ${totalDuration} minute" + if (totalDuration != 1) "s" else "",
                    style = Style.paragraph,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    color = MetadataGray
                )
            }

        }

        // Route details
        DetailsSheet(directionDetails)

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
private fun DetailsMainScreen(
    mapState: MapState,
    cameraPositionState: CameraPositionState,
    permissionState: PermissionState,
    navController: NavHostController,
    routeViewModel: RouteViewModel
) {
    Column(modifier = Modifier.fillMaxSize()) {
        //TODO make an AppBarColors class w/ the right colors and correct icon
        TopAppBar(
            title = {
                Text(
                    text = "Route Details",
                    fontFamily = robotoFamily,
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

        DrawableMap(
            mapState,
            cameraPositionState,
            permissionState,
            routeViewModel.getLineSize(cameraPositionState.position.zoom),
            routeViewModel.getDotSize(cameraPositionState.position.zoom)
        )

    }
}

/**
 * Main component of Route Details bottom sheet
 */
@Composable
fun DetailsSheet(directionDetails: List<DirectionDetails>) {

    val expandedStops = remember { mutableStateListOf(*Array(directionDetails.size) { false }) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .padding(top = 8.dp, start = 8.dp, end = 8.dp)
    ) {
        itemsIndexed(directionDetails) { index, details ->

            DirectionItem(
                time = if (index == directionDetails.lastIndex && details.directionType == DirectionType.WALK)
                    details.endTime else
                    details.startTime,
                movementDescription = details.movementDescription,
                destination = details.destination,
                directionType = details.directionType,
                drawSegmentAbove = index != 0,
                drawSegmentBelow = index != directionDetails.lastIndex || details.directionType == DirectionType.DEPART,
                isFinalDestination = index == directionDetails.lastIndex && details.directionType == DirectionType.WALK,
                busNumber = details.busNumber,
                numStops = details.numStops,
                modifier = Modifier.clickable {
                    expandedStops[index] = !expandedStops[index]
                },
                duration = details.duration,
                expandedStops = expandedStops[index],
                // If the previous direction was a bus that transfers onto this bus
                colorAbove = if (details.directionType == DirectionType.DEPART && details.busTransfer) TransitBlue else MetadataGray,
                colorBelow = if (details.directionType == DirectionType.DEPART) TransitBlue else MetadataGray,
                delayedTime = if (index == directionDetails.lastIndex && details.directionType == DirectionType.WALK)
                    details.delayedEndTime else
                    details.delayedStartTime,
            )

            if (expandedStops[index]) {
                // Drop first stop because it is already displayed before, and drop last stop because
                // it will be displayed as "Get off at"
                for (stop in details.stops.drop(1).dropLast(1)) {
                    StopItem(stop.name, stop.latitude.toDouble(), stop.long.toDouble())
                }
            }

            if (!(index < directionDetails.lastIndex && directionDetails[index + 1].busTransfer) && details.directionType == DirectionType.DEPART
            ) {
                DirectionItem(
                    time = details.endTime,
                    movementDescription = "Get off",
                    destination = details.stops.last().name,
                    directionType = details.directionType,
                    drawSegmentAbove = true,
                    drawSegmentBelow = index != directionDetails.lastIndex,
                    isFinalDestination = index == directionDetails.lastIndex,
                    busNumber = details.busNumber,
                    isLastStop = true,
                    colorAbove = TransitBlue,
                    delayedTime = details.delayedEndTime
                )
            }

        }
    }


}