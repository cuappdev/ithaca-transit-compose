package com.cornellappdev.transit.util

import android.icu.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale


/**
 * Extra utility functions for datetime
 */
object TimeUtils {

    /**
     * Value to represent the custom order of days in a week (with Sunday as
     * the first day). Sunday first indexing courtesy of Eatery
     */
    val dayOrder = mapOf(
        "Sunday" to 0,
        "Monday" to 1,
        "Tuesday" to 2,
        "Wednesday" to 3,
        "Thursday" to 4,
        "Friday" to 5,
        "Saturday" to 6
    )

    /**
     * Mapping from order to day
     */
    val dayString = mapOf(
        0 to "Sunday",
        1 to "Monday",
        2 to "Tuesday",
        3 to "Wednesday",
        4 to "Thursday",
        5 to "Friday",
        6 to "Saturday"
    )

    /**
     * Formatter for date and time. Ex: Mar 17, 1998 4:42 AM
     */
    val dateTimeFormatter =
        SimpleDateFormat("MMM d, yyyy h:mm aa", Locale.US)

    /**
     * Formatter for time only. Ex: 4:42 AM
     */
    val timeFormatter =
        SimpleDateFormat("h:mm aa", Locale.US)

    /**
     * Formatter for time only. Ex: Mar 17, 1998
     */
    val dateFormatter =
        SimpleDateFormat("MMM d, yyyy", Locale.US)


    /**
     * Convert ISO datetime string to hours, minutes, and AM/PM in format HH:MM AM
     */
    fun getHHMM(isoString: String): String {

        // Parse the ISO string to Instant, then convert to the given timezone
        val instant = Instant.parse(isoString)
        val zonedDateTime = instant.atZone(ZoneId.of("America/New_York"))

        // Format the ZonedDateTime to AM/PM
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")
        return zonedDateTime.format(formatter)
    }

    /**
     * Calculate the minute difference between two ISO datetime strings. Returns a negative
     * number if startString is more recent than endString
     */
    fun minuteDifference(startString: String, endString: String): Int {
        val start = Instant.parse(startString)
        val end = Instant.parse(endString)

        val duration = Duration.between(start, end)

        return duration.toMinutes().toInt()
    }

    /**
     * Return a string in the format "X d Y hr Z min" representing the day, hour, and minute difference between
     * two ISO datetime strings. Returns absolute value difference.
     */
    fun dayHourMinuteDifference(startString: String, endString: String): String {
        val start = Instant.parse(startString)
        val end = Instant.parse(endString)

        val duration = Duration.between(start, end).abs()

        val days = duration.toDays()
        val hours = (duration.toHours() % 24)
        val minutes = (duration.toMinutes() % 60)

        var diffString = ""

        if (days > 0) {
            diffString += "$days d "
        }
        if (hours > 0) {
            diffString += "$hours hr "
        }
        diffString += "$minutes min"
        return diffString
    }

    /**
     * Return an ISO string representing an integer amount of minutes added t
     * to an original date [isoString]
     */
    fun addSecondsToTime(isoString: String, seconds: Int): String {
        return Instant.parse(isoString).plusSeconds(seconds.toLong()).toString()
    }

    /**
     * Day of week with first letter capitalized and all else lowercase
     */
    fun DayOfWeek.toPascalCaseString(): String = this.name.lowercase()
        .replaceFirstChar { it.uppercase() }


}