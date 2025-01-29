package com.cornellappdev.transit.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Polyline

/**
 * Polyline wrapper with Transit design changes
 */
@Composable
fun TransitPolyline(points: List<LatLng>, color: Color) {
    Polyline(
        points = points,
        color = color,
    )
}