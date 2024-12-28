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
import androidx.compose.foundation.layout.size
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
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.BusLateness
import com.cornellappdev.transit.models.Direction
import com.cornellappdev.transit.models.DirectionStop
import com.cornellappdev.transit.models.DirectionType
import com.cornellappdev.transit.models.Transport
import com.cornellappdev.transit.ui.theme.IconGray
import com.cornellappdev.transit.ui.theme.MetadataGray
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.sfProDisplayFamily
import com.cornellappdev.transit.ui.theme.sfProTextFamily
import com.google.android.gms.maps.model.LatLng

//Invariant: if transport is WalkOnly, lateness must be None <- maybe can enforce this somehow idk
@Composable
fun RouteCell(transport: Transport) {
    val headerText =
        if (transport.walkOnly)
            "Directions" else
            "Board in ${transport.timeToBoard} min"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (transport.walkOnly) 8.dp else 4.dp)
            ) {
                Text(
                    "${transport.startTime} - ${transport.arriveTime}",
                    fontFamily = sfProDisplayFamily,
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
                        fontFamily = sfProDisplayFamily,
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

            if (!transport.walkOnly) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 4.dp)
                ) {
                    Text(
                        text = transport.lateness.text(),
                        fontFamily = sfProDisplayFamily,
                        color = transport.lateness.color(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 16.sp,
                        style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.live_glyph__late_),
                        contentDescription = "",
                        tint = transport.lateness.color()
                    )
                }
            }
            Row() {

            }
            for (direction in transport.directionList) {
                SingleRoute(
                    isBus = direction.type == DirectionType.DEPART,
                    walkOnly = transport.walkOnly,
                    stopName = direction.endLocation.toString(),
                    distance = direction.distance,
                    busLine = direction.routeId
                )
            }
            Icon(
                imageVector = if (transport.directionList.last().type == DirectionType.DEPART)
                    ImageVector.vectorResource(R.drawable.bus_destination) else
                    ImageVector.vectorResource(R.drawable.destination_stop),
                tint = Color.Unspecified,
                contentDescription = "",
                modifier = Modifier
                    .padding(start = 70.dp)
                    .offset(y = if (transport.walkOnly) (-4).dp else 0.dp)
            )
            /*when (transport) {
                is Transport.WalkOnly -> {
                    ConstraintLayout {
                        val (route, dest, toIcon) = createRefs()

                        Box(modifier = Modifier.constrainAs(route){
                            top.linkTo(parent.top, margin = 8.dp)
                            start.linkTo(parent.start)
                        }){
                            SingleRoute(
                                isBus = false,
                                walkOnly = true,
                                stopName = "Current Location",
                                distance = transport.distance,
                                busLine = null
                            )
                        }


                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.destination_stop),
                            contentDescription = "",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(16.dp)
                                .constrainAs(toIcon) {
                                    top.linkTo(route.bottom)
                                    start.linkTo(parent.start, margin = 70.dp)
                                }
                        )


                        Text(
                            transport.dest,
                            fontFamily = sfProDisplayFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.constrainAs(dest) {
                                start.linkTo(toIcon.end, margin = 14.dp)
                                top.linkTo(toIcon.top)
                                bottom.linkTo(toIcon.bottom)
                            })


                    }
                }

                is Transport.WalkAndBus -> {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 0.dp)
                    ) {
                        Text(
                            text = transport.lateness.text(),
                            fontFamily = sfProDisplayFamily,
                            color = transport.lateness.color(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 16.sp,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.live_glyph__late_),
                            contentDescription = "",
                            tint = transport.lateness.color()
                        )
                    }

                    ConstraintLayout {
                        val (endStop, startLoc, destLoc, busBox, walkIcon, dist, startIcon, endIcon, destIcon, line, el1, el2) = createRefs()

                        Row(modifier = Modifier
                            .background(
                                color = TransitBlue,
                                shape = RoundedCornerShape(4.dp)
                            )


                            .constrainAs(busBox) {
                                top.linkTo(startLoc.bottom)
                                bottom.linkTo(endStop.top)
                            }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(
                                    horizontal = 8.dp,
                                    vertical = 0.dp
                                )
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.bus),
                                    contentDescription = "",
                                    tint = Color.Unspecified
                                )
                                Text(
                                    "${transport.bus}",
                                    fontFamily = sfProDisplayFamily,
                                    fontSize = 10.sp,
                                    fontStyle = FontStyle.Normal,
                                    color = Color.White,
                                )
                            }
                        }

                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.walk),
                            contentDescription = "",
                            tint = Color.Unspecified,
                            modifier = Modifier.constrainAs(walkIcon) {
                                start.linkTo(busBox.start)
                                end.linkTo(busBox.end)
                                top.linkTo(endStop.bottom)
                                bottom.linkTo(destLoc.top)
                            })

                        Text(
                            transport.start,
                            color = PrimaryText,
                            fontFamily = sfProDisplayFamily,
                            fontStyle = FontStyle.Normal,
                            fontSize = 14.sp,
                            modifier = Modifier.constrainAs(startLoc) {
                                top.linkTo(parent.top)
                                start.linkTo(startIcon.end, margin = 16.dp)
                            })

                        Text(
                            "${transport.distance} mi away",
                            color = MetadataGray,
                            fontFamily = sfProTextFamily,
                            fontSize = 10.sp,
                            modifier = Modifier.constrainAs(dist) {
                                start.linkTo(startLoc.start)
                                top.linkTo(startLoc.bottom, margin = (-6).dp)
                            })
                        Text(
                            transport.endStop,
                            color = PrimaryText,
                            fontFamily = sfProDisplayFamily,
                            fontStyle = FontStyle.Normal,
                            fontSize = 14.sp,
                            modifier = Modifier.constrainAs(endStop) {
                                start.linkTo(startLoc.start)
                                top.linkTo(dist.bottom, margin = 8.dp)
                            })
                        Text(
                            transport.dest,
                            color = PrimaryText,
                            fontFamily = sfProDisplayFamily,
                            fontStyle = FontStyle.Normal,
                            fontSize = 14.sp,
                            modifier = Modifier.constrainAs(destLoc) {
                                top.linkTo(endStop.bottom, margin = 8.dp)
                                start.linkTo(startLoc.start)
                            })

                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.boarding_stop),
                            tint = Color.Unspecified,
                            contentDescription = "",
                            modifier = Modifier.constrainAs(startIcon) {
                                end.linkTo(startLoc.start)
                                top.linkTo(startLoc.top)
                                bottom.linkTo(startLoc.bottom)
                                start.linkTo(busBox.end, margin = 16.dp)
                            })

                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.bus_route_line),
                            tint = TransitBlue,
                            contentDescription = "",
                            modifier = Modifier.constrainAs(line) {
                                top.linkTo(startIcon.top)
                                bottom.linkTo(endIcon.bottom)
                                end.linkTo(startIcon.end)
                                start.linkTo(startIcon.start)

                            })

                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.boarding_stop),
                            tint = Color.Unspecified,
                            contentDescription = "",
                            modifier = Modifier.constrainAs(endIcon) {
                                end.linkTo(startIcon.end)
                                top.linkTo(endStop.top)
                                bottom.linkTo(endStop.bottom)
                                start.linkTo(startIcon.start)
                            })

                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.destination_stop),
                            tint = Color.Unspecified,
                            contentDescription = "",
                            modifier = Modifier.constrainAs(destIcon) {
                                top.linkTo(destLoc.top)
                                bottom.linkTo(destLoc.bottom)
                                end.linkTo(startIcon.end)
                                start.linkTo(startIcon.start)
                            })

                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ellipse_small),
                            tint = Color.Unspecified,
                            contentDescription = "",
                            modifier = Modifier.constrainAs(el1) {
                                top.linkTo(endIcon.bottom)
                                bottom.linkTo(walkIcon.bottom, margin = 7.dp)
                                end.linkTo(destIcon.end)
                                start.linkTo(destIcon.start)
                            })

                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ellipse_small),
                            tint = Color.Unspecified,
                            contentDescription = "",
                            modifier = Modifier.constrainAs(el2) {
                                top.linkTo(el1.bottom)
                                bottom.linkTo(destIcon.top)
                                end.linkTo(destIcon.end)
                                start.linkTo(destIcon.start)
                            })

                    }


                }

                is Transport.HasBus -> {
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = transport.lateness.text(),
                            fontFamily = sfProDisplayFamily,
                            color = transport.lateness.color(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.live_glyph__late_),
                            contentDescription = "",
                            tint = transport.lateness.color()
                        )
                    }

                    ConstraintLayout {
                        val (endStop, startLoc, destLoc, busBox, line2, lineIcon, dist, startIcon, destIcon, secondBusBox) = createRefs()
                        lateinit var transferRefs:  MutableMap<Int, ConstrainedLayoutReference>
                        lateinit var routeLineRefs:  MutableMap<Int, ConstrainedLayoutReference>
                        lateinit var boardingStopRefs:  MutableMap<Int, ConstrainedLayoutReference>
                        Row(modifier = Modifier
                            .background(
                                color = TransitBlue,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .constrainAs(busBox) {
                                top.linkTo(startLoc.bottom)
                                bottom.linkTo(endStop.top)
                            }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(
                                    horizontal = 8.dp,
                                    vertical = 0.dp
                                )
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.bus),
                                    contentDescription = "",
                                    tint = Color.Unspecified
                                )
                                Text(
                                    "${transport.firstBus}",
                                    fontFamily = sfProDisplayFamily,
                                    fontSize = 10.sp,
                                    fontStyle = FontStyle.Normal,
                                    color = Color.White,
                                )
                            }
                        }

                        Row(modifier = Modifier
                            .background(
                                color = TransitBlue,
                                shape = RoundedCornerShape(4.dp)
                            )


                            .constrainAs(secondBusBox) {
                                start.linkTo(busBox.start)
                                end.linkTo(busBox.end)
                                top.linkTo(endStop.bottom)
                                bottom.linkTo(destLoc.top)
                            }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(
                                    horizontal = 8.dp,
                                    vertical = 0.dp
                                )
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.bus),
                                    contentDescription = "",
                                    tint = Color.Unspecified
                                )
                                Text(
                                    "${transport.secondBus}",
                                    fontFamily = sfProDisplayFamily,
                                    fontSize = 10.sp,
                                    fontStyle = FontStyle.Normal,
                                    color = Color.White,
                                )
                            }
                        }

                        Text(
                            transport.start,
                            color = PrimaryText,
                            fontFamily = sfProDisplayFamily,
                            fontStyle = FontStyle.Normal,
                            fontSize = 14.sp,
                            modifier = Modifier.constrainAs(startLoc) {
                                top.linkTo(parent.top)
                                start.linkTo(startIcon.end, margin = 16.dp)
                            })

                        Text(
                            "${transport.distance} mi away",
                            color = MetadataGray,
                            fontFamily = sfProTextFamily,
                            fontSize = 10.sp,
                            modifier = Modifier.constrainAs(dist) {
                                start.linkTo(startLoc.start)
                                top.linkTo(startLoc.bottom, margin = (-6).dp)
                            })

                        ConstraintLayout(modifier = Modifier.constrainAs(endStop) {
                            start.linkTo(startLoc.start)
                            top.linkTo(dist.bottom, margin = 0.dp)
                        }){
                            transferRefs = mutableMapOf()

                            transport.directionList.forEachIndexed { index, _ ->
                                transferRefs[index] = createRef()
                            }
                            transport.directionList.forEachIndexed { index, direction ->
                                Text(
                                    direction.endLocation.toString(),
                                    color = PrimaryText,
                                    fontFamily = sfProDisplayFamily,
                                    fontStyle = FontStyle.Normal,
                                    fontSize = 14.sp,
                                    modifier = Modifier.constrainAs(transferRefs[index]!!) {
                                        top.linkTo(
                                            if (index == 0) parent.top else transferRefs[index - 1]!!.bottom,
                                            margin = 8.dp
                                        )
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                                )
                            }

                        }

                        Text(
                            transport.dest,
                            color = PrimaryText,
                            fontFamily = sfProDisplayFamily,
                            fontStyle = FontStyle.Normal,
                            fontSize = 14.sp,
                            modifier = Modifier.constrainAs(destLoc) {
                                top.linkTo(endStop.bottom, margin = 8.dp)
                                start.linkTo(startLoc.start)
                            })

                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.boarding_stop),
                            tint = Color.Unspecified,
                            contentDescription = "",
                            modifier = Modifier.constrainAs(startIcon) {
                                end.linkTo(startLoc.start)
                                top.linkTo(startLoc.top)
                                bottom.linkTo(startLoc.bottom)
                                start.linkTo(busBox.end, margin = 16.dp)
                            })

                        ConstraintLayout(modifier = Modifier.constrainAs(lineIcon) {
                            top.linkTo(startIcon.top, margin = 8.dp)
                            bottom.linkTo(line2.top)
                            end.linkTo(startIcon.end)
                            start.linkTo(startIcon.start)
                        }) {
                            routeLineRefs = mutableMapOf()
                            boardingStopRefs = mutableMapOf()

                            transport.directionList.forEachIndexed { index, _ ->
                                routeLineRefs[index] = createRef()
                                boardingStopRefs[index] = createRef()
                            }

                            transport.directionList.forEachIndexed { index, _ ->

                                val routeLineRef = routeLineRefs[index]!!
                                val boardingStopRef = boardingStopRefs[index]!!

                                Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.bus_route_line),
                                tint = TransitBlue,
                                contentDescription = "",
                                    modifier = Modifier.constrainAs(routeLineRef) {
                                        top.linkTo(
                                            if (index == 0) startIcon.bottom else boardingStopRefs[index-1]!!.top, margin = 6.dp
                                        )
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    })

                                Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.boarding_stop),
                                tint = Color.Unspecified,
                                contentDescription = "",
                                modifier = Modifier.constrainAs(boardingStopRef) {

                                    bottom.linkTo(routeLineRefs[index]!!.bottom, margin =(-2).dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                })
                                }
                        }

                        /*Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.bus_route_line),
                            tint = TransitBlue,
                            contentDescription = "",
                            modifier = Modifier.constrainAs(line) {
                                top.linkTo(startIcon.top)
                                bottom.linkTo(endIcon.bottom)
                                end.linkTo(startIcon.end)
                                start.linkTo(startIcon.start)

                            })

                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.boarding_stop),
                            tint = Color.Unspecified,
                            contentDescription = "",
                            modifier = Modifier.constrainAs(endIcon) {
                                end.linkTo(startIcon.end)
                                top.linkTo(endStop.top)
                                bottom.linkTo(endStop.bottom)
                                start.linkTo(startIcon.start)
                            })*/

                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.bus_route_line),
                            tint = TransitBlue,
                            contentDescription = "",
                            modifier = Modifier.constrainAs(line2) {
                                top.linkTo(lineIcon.bottom)
                                bottom.linkTo(destIcon.bottom)
                                end.linkTo(lineIcon.end)
                                start.linkTo(lineIcon.start)

                            })

                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.bus_destination),
                            tint = Color.Unspecified,
                            contentDescription = "",
                            modifier = Modifier.constrainAs(destIcon) {
                                top.linkTo(destLoc.top)
                                bottom.linkTo(destLoc.bottom)
                                end.linkTo(startIcon.end)
                                start.linkTo(startIcon.start)
                            })


                    }
                }
            }*/
        }
    }
}

@Composable
fun SingleRoute(
    isBus: Boolean,
    walkOnly: Boolean,
    stopName: String,
    distance: String,
    busLine: String?
) {
    ConstraintLayout() {
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
                        vertical = 0.dp
                    )
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.bus),
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                    Text(
                        "$busLine",
                        fontFamily = sfProDisplayFamily,
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
                if (walkOnly) {
                    Text(
                        "$distance mi away",
                        color = MetadataGray,
                        fontFamily = sfProTextFamily,
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

        if (isBus) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.bus_route_line),
                tint = TransitBlue,
                contentDescription = "",
                modifier = Modifier.constrainAs(line) {
                    top.linkTo(startIcon.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(startIcon.end)
                    start.linkTo(startIcon.start)

                })
        } else {
            if (walkOnly) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.bus_route_line),
                    tint = IconGray,
                    contentDescription = "",
                    modifier = Modifier.constrainAs(line) {
                        top.linkTo(startIcon.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(startIcon.end)
                        start.linkTo(startIcon.start)

                    })
            } else {
                Column(verticalArrangement = Arrangement.Bottom,

                    modifier = Modifier
                        .constrainAs(line) {
                            top.linkTo(startIcon.top)
                            bottom.linkTo(parent.bottom)
                            end.linkTo(startIcon.end)
                            start.linkTo(startIcon.start)
                        }
                        .height(41.dp)) {
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
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }

        }

        Text(
            stopName,
            color = PrimaryText,
            fontFamily = sfProDisplayFamily,
            fontStyle = FontStyle.Normal,
            fontSize = 14.sp,
            modifier = Modifier.constrainAs(startLoc) {
                top.linkTo(parent.top, margin = 2.dp)
                start.linkTo(startIcon.end, margin = 16.dp)
                end.linkTo(parent.end, margin = 16.dp)
                bottom.linkTo(if (((distance == null)) or walkOnly) parent.bottom else dist.top)
            })

        if ((distance != null) and !walkOnly) {
            Text(
                "$distance mi away",
                color = MetadataGray,
                fontFamily = sfProTextFamily,
                fontSize = 10.sp,
                modifier = Modifier.constrainAs(dist) {
                    start.linkTo(startLoc.start)
                    top.linkTo(startLoc.bottom, margin = (-6).dp)
                })
        }

    }
}

@Preview
@Composable
fun PreviewSingleRouteWithDistance() {
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
fun PreviewSingleRouteWithoutDistance() {
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
fun PreviewSingleRouteIsWalk() {
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
fun PreviewSingleRouteIsWalkOnlyWithDistance() {
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
fun PreviewRouteCell() {
    RouteCell(
        Transport(
            startTime = "12:42AM",
            arriveTime = "12:49AM",
            distance = "0.2",
            start = "Gates Hall",
            dest = "Collegetown Terrace Apartments",
            timeToBoard = 7,
            lateness = BusLateness.NORMAL,
            directionList = listOf<Direction>(
                Direction(
                    endLocation = LatLng(40.7128, -74.0060),
                    routeId = "30",
                    tripIds = listOf("1"),
                    stayOnBusForTransfer = false,
                    delay = 0,
                    startLocation = LatLng(40.7128, -74.0060),
                    path = listOf<LatLng>(),
                    stops = listOf<DirectionStop>(),
                    endTime = "12:49AM",
                    distance = "0.2",
                    startTime = "12:42AM",
                    directionType = "depart",
                ),
                Direction(
                    endLocation = LatLng(40.7128, -74.0060),
                    routeId = "",
                    tripIds = listOf("1"),
                    stayOnBusForTransfer = false,
                    delay = 0,
                    startLocation = LatLng(40.7128, -74.0060),
                    path = listOf<LatLng>(),
                    stops = listOf<DirectionStop>(),
                    endTime = "12:49AM",
                    distance = "0.2",
                    startTime = "12:42AM",
                    directionType = "walk",
                ),
            ),
            walkOnly = false
        )
    )
}

@Preview
@Composable
fun PreviewRouteCell2() {
    RouteCell(
        Transport(
            startTime = "12:42AM",
            arriveTime = "12:49AM",
            distance = "0.2",
            start = "Gates Hall",
            dest = "Collegetown Terrace Apartments",
            timeToBoard = 7,
            lateness = BusLateness.NORMAL,
            directionList = listOf<Direction>(
                Direction(
                    endLocation = LatLng(40.7128, -74.0060),
                    routeId = "30",
                    tripIds = listOf("1"),
                    stayOnBusForTransfer = false,
                    delay = 0,
                    startLocation = LatLng(40.7128, -74.0060),
                    path = listOf<LatLng>(),
                    stops = listOf<DirectionStop>(),
                    endTime = "12:49AM",
                    distance = "0.2",
                    startTime = "12:42AM",
                    directionType = "depart",
                ),
                Direction(
                    endLocation = LatLng(40.7128, -74.0060),
                    routeId = "30",
                    tripIds = listOf("1"),
                    stayOnBusForTransfer = false,
                    delay = 0,
                    startLocation = LatLng(40.7128, -74.0060),
                    path = listOf<LatLng>(),
                    stops = listOf<DirectionStop>(),
                    endTime = "12:49AM",
                    distance = "0.2",
                    startTime = "12:42AM",
                    directionType = "walk",
                ),
                Direction(
                    endLocation = LatLng(40.7128, -74.0060),
                    routeId = "30",
                    tripIds = listOf("1"),
                    stayOnBusForTransfer = false,
                    delay = 0,
                    startLocation = LatLng(40.7128, -74.0060),
                    path = listOf<LatLng>(),
                    stops = listOf<DirectionStop>(),
                    endTime = "12:49AM",
                    distance = "0.2",
                    startTime = "12:42AM",
                    directionType = "depart",
                ),
            ),
            walkOnly = false
        )
    )
}

@Preview
@Composable
fun PreviewRouteCell3() {
    RouteCell(
        Transport(
            startTime = "12:42AM",
            arriveTime = "12:49AM",
            distance = "0.2",
            lateness = BusLateness.NONE,
            dest = "Collegetown Terrace Apartments",
            start = "Gates Hall",
            walkOnly = true,
            timeToBoard = 0,
            directionList = listOf<Direction>(
                Direction(
                    endLocation = LatLng(40.7128, -74.0060),
                    routeId = "30",
                    tripIds = listOf("1"),
                    stayOnBusForTransfer = false,
                    delay = 0,
                    startLocation = LatLng(40.7128, -74.0060),
                    path = listOf<LatLng>(),
                    stops = listOf<DirectionStop>(),
                    endTime = "12:49AM",
                    distance = "0.2",
                    startTime = "12:42AM",
                    directionType = "walk",
                )
            )
        )
    )
}