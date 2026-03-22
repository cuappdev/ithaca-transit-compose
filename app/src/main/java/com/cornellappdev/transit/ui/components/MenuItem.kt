package com.cornellappdev.transit.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.PlaceType
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.Style

/**
 * Card for each entry in the search bar
 * @param type The place type used to resolve icon
 * @param label The label for the item
 * @param sublabel The sublabel for the item
 */
@Composable
fun MenuItem(
    type: PlaceType,
    label: String,
    sublabel: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayedSublabel = ecosystemSublabelFor(type) ?: sublabel

    Row(
        modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 36.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(iconForPlaceType(type)),
            contentDescription = null,
            modifier = Modifier
                .size(24.dp),
        )
        Spacer(modifier = Modifier.size(12.dp))
        Column() {
            Text(
                text = label,
                color = PrimaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = Style.heading3
            )
            Text(
                text = displayedSublabel,
                color = SecondaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = Style.paragraph
            )
        }
    }
}

private fun ecosystemSublabelFor(type: PlaceType): String? = when (type) {
    PlaceType.EATERY -> "Eatery"
    PlaceType.LIBRARY -> "Library"
    PlaceType.PRINTER -> "Printer"
    PlaceType.GYM -> "Gym"
    else -> null
}

private val searchIconByPlaceType = mapOf(
    PlaceType.BUS_STOP to R.drawable.bus_stop_pin,
    PlaceType.APPLE_PLACE to R.drawable.location_pin_gray,
    PlaceType.EATERY to R.drawable.eatery_icon,
    PlaceType.LIBRARY to R.drawable.library_icon,
    PlaceType.GYM to R.drawable.gym_icon,
    PlaceType.PRINTER to R.drawable.printer_icon
)

@DrawableRes
private fun iconForPlaceType(type: PlaceType): Int {
    return searchIconByPlaceType[type] ?: R.drawable.location_pin_gray
}

@Preview(showBackground = true)
@Composable
private fun PreviewMenuItemBusStop() {
    MenuItem(PlaceType.BUS_STOP, "Ithaca Commons", "Ithaca, NY", {})
}

@Preview(showBackground = true)
@Composable
private fun PreviewMenuItemApplePlace() {
    MenuItem(PlaceType.APPLE_PLACE, "Apple Place", "Ithaca, NY", {})
}

@Preview(showBackground = true)
@Composable
private fun PreviewMenuItemEatery() {
    MenuItem(PlaceType.EATERY, "Eatery", "Ithaca, NY", {})
}

@Preview(showBackground = true)
@Composable
private fun PreviewMenuItemGym() {
    MenuItem(PlaceType.GYM, "Gym", "Ithaca, NY", {})
}

@Preview(showBackground = true)
@Composable
private fun PreviewMenuItemLibrary() {
    MenuItem(PlaceType.LIBRARY, "Library", "Ithaca, NY", {})
}

@Preview(showBackground = true)
@Composable
private fun PreviewMenuItemPrinter() {
    MenuItem(PlaceType.PRINTER, "Printer", "Ithaca, NY", {})
}
