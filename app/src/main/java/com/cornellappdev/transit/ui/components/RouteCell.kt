package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.BusLateness
import com.cornellappdev.transit.models.Direction
import com.cornellappdev.transit.models.DirectionType
import com.cornellappdev.transit.models.Transport
import com.cornellappdev.transit.ui.theme.IconGray
import com.cornellappdev.transit.ui.theme.MetadataGray
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.helveticaFamily
import com.google.android.gms.maps.model.LatLng
import java.util.Locale

/**
 * Composable function to display a route cell with transport details.
 *
 * @param transport The transport data to display in the route cell.
 */
@Composable
fun RouteCell(transport: Transport) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        RouteCellHeader(transport = transport)

        if (!transport.walkOnly) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = transport.lateness.text(),
                    fontFamily = helveticaFamily,
                    color = transport.lateness.color(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 16.sp,
                    style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.live_glyph__late_),
                    contentDescription = "Lateness",
                    tint = transport.lateness.color()
                )
            }
        }

        ConstraintLayout {
            val directionRefs = transport.directionList.map { createRef() }

            for ((index, direction) in transport.directionList.withIndex()) {
                Box(
                    modifier = Modifier.constrainAs(directionRefs[index]) {
                        top.linkTo(if (index == 0) parent.top else directionRefs[index - 1].bottom)
                        start.linkTo(parent.start)
                        bottom.linkTo(if (index == transport.directionList.lastIndex) parent.bottom else directionRefs[index + 1].top)
                    }) {
                    SingleRoute(
                        isBus = direction.type == DirectionType.DEPART,
                        walkOnly = transport.walkOnly,
                        stopName = direction.name,
                        distance = if (index == transport.directionList.lastIndex) transport.distance else null,
                        busLine = direction.routeId

                    )
                }

            }
        }
        Icon(
            imageVector = if (transport.directionList.lastOrNull()?.type == DirectionType.DEPART)
                ImageVector.vectorResource(R.drawable.bus_destination) else
                ImageVector.vectorResource(R.drawable.destination_stop),
            tint = Color.Unspecified,
            contentDescription = "",
            modifier = Modifier
                .padding(start = 70.5.dp)
                .offset(
                    y = if (transport.directionList.lastOrNull()?.type == DirectionType.WALK && !transport.walkOnly)
                        0.dp else (-5).dp
                )
        )
    }

}

/**
 * Composable function to display the header of a route cell.
 *
 * @param transport The transport data to display in the route cell.
 */
@Composable
private fun RouteCellHeader(transport: Transport) {
    val headerText =
        if (transport.walkOnly)
            "Directions" else
            "Board in ${transport.timeToBoard} min"
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = if (transport.walkOnly) 8.dp else 4.dp)
    ) {
        Text(
            "${transport.startTime} - ${transport.arriveTime}",
            fontFamily = helveticaFamily,
            color = PrimaryText,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 20.sp,
            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
        )
        Row(
            modifier = Modifier.wrapContentWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                headerText,
                fontFamily = helveticaFamily,
                color = transport.lateness.color(),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.left_arrow),
                contentDescription = ""
            )
        }
    }
}

/**
 * Composable function to display a single route within a route cell.
 *
 * @param isBus Indicates if the route is a bus route.
 * @param walkOnly Indicates if the route is walk-only.
 * @param stopName The name of the stop.
 * @param distance The distance to the stop.
 * @param busLine The bus line number.
 */
@Composable
fun SingleRoute(
    isBus: Boolean,
    walkOnly: Boolean,
    stopName: String,
    distance: String?,
    busLine: String?
) {
    ConstraintLayout {
        val (iconBox, startIcon, line, startLoc, dist) = createRefs()
        if (isBus) {
            Row(modifier = Modifier
                .background(
                    color = TransitBlue,
                    shape = RoundedCornerShape(4.dp)
                )
                .constrainAs(iconBox) {
                    top.linkTo(parent.bottom, margin = 8.dp)
                    bottom.linkTo(parent.top)
                    start.linkTo(parent.start, margin = 16.dp)
                }) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(
                        horizontal = 8.dp,
                    )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.bus),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                    Text(
                        "$busLine",
                        fontFamily = helveticaFamily,
                        fontSize = 10.sp,
                        fontStyle = FontStyle.Normal,
                        color = Color.White,
                    )
                }
            }
        } else {
            Column(modifier = Modifier.constrainAs(iconBox) {
                top.linkTo(parent.bottom, margin = 8.dp)
                bottom.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(line.start, margin = 8.dp)
            }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.walk),
                    tint = Color.Unspecified,
                    contentDescription = "",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                if (walkOnly && distance != null) {
                    Text(
                        "${String.format(Locale.US, "%.1f", distance.toFloat())} mi away",
                        color = MetadataGray,
                        fontFamily = helveticaFamily,
                        fontSize = 10.sp,
                    )

                }
            }
        }
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.boarding_stop),
            tint = if (isBus or !walkOnly) Color.Unspecified else IconGray,
            contentDescription = "",
            modifier = Modifier.constrainAs(startIcon) {
                top.linkTo(parent.top, margin = if (walkOnly) 10.dp else 12.dp)
                bottom.linkTo(line.top)
                start.linkTo(parent.start, margin = 72.dp)
                end.linkTo(startLoc.start)
            })

        DrawLine(
            isBus = isBus,
            walkOnly = walkOnly,
            Modifier.constrainAs(line) {
                top.linkTo(startIcon.top)
                bottom.linkTo(parent.bottom)
                end.linkTo(startIcon.end)
                start.linkTo(startIcon.start)
            }
        )

        Text(
            stopName,
            color = PrimaryText,
            fontFamily = helveticaFamily,
            fontStyle = FontStyle.Normal,
            fontSize = 14.sp,
            modifier = Modifier.constrainAs(startLoc) {
                top.linkTo(parent.top, margin = 2.dp)
                start.linkTo(startIcon.end, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
                bottom.linkTo(if (distance == null || walkOnly) parent.bottom else dist.top)
            })

        if (distance != null && !walkOnly) {
            Text(
                "${String.format(Locale.US, "%.1f", distance.toFloat())} mi away",
                color = MetadataGray,
                fontFamily = helveticaFamily,
                fontSize = 10.sp,
                modifier = Modifier.constrainAs(dist) {
                    start.linkTo(startLoc.start)
                    top.linkTo(startLoc.bottom, margin = (-6).dp)
                    bottom.linkTo(parent.bottom)
                })
        }

    }
}

/**
 * Composable function to draw a line indicating the route type.
 *
 * @param isBus Indicates if the route is a bus route.
 * @param walkOnly Indicates if the route is walk-only.
 * @param modifier The modifier to be applied to the line.
 */
@Composable
fun DrawLine(isBus: Boolean, walkOnly: Boolean, modifier: Modifier = Modifier) {
    if (isBus || walkOnly) {
        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.bus_route_line),
            tint = if (isBus) TransitBlue else IconGray,
            contentDescription = "",
            modifier = modifier.offset(y = 2.dp)
        )
    } else {
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = modifier.height(44.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ellipse_small),
                tint = Color.Unspecified,
                contentDescription = "",
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ellipse_small),
                tint = Color.Unspecified,
                contentDescription = "",
                modifier = Modifier.padding(bottom = 6.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSingleRouteWithDistance() {
    Box(modifier = Modifier.background(Color.White)) {
        SingleRoute(
            isBus = true,
            stopName = "Gates Hall",
            distance = "1.2",
            busLine = "30",
            walkOnly = false
        )

    }
}

@Preview
@Composable
private fun PreviewSingleRouteWithoutDistance() {
    Box(modifier = Modifier.background(Color.White)) {
        SingleRoute(
            isBus = true,
            stopName = "Gates Hall",
            distance = "0",
            busLine = "30",
            walkOnly = false
        )

    }
}

@Preview
@Composable
private fun PreviewSingleRouteIsWalk() {
    Box(modifier = Modifier.background(Color.White)) {
        SingleRoute(
            isBus = false,
            stopName = "Gates Hall",
            distance = "0",
            busLine = null,
            walkOnly = false
        )

    }
}

@Preview
@Composable
private fun PreviewSingleRouteWalkOnlyWithDistance() {
    Box(modifier = Modifier.background(Color.White)) {
        SingleRoute(
            isBus = false,
            stopName = "College @ Oak",
            distance = "1.2",
            busLine = null,
            walkOnly = true
        )

    }
}

@Preview
@Composable
private fun PreviewRouteCellBusAndWalk() {
    RouteCell(
        Transport(
            startTime = "12:42AM",
            arriveTime = "12:49AM",
            distance = "0.2",
            start = "Gates Hall",
            timeToBoard = 7,
            lateness = BusLateness.NORMAL,
            directionList = listOf(
                Direction(
                    endLocation = LatLng(40.7128, -74.0060),
                    routeId = "30",
                    tripIds = listOf("1"),
                    stayOnBusForTransfer = false,
                    delay = 0,
                    startLocation = LatLng(40.7128, -74.0060),
                    path = listOf(),
                    stops = listOf(),
                    endTime = "12:49AM",
                    distance = "0.2",
                    startTime = "12:42AM",
                    directionType = "depart",
                    name = "Gates Hall"
                ),
                Direction(
                    endLocation = LatLng(40.7128, -74.0060),
                    routeId = "",
                    tripIds = listOf("1"),
                    stayOnBusForTransfer = false,
                    delay = 0,
                    startLocation = LatLng(40.7128, -74.0060),
                    path = listOf(),
                    stops = listOf(),
                    endTime = "12:49AM",
                    distance = "0.2",
                    startTime = "12:42AM",
                    directionType = "walk",
                    name = "Duffield Hall"
                ),
            ),
            walkOnly = false
        )
    )
}

@Preview
@Composable
private fun PreviewRouteCellMultBusesAndWalk() {
    RouteCell(
        Transport(
            startTime = "12:42AM",
            arriveTime = "12:49AM",
            distance = "0.2",
            start = "Gates Hall",
            timeToBoard = 7,
            lateness = BusLateness.NORMAL,
            directionList = listOf(
                Direction(
                    endLocation = LatLng(40.7128, -74.0060),
                    routeId = "30",
                    tripIds = listOf("1"),
                    stayOnBusForTransfer = false,
                    delay = 0,
                    startLocation = LatLng(40.7128, -74.0060),
                    path = listOf(),
                    stops = listOf(),
                    endTime = "12:49AM",
                    distance = "0.2",
                    startTime = "12:42AM",
                    directionType = "depart",
                    name = "Gates Hall"
                ),
                Direction(
                    endLocation = LatLng(40.7128, -74.0060),
                    routeId = "",
                    tripIds = listOf("1"),
                    stayOnBusForTransfer = false,
                    delay = 0,
                    startLocation = LatLng(40.7128, -74.0060),
                    path = listOf(),
                    stops = listOf(),
                    endTime = "12:49AM",
                    distance = "0.2",
                    startTime = "12:42AM",
                    directionType = "walk",
                    name = "Phillips Hall"
                ),
                Direction(
                    endLocation = LatLng(40.7128, -74.0060),
                    routeId = "81",
                    tripIds = listOf("1"),
                    stayOnBusForTransfer = false,
                    delay = 0,
                    startLocation = LatLng(40.7128, -74.0060),
                    path = listOf(),
                    stops = listOf(),
                    endTime = "12:49AM",
                    distance = "0.2",
                    startTime = "12:42AM",
                    directionType = "depart",
                    name = "Duffield Hall"
                ),
            ),
            walkOnly = false
        )
    )
}

@Preview
@Composable
private fun PreviewRouteCellWalkOnly() {
    RouteCell(
        Transport(
            startTime = "12:42AM",
            arriveTime = "12:49AM",
            distance = "0.2",
            lateness = BusLateness.NONE,
            start = "Gates Hall",
            walkOnly = true,
            timeToBoard = 0,
            directionList = listOf(
                Direction(
                    endLocation = LatLng(40.7128, -74.0060),
                    routeId = "30",
                    tripIds = listOf("1"),
                    stayOnBusForTransfer = false,
                    delay = 0,
                    startLocation = LatLng(40.7128, -74.0060),
                    path = listOf(),
                    stops = listOf(),
                    endTime = "12:49AM",
                    distance = "0.2",
                    startTime = "12:42AM",
                    directionType = "walk",
                    name = "Gates Hall"
                )
            )
        )
    )
}