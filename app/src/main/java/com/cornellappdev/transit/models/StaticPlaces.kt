package com.cornellappdev.transit.models

import com.cornellappdev.transit.models.ecosystem.eatery.Eatery
import com.cornellappdev.transit.models.ecosystem.gym.UpliftGym
import com.cornellappdev.transit.networking.ApiResponse
import com.squareup.moshi.Json

/**
 * Class wrapping all static places
 */
data class StaticPlaces(
    val printers: ApiResponse<List<Printer>>,
    val libraries: ApiResponse<List<Library>>,
    val eateries: ApiResponse<List<Eatery>>,
    val gyms: ApiResponse<List<UpliftGym>>,
)

/**
 * Class representing a Cornell printer
 */
data class Printer(
    @Json(name = "id") var id: Int,
    @Json(name = "location") var location: String,
    @Json(name = "description") var description: String,
    @Json(name = "latitude") var latitude: Double,
    @Json(name = "longitude") var longitude: Double
)

/**
 * Class representing a Cornell library
 */
data class Library(
    @Json(name = "id") var id: Int,
    @Json(name = "location") var location: String,
    @Json(name = "address") var address: String,
    @Json(name = "latitude") var latitude: Double,
    @Json(name = "longitude") var longitude: Double
)
