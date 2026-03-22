package com.cornellappdev.transit.networking

import com.google.android.gms.maps.model.LatLng
import com.squareup.moshi.FromJson
import com.squareup.moshi.Json
import com.squareup.moshi.ToJson
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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

class DateTimeAdapter {
    @ToJson
    fun toJson(dateTime: LocalDateTime): String {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"))
    }

    @FromJson
    fun fromJson(dateTime: String): LocalDateTime {
        return try {
            val instant = Instant.parse(dateTime)
            LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        } catch (_: Exception) {
            try {
                LocalDateTime.parse(dateTime)
            } catch (_: Exception) {
                LocalDateTime.MIN
            }
        }
    }
}