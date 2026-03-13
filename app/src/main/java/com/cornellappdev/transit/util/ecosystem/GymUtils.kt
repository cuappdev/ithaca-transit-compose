package com.cornellappdev.transit.util.ecosystem

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.models.ecosystem.UpliftCapacity
import com.cornellappdev.transit.ui.theme.AccentClosed
import com.cornellappdev.transit.ui.theme.AccentOpen
import com.cornellappdev.transit.ui.theme.AccentOrange
import com.cornellappdev.transit.ui.theme.robotoFamily
import com.cornellappdev.transit.util.HIGH_CAPACITY_THRESHOLD
import com.cornellappdev.transit.util.MEDIUM_CAPACITY_THRESHOLD

/**
 * Format percent string based on a gym's current capacity
 */
fun capacityPercentAnnotatedString(capacity: UpliftCapacity?): AnnotatedString {

    // Return empty string if no capacity data available
    if (capacity == null) {
        return AnnotatedString("")
    }

    val color = if (capacity.percent <= MEDIUM_CAPACITY_THRESHOLD) {
        AccentOpen
    } else if (capacity.percent >= HIGH_CAPACITY_THRESHOLD) {
        AccentClosed
    } else {
        AccentOrange
    }

    return buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                fontSize = 14.sp,
                fontFamily = robotoFamily,
                fontWeight = FontWeight(600),
                color = color,
            )
        ) {
            append("${capacity.percentString()} full")
        }
    }
}