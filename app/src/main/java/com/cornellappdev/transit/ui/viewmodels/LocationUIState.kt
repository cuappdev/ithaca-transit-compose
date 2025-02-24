package com.cornellappdev.transit.ui.viewmodels

import com.google.android.gms.maps.model.LatLng

/**
 * Wrapper for locations in Route Options
 */
sealed class LocationUIState {
    data object CurrentLocation :
        LocationUIState()

    data class Place(val name: String, val coordinates: LatLng) :
        LocationUIState()
}