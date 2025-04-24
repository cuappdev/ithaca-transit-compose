package com.cornellappdev.transit.ui.screens.settings

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.cornellappdev.transit.ui.components.PrivacyOptionItem
import com.cornellappdev.transit.ui.components.SwitchItem
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.robotoFamily

/**
 * Composable function for the Privacy Settings screen, which displays a list of privacy settings
 * and links to privacy policy.
 */
@Composable
fun PrivacySettingsScreen() {
    val context = LocalContext.current

    var shareWithAppDev by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )
    {
        Text(
            text = "Privacy",
            fontSize = 32.sp,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp),
            fontWeight = FontWeight.Bold,
            fontFamily = robotoFamily,
            color = TransitBlue,
        )
        Text(
            text = "Manage permissions and analytics",
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 16.dp),
            fontFamily = robotoFamily,
            color = Color.Gray
        )
        Text(
            text = "Notifications",
            fontSize = 28.sp,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp),
            fontWeight = FontWeight.Bold,
            fontFamily = robotoFamily,
        )
        PrivacyOptionItem(
            text = "Location Access",
            subtext = "Used to find eateries near you",
            onClick = {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                context.startActivity(intent)
            },
            icon = Icons.Outlined.ArrowForward
        )
        PrivacyOptionItem(
            text = "Notification Access",
            subtext = "Used to send device notifications",
            onClick = {
                val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
                context.startActivity(intent)
            },
            icon = Icons.Outlined.ArrowForward
        )

        Text(
            text = "Legal",
            fontSize = 28.sp,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp),
            fontWeight = FontWeight.Bold,
            fontFamily = robotoFamily,
        )

        SwitchItem(
            "Share with Cornell AppDev",
            "Help us improve products and services",
            shareWithAppDev,
            { shareWithAppDev = it }
        )

        PrivacyOptionItem(
            text = "Privacy Policy",
            subtext = "",
            onClick = {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = "https://www.cornellappdev.com/privacy".toUri()
                }
                context.startActivity(intent)
            },
            icon = Icons.Outlined.ArrowForward
        )

    }

}


@Preview(showBackground = true)
@Composable
private fun PreviewPrivacySettingsScreen() {
    PrivacySettingsScreen()
}