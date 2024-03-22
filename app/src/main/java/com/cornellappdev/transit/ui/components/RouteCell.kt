package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.R
import androidx.constraintlayout.compose.ConstraintLayout
import com.cornellappdev.transit.ui.theme.IconGrey
import com.cornellappdev.transit.ui.theme.LateRed
import com.cornellappdev.transit.ui.theme.LiveGreen
import com.cornellappdev.transit.ui.theme.MetadataGrey
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.sfProDisplayFamily
import com.cornellappdev.transit.ui.theme.sfProTextFamily

//Invariant: if transport is WalkOnly, lateness must be None <- maybe can enforce this somehow idk
@Composable
fun RouteCell(transport: Transport) {
    when (transport) {
        is Transport.WalkOnly -> {
            Row(
                modifier = Modifier
                    .width(375.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "${transport.startTime} - ${transport.arriveTime}",
                            fontFamily = sfProDisplayFamily,
                            color = PrimaryText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Row(modifier = Modifier.wrapContentWidth()) {
                            Text(
                                "Directions",
                                fontFamily = sfProDisplayFamily,
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.Outlined.KeyboardArrowRight,
                                contentDescription = ""
                            )
                        }
                    }

                    ConstraintLayout {
                        val (walkIcon, dist, currLoc, dest, fromIcon, toIcon, line) = createRefs()

                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.walk),
                            tint = Color.Unspecified,
                            contentDescription = "",
                            modifier = Modifier.constrainAs(walkIcon) {
                                start.linkTo(dist.start)
                                end.linkTo(dist.end)
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                            })
                        Text(
                            "${transport.distance} mi",
                            fontFamily = sfProDisplayFamily,
                            fontStyle = FontStyle.Normal,
                            color = MetadataGrey,
                            fontSize = 10.sp,
                            modifier = Modifier.constrainAs(dist) {
                                top.linkTo(walkIcon.bottom)
                                start.linkTo(parent.start)
                            })

                        Icon(imageVector = ImageVector.vectorResource(id = R.drawable.boarding_stop),
                            contentDescription = "",
                            tint = IconGrey,
                            modifier = Modifier
                                .size(12.dp)
                                .constrainAs(fromIcon) {
                                    top.linkTo(parent.top, margin = 12.dp)
                                    start.linkTo(dist.end, margin = 16.dp)

                                })

                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.bus_route_line),
                            contentDescription = "",
                            tint = IconGrey,
                            modifier = Modifier.constrainAs(line) {
                                top.linkTo(fromIcon.top)
                                bottom.linkTo(toIcon.bottom)
                                start.linkTo(fromIcon.start)
                                end.linkTo(fromIcon.end)
                            })

                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.destination_stop),
                            contentDescription = "",
                            tint = Color.Unspecified,
                            modifier = Modifier
                                .size(16.dp)
                                .constrainAs(toIcon) {
                                    bottom.linkTo(parent.bottom)
                                    start.linkTo(fromIcon.start)
                                    end.linkTo(fromIcon.end)
                                }
                        )
                        Text(
                            "Current Location",
                            fontFamily = sfProDisplayFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.constrainAs(currLoc) {
                                start.linkTo(fromIcon.end, margin = 16.dp)
                                top.linkTo(fromIcon.top)
                                bottom.linkTo(fromIcon.bottom)
                            })

                        Text(
                            transport.dest,
                            fontFamily = sfProDisplayFamily,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.constrainAs(dest) {
                                start.linkTo(currLoc.start)
                                top.linkTo(toIcon.top)
                                bottom.linkTo(toIcon.bottom)
                            })


                    }
                }
            }
        }

        is Transport.WalkAndBus -> {
            Row(
                modifier = Modifier
                    .width(375.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "${transport.startTime} - ${transport.arriveTime}",
                            fontFamily = sfProDisplayFamily,
                            color = PrimaryText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Row(modifier = Modifier.wrapContentWidth()) {
                            Text(
                                "Board in ${transport.timeToBoard} min",
                                fontFamily = sfProDisplayFamily,
                                color = transport.lateness.color(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.Outlined.KeyboardArrowRight,
                                contentDescription = ""
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = transport.lateness.text(),
                            fontFamily = sfProDisplayFamily,
                            color = transport.lateness.color(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
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
                            .background(color = TransitBlue, shape = RoundedCornerShape(4.dp))


                            .constrainAs(busBox) {
                                top.linkTo(startLoc.bottom)
                                bottom.linkTo(endStop.top)
                            }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp)
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
                            color = MetadataGrey,
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
            }
        }

        is Transport.BusOnly -> {
            Row(
                modifier = Modifier
                    .width(375.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "${transport.startTime} - ${transport.arriveTime}",
                            fontFamily = sfProDisplayFamily,
                            color = PrimaryText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Row(modifier = Modifier.wrapContentWidth()) {
                            Text(
                                "Board in ${transport.timeToBoard} min",
                                fontFamily = sfProDisplayFamily,
                                color = transport.lateness.color(),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Icon(
                                imageVector = Icons.Outlined.KeyboardArrowRight,
                                contentDescription = ""
                            )
                        }
                    }

                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = transport.lateness.text(),
                            fontFamily = sfProDisplayFamily,
                            color = transport.lateness.color(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.live_glyph__late_),
                            contentDescription = "",
                            tint = transport.lateness.color()
                        )
                    }

                    ConstraintLayout {
                        val (endStop, startLoc, destLoc, busBox, line2, dist, startIcon, endIcon, destIcon, line, secondBusBox) = createRefs()

                        Row(modifier = Modifier
                            .background(color = TransitBlue, shape = RoundedCornerShape(4.dp))


                            .constrainAs(busBox) {
                                top.linkTo(startLoc.bottom)
                                bottom.linkTo(endStop.top)
                            }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp)
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
                            .background(color = TransitBlue, shape = RoundedCornerShape(4.dp))


                            .constrainAs(secondBusBox) {
                                start.linkTo(busBox.start)
                                end.linkTo(busBox.end)
                                top.linkTo(endStop.bottom)
                                bottom.linkTo(destLoc.top)
                            }) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 0.dp)
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
                            color = MetadataGrey,
                            fontFamily = sfProTextFamily,
                            fontSize = 10.sp,
                            modifier = Modifier.constrainAs(dist) {
                                start.linkTo(startLoc.start)
                                top.linkTo(startLoc.bottom, margin = (-6).dp)
                            })
                        Text(
                            transport.transferStop,
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
                            imageVector = ImageVector.vectorResource(R.drawable.bus_route_line),
                            tint = TransitBlue,
                            contentDescription = "",
                            modifier = Modifier.constrainAs(line2) {
                                top.linkTo(endIcon.top)
                                bottom.linkTo(destIcon.bottom)
                                end.linkTo(endIcon.end)
                                start.linkTo(endIcon.start)

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
            }
        }
    }

}

enum class BusLateness {
    LATE {
        override fun color() = LateRed
        override fun text() = "Delayed"
    },
    NORMAL {
        override fun color() = LiveGreen
        override fun text() = "On Time"
    },
    NONE {
        override fun color() = Color.Black
        override fun text() = ""
    };

    abstract fun color(): Color
    abstract fun text(): String
}

@Preview
@Composable
fun PreviewRouteCell() {
    RouteCell(
        Transport.WalkOnly(
            startTime = "12:42AM",
            arriveTime = "12:49AM",
            distance = "0.2",
            dest = "Collegetown Terrace Apartments",
        )
    )
}

sealed class Transport {
    class WalkOnly(
        val startTime: String,
        val arriveTime: String,
        val distance: String,
        val dest: String
    ) : Transport()

    class WalkAndBus(
        val startTime: String,
        val arriveTime: String,
        val distance: String,
        val start: String,
        val dest: String,
        val timeToBoard: Int,
        val endStop: String,
        val bus: Int,
        val lateness: BusLateness
    ) : Transport()

    class BusOnly(
        val startTime: String,
        val arriveTime: String,
        val distance: String,
        val start: String,
        val dest: String,
        val transferStop: String,
        val timeToBoard: Int,
        val firstBus: Int,
        val secondBus: Int,
        val lateness: BusLateness
    ) : Transport()
}