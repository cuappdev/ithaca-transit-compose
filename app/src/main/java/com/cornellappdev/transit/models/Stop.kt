package com.cornellappdev.transit.models

import com.squareup.moshi.Json

data class StopList (
    @Json(name = "success") var success: Boolean,
    @Json(name = "data") var stops : List<Stop>
)

data class Stop (
    @Json(name = "lat") var latitude: Double,
    @Json(name = "long") var departureLocationName: Double,
    @Json(name = "name") var name : String,
    @Json(name = "type") var type : String
)
