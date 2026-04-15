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
fun HomeScreenMarkers(
    filterState: FilterState,
    favorites: Set<Place>,
    staticPlaces: StaticPlaces,
    onPlaceClick: (Place) -> Unit
) {

    when (filterState) {
        FilterState.FAVORITES -> {
            favorites.forEach {
                LocationMarker(
                    position = LatLng(it.latitude, it.longitude),
                    iconRes = R.drawable.favorite_pin,
                    onClick = { onPlaceClick(it) }
                )
            }
        }

        FilterState.PRINTERS -> {
            if (staticPlaces.printers is ApiResponse.Success) {
                staticPlaces.printers.data.forEach {
                    LocationMarker(
                        position = LatLng(it.latitude, it.longitude),
                        iconRes = R.drawable.printer_pin,
                        onClick = { onPlaceClick(it.toPlace()) }
                    )
                }
            }
        }

        FilterState.GYMS -> {
            if (staticPlaces.gyms is ApiResponse.Success) {
                staticPlaces.gyms.data.forEach {
                    LocationMarker(
                        position = LatLng(it.latitude, it.longitude),
                        iconRes = R.drawable.gym_pin,
                        onClick = { onPlaceClick(it.toPlace()) }
                    )
                }
            }
        }

        FilterState.EATERIES -> {
            if (staticPlaces.eateries is ApiResponse.Success) {
                staticPlaces.eateries.data.forEach { eatery ->
                    eatery.latitude?.let { latitude ->
                        eatery.longitude?.let { longitude ->
                            LocationMarker(
                                position = LatLng(latitude, longitude),
                                iconRes = R.drawable.eatery_pin,
                                onClick = { onPlaceClick(eatery.toPlace()) }
                            )
                        }
                    }
                }
            }
        }

        FilterState.LIBRARIES -> {
            if (staticPlaces.libraries is ApiResponse.Success) {
                staticPlaces.libraries.data.forEach {
                    LocationMarker(
                        position = LatLng(it.latitude, it.longitude),
                        iconRes = R.drawable.library_pin,
                        onClick = { onPlaceClick(it.toPlace()) }
                    )
                }
            }
        }
    }
}

@Composable
private fun LocationMarker(
    position: LatLng,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit
) {
    MarkerComposable(
        state = rememberMarkerState(
            position = position
        ),
        onClick = {
            onClick()
            true
        }
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}