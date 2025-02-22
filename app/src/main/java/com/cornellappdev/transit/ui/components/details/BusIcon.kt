package com.cornellappdev.transit.ui.components.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.robotoFamily

/**
 * Multi-size icon badge for bus routes on details screen
 *
 */
@Composable
fun BusIcon(busNumber: Int, modifier: Modifier = Modifier, isSmall: Boolean = false) {

    Row(
        modifier = modifier
            .background(
                color = TransitBlue,
                shape = RoundedCornerShape(4.dp)
            )
            .width(if (isSmall) 40.dp else 66.dp)
            .height(if (isSmall) 20.dp else 36.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(
                horizontal = if (isSmall) 2.dp else 8.dp,
                vertical = if (isSmall) 4.dp else 8.dp
            )
        ) {

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.bus),
                contentDescription = "Bus",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(if (isSmall) 14.dp else 24.dp)
                    .padding(start = 4.dp, end = if (isSmall) 3.dp else 6.dp)
            )
            Text(
                text = busNumber.toString(),
                fontFamily = robotoFamily,
                fontSize = (if (isSmall) 10.sp else 18.sp),
                fontWeight = if (isSmall) FontWeight.Normal else FontWeight.Bold,
                color = Color.White,
                style = TextStyle(platformStyle = PlatformTextStyle(includeFontPadding = false))
            )

        }
    }
}

@Preview
@Composable
private fun PreviewBusIconLarge() {
    BusIcon(30, isSmall = false)
}

@Preview
@Composable
private fun PreviewBusIconSmall() {
    BusIcon(30, isSmall = true)
}
