package com.cornellappdev.transit.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.PlaceType
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.Style
import com.cornellappdev.transit.ui.theme.sfProDisplayFamily

/**
 * Card for each entry in the search bar
 * @param icon The icon for the item
 * @param label The label for the item
 * @param sublabel The sublabel for the item
 */
@Composable
fun MenuItem(type: PlaceType, label: String, sublabel: String, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (type == PlaceType.APPLE_PLACE) {
            Image(
                painterResource(R.drawable.location_pin_gray),
                contentDescription = "Place",
                modifier = Modifier.padding(end = 20.dp),
            )
        } else {
            Image(
                painterResource(R.drawable.bus_stop_pin),
                contentDescription = "Stop",
                modifier = Modifier.padding(end = 20.dp),
            )
        }
        Column() {
            Text(
                text = label,
                color = PrimaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = Style.heading3
            )
            Text(
                text = sublabel,
                color = SecondaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = Style.paragraph
            )
        }
    }
}


@Preview
@Composable
fun PreviewMenuItem() {
    MenuItem(PlaceType.BUS_STOP, "Ithaca Commons", "Ithaca, NY", {})
}