package com.cornellappdev.transit.models


enum class RouteOptionType {
    None,
    BoardingSoon,
    FromStop,
    Walking
}


data class MapState(
    val isShowing: Boolean,
    val routeOptionType: RouteOptionType,
    val route: Route?
)