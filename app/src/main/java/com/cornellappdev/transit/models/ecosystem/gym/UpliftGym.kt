package com.cornellappdev.transit.models.ecosystem.gym

import android.icu.util.Calendar
import kotlin.math.roundToInt

/**
 * A [UpliftGym] object represents all the information needed about one particular gym.
 */
data class UpliftGym(
    val name: String,
    /**
     * The gym id.
     */
    val id: String,
    /**
     * The fitness facility ID (see [id] for gym id).
     */
    val facilityId: String,
    /**
     * A list of exactly 7 lists of time intervals. Each list of time intervals corresponds to a particular
     * day (index 0=Monday, ..., 6=Sunday), and the times in said list indicates the hours of
     * this gym. Sorted in ascending order by time.
     *
     * Example: ((7:00 AM - 8:30 AM, 10:00AM - 10:45PM), ...) indicates that on Monday, this
     * gym is open from 7 to 8:30 AM, then closes, and then is open from 10 AM to 10:45 PM.
     * (These are real gym hours for Teagle Down!)
     *
     * If an index of this list is null, that indicates the gym is closed on that day.
     */
    val hours: List<List<TimeInterval>?>,

    /**
     * A list of exactly 7 [BowlingInfo] objects for each day, starting on Monday.
     *
     * If the list itself is null, that indicates bowling is not offered by this gym.
     */
    val imageUrl: String?,
    val upliftCapacity: UpliftCapacity?,
    val latitude: Double,
    val longitude: Double,
) {

}

/**
 * A gym's capacity.
 */
data class UpliftCapacity(
    /** The direct percent read from CFC. */
    val percent: Double,
    val updated: Calendar
) {
    /**
     * Returns a string representation of the percentage capacity.
     */
    fun percentString(): String {
        return "${(percent * 100).roundToInt()}%"
    }

    /**
     * Returns a string representing the time at which this capacity was last updated.
     * Of the form `"HH:MM AM"`
     */
    fun updatedString(): String {
        val timeOfDay = TimeOfDay(
            hour = updated.get(Calendar.HOUR),
            minute = updated.get(Calendar.MINUTE),
            isAM = updated.get(Calendar.AM_PM) == Calendar.AM
        )
        return "$timeOfDay".replace(" ", "")
    }
}