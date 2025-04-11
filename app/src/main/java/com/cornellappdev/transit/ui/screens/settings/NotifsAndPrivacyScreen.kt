package com.cornellappdev.transit.ui.screens.settings

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.ui.components.PrivacyItem
import com.cornellappdev.transit.ui.components.SwitchItem
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.robotoFamily

@Composable
fun NotifsAndPrivacyScreen(context: Context) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    )
    {
        Text(
            text = "Notifications & Privacy",
            fontSize = 32.sp,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp),
            fontWeight = FontWeight.Bold,
            fontFamily = robotoFamily,
            fontStyle = FontStyle.Normal,
            color = TransitBlue,
        )

        Text(
            text = "Notifications",
            fontSize = 28.sp,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp),
            fontWeight = FontWeight.Bold,
            fontFamily = robotoFamily,
            fontStyle = FontStyle.Normal,
        )

        SwitchItem(
            text = "Pause all notifications",
            subtext = "",
            onclick = {}
        )

        SwitchItem(
            text = "Bus Notifications",
            subtext = "Get notified when a bus is arriving",
            onclick = {}
        )

        SwitchItem(
            text = "Cornell AppDev Notifications",
            subtext = "Get notified about new releases and feedback",
            onclick = {}
        )

        SwitchItem(
            text = "Account Notifications",
            subtext = "Get notified about account security and privacy",
            onclick = {}
        )

        PrivacyItem(
            text = "Privacy Settings",
            subtext = "",
            onclick = {},
            icon = Icons.Outlined.KeyboardArrowRight
        )

        Text(
            text = "Legal",
            fontSize = 28.sp,
            modifier = Modifier.padding(top = 16.dp, start = 16.dp),
            fontWeight = FontWeight.Bold,
            fontFamily = robotoFamily,
            fontStyle = FontStyle.Normal,
        )


        PrivacyItem(
            text = "Terms and Conditions",
            subtext = "",
            onclick = {},
            icon = Icons.Outlined.ArrowForward
        )

        PrivacyItem(
            text = "Privacy Policy",
            subtext = "",
            onclick = {},
            icon = Icons.Outlined.ArrowForward
        )

    }

}



@Preview(showBackground = true)
@Composable
private fun NotifsAndPrivacyScreenPreview() {
    NotifsAndPrivacyScreen(context = LocalContext.current)
}