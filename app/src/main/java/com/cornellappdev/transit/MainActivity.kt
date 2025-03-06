package com.cornellappdev.transit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cornellappdev.transit.ui.NavigationController
import com.cornellappdev.transit.ui.screens.HomeScreen
import com.cornellappdev.transit.ui.screens.RouteScreen
import com.cornellappdev.transit.ui.theme.TransitTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TransitTheme(darkTheme = false) {
                NavigationController()
            }
        }
    }
}