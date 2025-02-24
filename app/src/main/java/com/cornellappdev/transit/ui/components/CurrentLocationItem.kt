package com.cornellappdev.transit.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.PlaceType
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.Style

/**
 * Card for the current location entry in the search bar
 */
@Composable
fun CurrentLocationItem(onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painterResource(R.drawable.blue_nav_arrow),
            contentDescription = "Current Location",
            modifier = Modifier.padding(end = 20.dp).size(14.dp),
        )
        Text(
            text = "Current Location",
            color = PrimaryText,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = Style.heading3
        )
    }
}


@Preview
@Composable
private fun PreviewCurrentLocationItem() {
    CurrentLocationItem {}
}