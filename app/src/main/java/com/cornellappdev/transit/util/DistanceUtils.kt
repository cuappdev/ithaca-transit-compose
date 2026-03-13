package com.cornellappdev.transit.util

import android.location.Location
import com.cornellappdev.transit.util.StringUtils.fromMetersToMiles
import com.google.android.gms.maps.model.LatLng

/**
 * Returns distance in miles
 */
fun calculateDistance(
    from: LatLng,
    to: LatLng
): Double {
    val results = FloatArray(1)
    Location.distanceBetween(from.latitude, from.longitude, to.latitude, to.longitude, results)

    return results[0].toDouble()
}