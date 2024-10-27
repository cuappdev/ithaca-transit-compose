package com.cornellappdev.transit.ui.viewmodels

import com.google.android.gms.maps.model.LatLng

/**
 * Wrapper for locations in Route Options
 */
sealed class LocationUIState {
    data class CurrentLocation(val coordinates: LatLng?) :
        LocationUIState()

    data class Place(val name: String, val coordinates: LatLng) :
        LocationUIState()
}

/**
 * Retrieve the coordinates from a LocationUIState regardless of type
 */
fun getCoordinatesFromLocationState(locationState: LocationUIState): LatLng? {
    return when (locationState) {
        is LocationUIState.CurrentLocation -> {
            locationState.coordinates
        }

        is LocationUIState.Place -> {
            locationState.coordinates
        }
    }
}