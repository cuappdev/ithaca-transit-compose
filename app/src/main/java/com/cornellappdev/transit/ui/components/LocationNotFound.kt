package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.transit.ui.theme.robotoFamily

@Composable
fun LocationNotFound() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Rounded.Place,
            contentDescription = "",
            tint = Color.Gray,
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.CenterHorizontally)
        )
        Text(
            text = "Location Not Found",
            fontFamily = robotoFamily,
            fontStyle = FontStyle.Normal,
            color = Color.Gray
        )

    }
}

@Preview
@Composable
private fun LocationNotFoundPreview() {
    LocationNotFound()
}