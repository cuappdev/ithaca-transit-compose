package com.cornellappdev.transit.util.ecosystem

import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.PlaceType
import com.cornellappdev.transit.models.ecosystem.Library
import com.cornellappdev.transit.models.ecosystem.Printer


/**
 * Predefined mapping from library to generic place
 */
fun Library.toPlace(): Place = Place(
    latitude = this.latitude,
    longitude = this.longitude,
    name = this.location,
    detail = this.address,
    type = PlaceType.APPLE_PLACE
)

/**
 * Predefined mapping from printer to generic place
 */
fun Printer.toPlace(): Place = Place(
    latitude = this.latitude,
    longitude = this.longitude,
    name = this.location,
    detail = this.description,
    type = PlaceType.APPLE_PLACE
)
