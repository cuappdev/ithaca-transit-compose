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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.sfProDisplayFamily


/**
 * Large icon badge for bus routes on details screen
 */
@Composable
fun BusIcon(busNumber: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(
                color = TransitBlue,
                shape = RoundedCornerShape(4.dp)
            )
            .width(72.dp)
            .height(36.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(
                horizontal = 8.dp,
                vertical = 8.dp
            )
        ) {

            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.bus),
                contentDescription = "Bus",
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = busNumber.toString(),
                fontFamily = sfProDisplayFamily,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )

        }
    }
}