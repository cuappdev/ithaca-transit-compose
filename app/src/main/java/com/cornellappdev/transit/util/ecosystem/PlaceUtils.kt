package com.cornellappdev.transit.util.ecosystem

import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.PlaceType
import com.cornellappdev.transit.models.ecosystem.Eatery
import com.cornellappdev.transit.models.ecosystem.Library
import com.cornellappdev.transit.models.ecosystem.Printer
import com.cornellappdev.transit.models.ecosystem.UpliftGym
import com.cornellappdev.transit.util.getGymLocationString


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

/**
 * Predefined mapping from eatery to generic place. Nullable latitudes and longitudes default to 0
 */
fun Eatery.toPlace(): Place = Place(
    latitude = this.latitude ?: 0.0,
    longitude = this.longitude ?: 0.0,
    name = this.name,
    detail = this.location,
    type = PlaceType.APPLE_PLACE
)

/**
 * Predefined mapping from gym to generic place.
 */
fun UpliftGym.toPlace(): Place = Place(
    latitude = this.latitude,
    longitude = this.longitude,
    name = this.name,
    detail = getGymLocationString(this.name),
    type = PlaceType.APPLE_PLACE
)
