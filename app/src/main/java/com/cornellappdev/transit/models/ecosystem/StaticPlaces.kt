package com.cornellappdev.transit.models.ecosystem

import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.PlaceType
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
) : EcosystemPlace {
    override fun toPlace(): Place = Place(
        latitude = this.latitude,
        longitude = this.longitude,
        name = this.location,
        detail = this.description,
        type = PlaceType.APPLE_PLACE
    )
}

/**
 * Class representing a Cornell library
 */
data class Library(
    @Json(name = "id") var id: Int,
    @Json(name = "location") var location: String,
    @Json(name = "address") var address: String,
    @Json(name = "latitude") var latitude: Double,
    @Json(name = "longitude") var longitude: Double
) : DetailedEcosystemPlace {

    override fun operatingHours(): List<DayOperatingHours> {
        //TODO: Implement
        return emptyList()
    }

    override fun toPlace(): Place = Place(
        latitude = this.latitude,
        longitude = this.longitude,
        name = this.location,
        detail = this.address,
        type = PlaceType.APPLE_PLACE
    )
}
