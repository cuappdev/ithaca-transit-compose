package com.cornellappdev.transit.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cornellappdev.transit.ui.screens.HomeScreen
import com.cornellappdev.transit.ui.screens.RouteScreen
import com.cornellappdev.transit.ui.viewmodels.HomeViewModel
import com.cornellappdev.transit.ui.viewmodels.RouteViewModel

/**
 * The navigation controller for the app (parent of all screens)
 */
//TODO: make navContoller accept argument (start + dest) by editing the navHost
@Composable
fun NavigationController(
    homeViewModel: HomeViewModel = hiltViewModel(),
    routeViewModel : RouteViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home",
    ) {
        composable("home") {
            HomeScreen(homeViewModel = homeViewModel, navController = navController)
        }
        composable("route") {
            RouteScreen(navController = navController, routeViewModel = routeViewModel)
        }

    }
}