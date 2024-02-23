package com.cornellappdev.transit.ui.components


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Card for each entry in the search bar
 * @param icon The icon for the item
 * @param label The label for the item
 * @param sublabel The sublabel for the item
 */
@Composable
fun MenuItem(icon: ImageVector, label: String, sublabel: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            icon,
            contentDescription = "Place",
            modifier = Modifier.padding(horizontal = 10.dp)
        )
        Column() {
            Text(text = label, fontSize = 24.sp)
            Text(text = sublabel, fontSize = 16.sp)
        }
    }
}


@Preview
@Composable
fun PreviewMenuItem() {
    MenuItem(Icons.Filled.Place, "Ithaca Commons", "Ithaca, NY")
}