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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuItem (label: String, sublabel: String) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
        Icon(Icons.Filled.Place, contentDescription = "Place", modifier = Modifier.padding(horizontal = 10.dp))
        Column () {
            Text(text = label, fontSize = 24.sp)
            Text(text = sublabel, fontSize = 16.sp)
        }
    }
}

@Preview
@Composable
fun PreviewMenuItem() {
    MenuItem("Ithaca Commons", "Ithaca, NY")
}