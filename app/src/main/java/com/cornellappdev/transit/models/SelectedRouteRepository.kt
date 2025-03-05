package com.cornellappdev.transit.models

import com.cornellappdev.transit.ui.viewmodels.LocationUIState
import com.cornellappdev.transit.ui.viewmodels.SelectedRouteState
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectedRouteRepository @Inject constructor() {

    private val _selectedRoute: MutableStateFlow<SelectedRouteState> = MutableStateFlow(
        SelectedRouteState(
            LocationUIState.CurrentLocation,
            LocationUIState.CurrentLocation
        )
    )

    /**
     * The selected route parameters in route options
     */
    val selectedRoute = _selectedRoute.asStateFlow()

    /**
     * Change the start location in [_selectedRoute]
     * @param location The new starting location as a LocationUIState
     */
    fun setStartPlace(location: LocationUIState) {
        _selectedRoute.value = SelectedRouteState(
            location,
            _selectedRoute.value.endPlace
        )
    }

    /**
     * Change the destination location in [_selectedRoute]
     * @param location The new destination location as a LocationUIState
     */
    fun setDestPlace(location: LocationUIState) {
        _selectedRoute.value = SelectedRouteState(
            _selectedRoute.value.startPlace,
            location
        )
    }

    /**
     * Swap start and destination locations
     */
    fun swapPlaces() {
        _selectedRoute.value = SelectedRouteState(
            _selectedRoute.value.endPlace,
            _selectedRoute.value.startPlace
        )
    }

}