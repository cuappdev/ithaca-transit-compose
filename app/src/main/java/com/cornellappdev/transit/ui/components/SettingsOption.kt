package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.theme.robotoFamily

@Composable
fun SettingsOption(name: String, description: String, icon: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(top = 8.dp, bottom = 8.dp)
            .fillMaxWidth()
            .clickable {
                onClick()
            },
    )
    {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "",
            tint = Color.Gray,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
                .size(36.dp)
                .align(Alignment.CenterVertically)
        )

        Column(modifier = Modifier.align(Alignment.CenterVertically)) {

            Text(
                text = name,
                fontSize = 20.sp,
                fontFamily = robotoFamily,
                fontStyle = FontStyle.Normal,
            )

            Text(
                text = description,
                fontSize = 14.sp,
                fontFamily = robotoFamily,
                color = Color.Gray,
                fontStyle = FontStyle.Normal,
            )
        }
    }
}

@Preview
@Composable
private fun SettingsOptionPreview() {
    SettingsOption(
        name = "About Transit",
        description = "Learn more about the team behind the app",
        icon = R.drawable.appdev_gray,
        onClick = {})
}