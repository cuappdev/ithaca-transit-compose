package com.cornellappdev.transit.models.ecosystem

/**
 * Mapping of a day of the week to the hours for that day
 */
data class DayOperatingHours(
    val dayOfWeek: String,
    val hours: List<String>
)
