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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.theme.robotoFamily

/**
 * Compose function to display a clickable settings option (displayed on the main settings page)
 * with a title, description, and an icon.
 * @param name The title of the settings option.
 * @param description The description of the settings option.
 * @param icon The resource ID of the icon to be displayed.
 * @param onClick The callback function to be executed when the option is clicked.
 * **/
@Composable
fun SettingsPageItem(name: String, description: String, icon: Int, onClick: () -> Unit) {
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
            contentDescription = null,
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
            )

            Text(
                text = description,
                fontSize = 14.sp,
                fontFamily = robotoFamily,
                color = Color.Gray,
            )
        }
    }
}

@Preview
@Composable
private fun SettingsPageItemPreview() {
    SettingsPageItem(
        name = "About Navi",
        description = "Learn more about the team behind the app",
        icon = R.drawable.appdev_gray,
        onClick = {})
}