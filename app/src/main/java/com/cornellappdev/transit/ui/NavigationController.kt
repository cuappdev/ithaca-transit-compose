package com.cornellappdev.transit.ui

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cornellappdev.transit.ui.screens.DetailsScreen
import com.cornellappdev.transit.ui.screens.HomeScreen
import com.cornellappdev.transit.ui.screens.RouteScreen
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
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(homeViewModel = homeViewModel, navController = navController)
        }
        composable("route/{destination}") { backStackEntry ->
            val destArg = backStackEntry.arguments?.getString("destination")
            if (destArg != null) {
                routeViewModel.changeEndLocation(destArg)
            }
            RouteScreen(navController = navController, routeViewModel = routeViewModel)
        }
        composable("details") {
            DetailsScreen(navController = navController, homeViewModel = homeViewModel)
        }

    }
}