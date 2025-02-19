package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.ui.theme.robotoFamily

@Composable
fun SettingsOption(name: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
            .clickable {
                onClick()
            },
    )
    {
        Text(
            text = name,
            fontSize = 24.sp,
            fontFamily = robotoFamily,
            fontStyle = FontStyle.Normal,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview
@Composable
fun SettingsOptionPreview() {
    SettingsOption(name = "Privacy Policy", onClick = {})
}