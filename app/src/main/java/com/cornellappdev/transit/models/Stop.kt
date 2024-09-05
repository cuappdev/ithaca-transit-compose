package com.cornellappdev.transit.models

import com.squareup.moshi.Json

/**
 * Data class representing a TCAT stop
 */

enum class Type{
    BUSSTOP{

    },
    PLACE{
        
    }
}

data class Stop(
    @Json(name = "lat") var latitude: Double,
    @Json(name = "long") var longitude: Double,
    @Json(name = "name") var name: String,
    @Json(name = "type") var type: String
)
