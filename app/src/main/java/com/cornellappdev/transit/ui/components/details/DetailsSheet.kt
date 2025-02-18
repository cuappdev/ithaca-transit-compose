package com.cornellappdev.transit.ui.components.details

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.Direction
import com.cornellappdev.transit.models.DirectionStop
import com.cornellappdev.transit.models.DirectionType
import com.cornellappdev.transit.models.Route
import com.cornellappdev.transit.ui.theme.DividerGray
import com.cornellappdev.transit.ui.theme.IconGray
import com.cornellappdev.transit.ui.theme.MetadataGray
import com.cornellappdev.transit.ui.theme.Style.heading4
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.sfProDisplayFamily
import com.cornellappdev.transit.util.TimeUtils
import java.text.SimpleDateFormat
import java.util.Locale


//TODO: Move Route parsing logic into RouteViewModel
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DetailsSheet(route: Route) {

    val expandedStops = remember { mutableStateListOf(*Array(route.directions.size) { false }) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .animateContentSize()
            .padding(horizontal = 8.dp)
    ) {
        itemsIndexed(route.directions) { index, direction ->

            DirectionItem(
                time = if (index == route.directions.lastIndex)
                    TimeUtils.getHHMM(direction.endTime) else TimeUtils.getHHMM(
                    direction.startTime
                ),
                movementDescription = if (direction.type == DirectionType.DEPART) "Board" else "Walk to",
                destination = direction.name,
                directionType = direction.type,
                drawSegmentAbove = index != 0,
                drawSegmentBelow = index != route.directions.lastIndex,
                isFinalDestination = index == route.directions.lastIndex,
                busNumber = direction.routeId ?: "",
                numStops = direction.stops.size - 1,
                modifier = Modifier.clickable {
                    expandedStops[index] = !expandedStops[index]
                },
                duration = TimeUtils.minuteDifference(direction.startTime, direction.endTime),
                expandedStops = expandedStops[index],
                colorAbove = if (index > 0 && route.directions[index - 1].type == DirectionType.DEPART && direction.type == DirectionType.DEPART) TransitBlue else Color.Gray,
                colorBelow = if (direction.type == DirectionType.DEPART) TransitBlue else Color.Gray
            )

            if (expandedStops[index]) {
                for (stop in direction.stops.drop(1).dropLast(1)) {
                    StopItem(stop.name, stop.latitude.toDouble(), stop.long.toDouble())
                }
            }

            if (direction.type == DirectionType.DEPART) {
                DirectionItem(
                    time = TimeUtils.getHHMM(direction.endTime),
                    movementDescription = "Get off",
                    destination = direction.stops.last().name,
                    directionType = direction.type,
                    drawSegmentAbove = true,
                    drawSegmentBelow = index != route.directions.lastIndex,
                    isFinalDestination = index == route.directions.lastIndex,
                    busNumber = direction.routeId ?: "",
                    isLastStop = true,
                    colorAbove = TransitBlue

                )
            }

        }
    }


}

@Composable
fun DirectionItem(
    time: String,
    movementDescription: String,
    destination: String,
    directionType: DirectionType,
    drawSegmentAbove: Boolean,
    drawSegmentBelow: Boolean,
    isFinalDestination: Boolean,
    busNumber: String,
    modifier: Modifier = Modifier,
    isLastStop: Boolean = false,
    numStops: Int = 0,
    duration: Int = 0,
    expandedStops: Boolean = false,
    colorAbove: Color = Color.Gray,
    colorBelow: Color = Color.Gray
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(start = 12.dp, end = 12.dp)
            .height(80.dp)
    ) {
        // Time Text
        Column {
            Text(
                text = time,
                color = Color.Black,
                modifier = Modifier
                    .width(72.dp)
                    .padding(end = 8.dp),
                style = heading4
            )
        }

        // Direction Dot & Segments
        val segmentHeight = 32.dp
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(segmentHeight)
                    .background(if (drawSegmentAbove) colorAbove else Color.Transparent)
            )

            Canvas(
                modifier = Modifier
                    .size(16.dp)
            ) {
                drawCircle(
                    color = if (directionType == DirectionType.DEPART) TransitBlue else Color.Gray
                )
            }

            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(segmentHeight)
                    .background(if (drawSegmentBelow) colorBelow else Color.Transparent)
            )

        }

        Spacer(modifier = Modifier.width(20.dp))

        // Description
        val inlineContent = if (directionType == DirectionType.DEPART) mapOf(
            "busIcon" to InlineTextContent(
                placeholder = Placeholder(
                    width = 40.sp,
                    height = 24.sp,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                )
            ) {
                BusIcon(busNumber.toInt(), isSmall = true)
            }
        ) else emptyMap()

        val annotatedString =
            if (directionType == DirectionType.WALK)
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 12.sp,
                        )
                    ) {
                        append("$movementDescription ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append(destination)
                    }
                } else buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontSize = 12.sp,
                    )
                ) {
                    append("$movementDescription ")
                }
                if (!isLastStop) appendInlineContent(id = "busIcon")
                withStyle(
                    style = SpanStyle(
                        fontSize = 12.sp,
                    )
                ) {
                    if (!isLastStop) append(" at ") else append("at ")
                }
                withStyle(
                    style = SpanStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(destination)
                }
                if (!isLastStop) {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 10.sp,
                            color = MetadataGray
                        )
                    ) {
                        append("\n$numStops stops • $duration min")
                    }

                }
            }


        Column(
            Modifier
                .fillMaxHeight()
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = annotatedString,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .height(79.dp)
                        .wrapContentHeight(align = Alignment.CenterVertically)
                        .weight(1f),
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    inlineContent = inlineContent
                )
                if (directionType == DirectionType.DEPART && numStops > 1) {
                    if (expandedStops) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.baseline_expand_less),
                            contentDescription = "Expand less",
                            modifier = Modifier.height(79.dp),

                            )
                    } else {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.baseline_expand_more),
                            contentDescription = "Expand more",
                            modifier = Modifier.height(79.dp)
                        )
                    }
                }
            }
            if (!isFinalDestination) {
                Divider(color = IconGray, thickness = 1.dp)
            }
        }


    }
}

@Composable
fun StopItem(name: String, latitude: Double, longitude: Double) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(start = 12.dp, end = 12.dp)
            .height(80.dp)
    ) {

        Box(
            modifier = Modifier
                .width(72.dp)
                .padding(end = 8.dp)
        )

        // Direction Dot & Segments
        val segmentHeight = 32.dp
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(segmentHeight)
                    .background(TransitBlue)
            )

            Canvas(
                modifier = Modifier
                    .size(16.dp)
            ) {
                drawCircle(
                    color = TransitBlue
                )
            }

            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(segmentHeight)
                    .background(TransitBlue)
            )

        }

        Spacer(modifier = Modifier.width(20.dp))

        // Description
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Text(
                text = name,
                color = Color.Black,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .height(79.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Divider(color = IconGray, thickness = 1.dp)

        }
    }
}