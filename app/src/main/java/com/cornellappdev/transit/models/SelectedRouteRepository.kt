package com.cornellappdev.transit.models

import com.cornellappdev.transit.ui.viewmodels.LocationUIState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectedRouteRepository @Inject constructor() {

    private val _startPlace: MutableStateFlow<LocationUIState> = MutableStateFlow(
        LocationUIState.CurrentLocation
    )

    private val _destPlace: MutableStateFlow<LocationUIState> = MutableStateFlow(
        LocationUIState.CurrentLocation
    )

    /**
     * Pair of the name of the starting location and the coordinates
     */
    val startPlace = _startPlace.asStateFlow()

    /**
     * Pair of the name of the ending location and the coordinates
     */
    val destPlace = _destPlace.asStateFlow()

    /**
     * Change the start location [_startPlace]
     * @param location The new starting location as a LocationUIState
     */
    fun setStartPlace(location: LocationUIState) {
        _startPlace.value = location
    }

    /**
     * Change the destination location [_destPlace]
     * @param location The new destination location as a LocationUIState
     */
    fun setDestPlace(location: LocationUIState) {
        _destPlace.value = location
    }

}