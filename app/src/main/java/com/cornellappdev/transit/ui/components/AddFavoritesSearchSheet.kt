package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.cornellappdev.transit.ui.theme.sfProDisplayFamily

//TODO: add spec
@Composable
fun AddFavoritesSearchSheet () {
    Column() {
        Row{
            Text(
                text = "Add Favorite",
                fontWeight = FontWeight(600),
                fontSize = 20.sp,
                fontFamily = sfProDisplayFamily,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Cancel",
                fontWeight = FontWeight(300),
                fontSize = 20.sp,
                fontFamily = sfProDisplayFamily,
                textAlign = TextAlign.Right
            )
        }

    }
}

@Preview
@Composable
fun PreviewAddFavScreen() {
    AddFavoritesSearchSheet()
}