package com.cornellappdev.transit.networking

import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.FromJson
import com.squareup.moshi.Json

class LatLngAdapter {

    /**
     * Custom class for parsing world coordinates
     */
    data class Coordinate(
        @Json(name = "lat") val latitude: Double,
        @Json(name = "long") val longitude: Double,
    )

    /**
     * Convert from custom Coordinate class to LatLng class from Google GMS API
     */
    @FromJson
    fun fromJson(coord: Coordinate): LatLng {
        return LatLng(coord.latitude, coord.longitude)
    }
}