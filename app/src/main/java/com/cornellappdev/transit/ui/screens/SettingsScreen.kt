package com.cornellappdev.transit.ui.screens

import android.content.Context
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.components.SettingsOption
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.robotoFamily

/**
 * Composable for Settings Screen, which displays a list of settings options and app information.
 * **/
@Composable
fun SettingsScreen(context: Context, navController: NavController) {
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
            fontStyle = FontStyle.Normal,
            color = TransitBlue,
        )

        SettingsOption(
            name = "About Transit",
            description = "Learn more about the team behind the app",
            icon = R.drawable.appdev_gray,
            onClick = { navController.navigate("about") })

        //TODO: Will be implemented after rebranding ecosystem is done
        /*HorizontalDivider(thickness = 0.5.dp)

        SettingsOption(
            name = "Show Onboarding",
            description = "Need a refresher? See how to use the app",
            icon = R.drawable.lightbulb,
            onClick = {})

        HorizontalDivider(thickness = 0.5.dp)

        SettingsOption(
            name = "Favorites",
            description = "Manage your favorite stops",
            icon = R.drawable.favorites,
            onClick = {})

        HorizontalDivider(thickness = 0.5.dp)

        SettingsOption(
            name = "App Icon",
            description = "Choose your adventure",
            icon = R.drawable.bus,
            onClick = {})*/

        HorizontalDivider(thickness = 0.5.dp)

        SettingsOption(
            name = "Notifications and Privacy",
            description = "Manage permissions and analytics",
            icon = R.drawable.lock,
            onClick = {
                navController.navigate("notifs_privacy")
            }
        )

        HorizontalDivider(thickness = 0.5.dp)

        SettingsOption(
            name = "Support",
            description = "Report issues and contact us",
            icon = R.drawable.help_outline,
            onClick = { navController.navigate("support") },
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
    SettingsScreen(
        context = LocalContext.current,
        navController = NavController(LocalContext.current)
    )
}
