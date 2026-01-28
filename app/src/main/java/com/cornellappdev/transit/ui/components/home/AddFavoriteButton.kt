package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AddFavoriteButton(
    onAddFavoriteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onAddFavoriteClick,
        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Text("Add Favorite")
    }
}

@Preview
@Composable
fun AddFavoriteButtonPreview() {
    AddFavoriteButton(
        onAddFavoriteClick = {},
    )
}