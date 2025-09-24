package com.cornellappdev.transit.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cornellappdev.transit.ui.screens.DetailsScreen
import com.cornellappdev.transit.ui.screens.HomeScreen
import com.cornellappdev.transit.ui.screens.RouteScreen
import com.cornellappdev.transit.ui.screens.SettingsScreen
import com.cornellappdev.transit.ui.screens.settings.AboutScreen
import com.cornellappdev.transit.ui.screens.settings.FavoritesScreen
import com.cornellappdev.transit.ui.screens.settings.NotifsAndPrivacyScreen
import com.cornellappdev.transit.ui.screens.settings.PrivacySettingsScreen
import com.cornellappdev.transit.ui.screens.settings.SupportScreen
import com.cornellappdev.transit.ui.viewmodels.HomeViewModel
import com.cornellappdev.transit.ui.viewmodels.RouteViewModel

/**
 * The navigation controller for the app (parent of all screens)
 */
@Composable
fun NavigationController(
    homeViewModel: HomeViewModel = hiltViewModel(),
    routeViewModel: RouteViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",
    ) {
        composable("home") {
            HomeScreen(homeViewModel = homeViewModel, navController = navController)
        }

        composable("details") {
            DetailsScreen(
                navController = navController,
                routeViewModel = routeViewModel,
            )
        }

        composable("route") {
            RouteScreen(navController = navController, routeViewModel = routeViewModel)
        }

        composable("settings") {
            SettingsScreen (
                onAboutClick = {navController.navigate("about")},
                onSupportClick = {navController.navigate("support")},
                onNotificationsAndPrivacyClick = {navController.navigate("notifs_privacy")})
        }

        composable("about") {
            AboutScreen()
        }

        composable("notifs_privacy") {
            NotifsAndPrivacyScreen (
                onPrivacyClick = {navController.navigate("privacy_settings")}
            )
        }

        composable("privacy_settings") {
            PrivacySettingsScreen()
        }

        composable("favorites") {
            FavoritesScreen()
        }

        composable("support") {
            SupportScreen()
        }
    }
}
