package com.cornellappdev.transit.ui.viewmodels

/**
 * Class wrapping the pair of start location and end location for route searching
 */
data class SelectedRouteState(
    val startPlace: LocationUIState,
    val endPlace: LocationUIState
)