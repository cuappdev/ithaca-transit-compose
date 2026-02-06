package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.transit.models.ecosystem.DayOperatingHours
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.Style.heading3
import com.cornellappdev.transit.ui.theme.Style.heading3Semibold
import com.cornellappdev.transit.util.sampleHours

/**
 * Composable that displays operating hours for an eatery
 *
 * @param operatingHours List of associated dayOfWeek and hours pairs in the [DayOperatingHours] class
 * @param modifier Modifier for the component
 */
@Composable
fun OperatingHoursList(
    operatingHours: List<DayOperatingHours>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        operatingHours.forEachIndexed { index, (day, hours) ->
            OperatingHoursRow(
                day = day,
                hours = hours,
                index == 0
            )
        }
    }
}

/**
 * Single row displaying a day and its operating hours
 */
@Composable
private fun OperatingHoursRow(
    day: String,
    hours: List<String>,
    isHighlighted: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.weight(0.6f)
        ) {
            if (isHighlighted) {
                VerticalDivider(
                    color = PrimaryText,
                    thickness = 2.dp,
                    modifier = Modifier.height(16.dp)
                )
                Spacer(Modifier.width(12.dp))
            } else {
                Spacer(Modifier.width(14.dp))
            }

            Text(
                text = day,
                style = if (isHighlighted) heading3Semibold else heading3,
                color = if (isHighlighted) PrimaryText else SecondaryText,
            )
        }

        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.weight(0.4f)
        ) {
            hours.forEach { timeRange ->
                Text(
                    text = timeRange,
                    style = heading3,
                    color = SecondaryText,
                    modifier = Modifier.padding(bottom = 8.dp),
                    textAlign = TextAlign.Left
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OperatingHoursPreview() {

    OperatingHoursList(operatingHours = sampleHours)
}