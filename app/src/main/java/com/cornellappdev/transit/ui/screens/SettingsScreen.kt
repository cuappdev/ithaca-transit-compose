package com.cornellappdev.transit.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.components.SettingsPageItem
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.robotoFamily
import com.cornellappdev.transit.util.NOTIFICATIONS_ENABLED

/**
 * Composable for Settings Screen, which displays a list of settings options and app information.
 * **/
@Composable
fun SettingsScreen(onSupportClick: () -> Unit,
                   onAboutClick: () -> Unit,
                   onNotificationsAndPrivacyClick: () -> Unit ){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    )
    {
        Text(
            text = "Settings",
            fontSize = 32.sp,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp),
            fontWeight = FontWeight.Bold,
            fontFamily = robotoFamily,
            color = TransitBlue,
        )

        SettingsPageItem(
            name = stringResource(R.string.about_text),
            description = "Learn more about the team behind the app",
            icon = R.drawable.appdev_gray,
            onClick = onAboutClick)
        HorizontalDivider(thickness = 0.5.dp)

        // Disable Notifications until implemented

        if (NOTIFICATIONS_ENABLED) {
            SettingsPageItem(
                name = "Notifications and Privacy",
                description = "Manage permissions and analytics",
                icon = R.drawable.lock,
                onClick = onNotificationsAndPrivacyClick
            )
            HorizontalDivider(thickness = 0.5.dp)
        }

        SettingsPageItem(
            name = "Support",
            description = "Report issues and contact us",
            icon = R.drawable.help_outline,
            onClick = onSupportClick
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.clock_tower),
            contentDescription = "Clock Tower",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .graphicsLayer(
                    scaleX = 1.8f,
                    scaleY = 1.8f,
                    transformOrigin = TransformOrigin(1f, 1f)
                )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen({}, {}, {})
}
