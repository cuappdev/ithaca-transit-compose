package com.cornellappdev.transit.util.ecosystem

import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.PlaceType
import com.cornellappdev.transit.models.ecosystem.Eatery
import com.cornellappdev.transit.models.ecosystem.Library
import com.cornellappdev.transit.models.ecosystem.Printer
import com.cornellappdev.transit.models.ecosystem.UpliftGym


/**
 * Predefined mapping from library to place
 */
fun Library.toPlace(): Place = Place(
    latitude = this.latitude,
    longitude = this.longitude,
    name = this.location,
    detail = this.address,
    type = PlaceType.LIBRARY
)

/**
 * Predefined mapping from printer to place
 */
fun Printer.toPlace(): Place = Place(
    latitude = this.latitude,
    longitude = this.longitude,
    name = this.location,
    detail = this.description,
    type = PlaceType.PRINTER
)

/**
 * Predefined mapping from eatery to place. Nullable latitudes and longitudes default to 0
 */
fun Eatery.toPlace(): Place = Place(
    latitude = this.latitude ?: 0.0,
    longitude = this.longitude ?: 0.0,
    name = this.name,
    detail = this.location,
    type = PlaceType.EATERY
)

/**
 * Predefined mapping from gym to place.
 */
fun UpliftGym.toPlace(): Place = Place(
    latitude = this.latitude,
    longitude = this.longitude,
    name = this.name,
    detail = this.id,
    type = PlaceType.GYM
)
