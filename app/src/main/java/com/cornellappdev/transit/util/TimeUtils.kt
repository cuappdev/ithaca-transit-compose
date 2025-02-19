package com.cornellappdev.transit.util

import android.os.Build

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import android.icu.text.SimpleDateFormat
import java.time.Duration


/**
 * Extra utility functions for datetime
 */
object TimeUtils {

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

}