package com.cornellappdev.transit.ui.components.home

import androidx.annotation.DrawableRes
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.ecosystem.StaticPlaces
import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.ui.viewmodels.FilterState
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMapComposable
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberMarkerState

/**
 * Set of pins displayed on the home screen map depending on the [filterState]
 */
@Composable
@GoogleMapComposable
fun HomeScreenMarkers(filterState: FilterState, favorites: Set<Place>, staticPlaces: StaticPlaces) {

    when (filterState) {
        FilterState.FAVORITES -> {
            favorites.forEach {
                LocationMarker(LatLng(it.latitude, it.longitude), R.drawable.favorite_pin)
            }
        }

        FilterState.PRINTERS -> {
            if (staticPlaces.printers is ApiResponse.Success) {
                staticPlaces.printers.data.forEach {
                    LocationMarker(LatLng(it.latitude, it.longitude), R.drawable.printer_pin)
                }
            }
        }

        FilterState.GYMS -> {
            if (staticPlaces.gyms is ApiResponse.Success) {
                staticPlaces.gyms.data.forEach {
                    LocationMarker(LatLng(it.latitude, it.longitude), R.drawable.gym_pin)
                }
            }
        }

        FilterState.EATERIES -> {
            if (staticPlaces.eateries is ApiResponse.Success) {
                staticPlaces.eateries.data.forEach { eatery ->
                    eatery.latitude?.let { latitude ->
                        eatery.longitude?.let { longitude ->
                            LocationMarker(LatLng(latitude, longitude), R.drawable.eatery_pin)
                        }
                    }
                }
            }
        }

        FilterState.LIBRARIES -> {
            if (staticPlaces.libraries is ApiResponse.Success) {
                staticPlaces.libraries.data.forEach {
                    LocationMarker(LatLng(it.latitude, it.longitude), R.drawable.library_pin)
                }
            }
        }
    }
}

@Composable
private fun LocationMarker(
    position: LatLng,
    @DrawableRes iconRes: Int
) {
    MarkerComposable(
        state = rememberMarkerState(
            position = position
        )
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}