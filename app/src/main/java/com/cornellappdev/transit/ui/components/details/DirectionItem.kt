package com.cornellappdev.transit.ui.components.details

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.DirectionType
import com.cornellappdev.transit.ui.theme.DetailsDividerGray
import com.cornellappdev.transit.ui.theme.IconGray
import com.cornellappdev.transit.ui.theme.MetadataGray
import com.cornellappdev.transit.ui.theme.Style.heading4
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.robotoFamily

/**
 * Composable representing a direction item, one component of a Route
 */
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
    colorAbove: Color = MetadataGray,
    colorBelow: Color = MetadataGray,
    delayedTime: String? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(start = 12.dp, end = 12.dp)
            .height(80.dp)
    ) {
        // Time Text
        Column(verticalArrangement = Arrangement.Center) {
            Text(
                text = time,
                color = Color.Black,
                modifier = Modifier
                    .width(72.dp)
                    .padding(end = 8.dp),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontFamily = robotoFamily,
                    lineHeight = 14.sp,
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    ),
                    textDecoration = if (delayedTime != null) TextDecoration.LineThrough else TextDecoration.None
                )
            )
            if (delayedTime != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = delayedTime,
                    color = Color.Red,
                    modifier = Modifier
                        .width(72.dp)
                        .padding(end = 8.dp),
                    style = heading4
                )
            }
        }

        // Direction Dot & Segments
        val segmentHeight = 32.dp
        DotAndSegments(
            segmentHeight,
            drawSegmentAbove,
            colorAbove,
            isFinalDestination,
            directionType,
            drawSegmentBelow,
            colorBelow
        )

        Spacer(modifier = Modifier.width(20.dp))

        // Description
        val inlineContent = if (directionType == DirectionType.DEPART) {
            mapOf(
                "busIcon" to InlineTextContent(
                    placeholder = Placeholder(
                        width = 40.sp,
                        height = 20.sp,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                    )
                ) {
                    BusIcon(busNumber.toInt(), isSmall = true)
                }
            )
        } else {
            emptyMap()
        }

        val annotatedString = buildDirectionAnnotatedString(
            directionType,
            movementDescription,
            destination,
            isLastStop,
            numStops,
            duration
        )


        StopDescription(
            annotatedString,
            inlineContent,
            directionType,
            numStops,
            expandedStops,
            isFinalDestination
        )


    }
}

/**
 * Middle section of Direction item, with segments connecting above and below and a centered dot
 */
@Composable
private fun DotAndSegments(
    segmentHeight: Dp,
    drawSegmentAbove: Boolean,
    colorAbove: Color,
    isFinalDestination: Boolean,
    directionType: DirectionType,
    drawSegmentBelow: Boolean,
    colorBelow: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .width(3.dp)
                .height(segmentHeight)
                .background(if (drawSegmentAbove) colorAbove else Color.Transparent)
        )

        if (isFinalDestination) {
            Icon(
                imageVector = ImageVector.vectorResource(if (directionType == DirectionType.DEPART) R.drawable.bus_destination else R.drawable.destination_stop),
                contentDescription = "Destination",
                tint = Color.Unspecified,
                modifier = Modifier.size(16.dp)
            )
        } else {
            Canvas(
                modifier = Modifier
                    .size(16.dp)
            ) {
                drawCircle(
                    color = if (directionType == DirectionType.DEPART) TransitBlue else MetadataGray
                )
            }
        }

        Box(
            modifier = Modifier
                .width(3.dp)
                .height(segmentHeight)
                .background(if (drawSegmentBelow) colorBelow else Color.Transparent)
        )

    }
}

/**
 * Right side of the Direction item, with annotated text
 */
@Composable
private fun StopDescription(
    annotatedString: AnnotatedString,
    inlineContent: Map<String, InlineTextContent>,
    directionType: DirectionType,
    numStops: Int,
    expandedStops: Boolean,
    isFinalDestination: Boolean
) {
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
            Divider(color = DetailsDividerGray, thickness = 1.dp)
        }
    }
}

/**
 * Composable for intermediate stops
 */
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
                    .width(3.dp)
                    .height(segmentHeight)
                    .background(TransitBlue)
            )

            Canvas(
                modifier = Modifier
                    .size(16.dp)
            ) {
                drawCircle(
                    color = TransitBlue,
                    style = Stroke(
                        width = 8f
                    )

                )

            }

            Box(
                modifier = Modifier
                    .width(3.dp)
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
                text = "    $name",
                color = Color.Black,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .height(79.dp)
                    .wrapContentHeight(align = Alignment.CenterVertically),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Divider(color = DetailsDividerGray, thickness = 1.dp)

        }
    }
}

private fun buildDirectionAnnotatedString(
    directionType: DirectionType,
    movementDescription: String,
    destination: String,
    isLastStop: Boolean,
    numStops: Int,
    duration: Int
): AnnotatedString {
    return if (directionType == DirectionType.WALK) {
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
        }
    } else {
        buildAnnotatedString {
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
    }

}

@Preview
@Composable
private fun PreviewDirectionItem() {
    DirectionItem(
        time = "12:51 PM",
        movementDescription = "Board",
        destination = "Goldwin Smith Hall",
        directionType = DirectionType.DEPART,
        drawSegmentAbove = true,
        drawSegmentBelow = true,
        isFinalDestination = false,
        busNumber = "30",
        isLastStop = false,
        numStops = 3,
        duration = 5,
        expandedStops = false,
        colorAbove = TransitBlue,
        colorBelow = TransitBlue
    )
}

@Preview
@Composable
private fun PreviewStopItem() {
    StopItem(
        name = "Risley Hall",
        latitude = 0.00,
        longitude = 0.00
    )
}