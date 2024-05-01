package com.cornellappdev.transit.ui.components

import androidx.compose.runtime.Composable
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline

/**
 * Polyline wrapper with Transit design changes
 */
@Composable
fun TransitPolyline(points : List<LatLng>){
    Polyline(
        points = points,
        color = TransitBlue,
    )
}