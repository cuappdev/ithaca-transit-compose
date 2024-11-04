package com.cornellappdev.transit.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cornellappdev.transit.ui.screens.DetailsScreen
import com.cornellappdev.transit.ui.screens.HomeScreen
import com.cornellappdev.transit.ui.screens.RouteScreen
import com.cornellappdev.transit.ui.viewmodels.HomeViewModel
import com.cornellappdev.transit.ui.viewmodels.LocationUIState
import com.cornellappdev.transit.ui.viewmodels.RouteViewModel
import com.cornellappdev.transit.util.StringUtils.fromURLString
import com.google.android.gms.maps.model.LatLng
import kotlin.math.log

/**
 * The navigation controller for the app (parent of all screens)
 */
@RequiresApi(Build.VERSION_CODES.O)
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
        composable("route/{destination}/{latitude}/{longitude}") { backStackEntry ->
            val destArg = backStackEntry.arguments?.getString("destination")
            val latitudeArg = backStackEntry.arguments?.getString("latitude")
            val longitudeArg = backStackEntry.arguments?.getString("longitude")
            val currentLocation = routeViewModel.currentLocation.collectAsState().value
            if (destArg != null && latitudeArg != null && longitudeArg != null && currentLocation != null) {
                routeViewModel.changeStartLocation(
                    LocationUIState.CurrentLocation(
                        LatLng(currentLocation.latitude, currentLocation.longitude)
                    )
                )
                routeViewModel.changeEndLocation(
                    LocationUIState.Place(
                        destArg.fromURLString(),
                        LatLng(
                            latitudeArg.toDouble(),
                            longitudeArg.toDouble()
                        )
                    )
                )
            }
            RouteScreen(navController = navController, routeViewModel = routeViewModel)
        }
        composable("details") {
            DetailsScreen(navController = navController, homeViewModel = homeViewModel)
        }

        composable("route") {
            RouteScreen(navController = navController, routeViewModel = routeViewModel)
        }

    }
}
