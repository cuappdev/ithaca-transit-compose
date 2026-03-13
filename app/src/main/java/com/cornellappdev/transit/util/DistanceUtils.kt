package com.cornellappdev.transit.util

import android.location.Location
import com.cornellappdev.transit.util.StringUtils.fromMetersToMiles
import com.google.android.gms.maps.model.LatLng


/**
 * Returns distance string in miles
 */
fun calculateDistanceString(
    from: LatLng,
    to: LatLng,
) : String {
    val results = FloatArray(1)
    Location.distanceBetween(from.latitude, from.longitude, to.latitude, to.longitude, results)

    return results[0].toString().fromMetersToMiles() + " mi"
}