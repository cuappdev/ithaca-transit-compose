package com.cornellappdev.transit.ui.viewmodels

import android.util.Log
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.networking.ApiResponse
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

fun unwrapLocationState(locationState: LocationUIState): LatLng? {
    return when (locationState) {
        is LocationUIState.CurrentLocation -> {
            locationState.coordinates
        }

        is LocationUIState.Place -> {
            locationState.coordinates
        }
    }
}