package com.cornellappdev.transit.util

import android.icu.text.SimpleDateFormat
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import com.cornellappdev.transit.models.ecosystem.DayOperatingHours
import com.cornellappdev.transit.ui.theme.LateRed
import com.cornellappdev.transit.ui.theme.LiveGreen
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.viewmodels.OpenStatus
import java.time.DayOfWeek
import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
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


    /**
     * Rotate operating hours such that first value is today's date
     *
     * @param operatingHours A list of pairs mapping the first value day string to second value list of hours open
     */
    fun rotateOperatingHours(
        operatingHours: List<DayOperatingHours>,
        currentDate: LocalDate = LocalDate.now()
    ): List<DayOperatingHours> {
        val today = currentDate.dayOfWeek.toPascalCaseString()

        val todayIndex = operatingHours.indexOfFirst {
            it.dayOfWeek.equals(today, ignoreCase = true)
        }

        // Defensive programming only if [operatingHours] is missing a day
        if (todayIndex == -1) return operatingHours

        return operatingHours.drop(todayIndex) + operatingHours.take(todayIndex)
    }

    /**
     * Find the next time a place is open if it is closed for the day
     */
    private fun findOpenNextDay(operatingHours: List<DayOperatingHours>): OpenStatus {
        // Check day after
        val dayAfter = operatingHours[1].hours
        if (!dayAfter.any { it.equals("Closed", ignoreCase = true) }) {
            val firstOpenTime = parseTimeRange(dayAfter[0])?.first
            if (firstOpenTime != null) {
                return OpenStatus(
                    false,
                    "until ${formatTime(firstOpenTime)}"
                )
            }
        }
        // Find next open day
        for (i in 2 until operatingHours.size) {
            val currDay = operatingHours[i].hours
            if (!currDay.any { it.equals("Closed", ignoreCase = true) }) {
                val dayName = operatingHours[i].dayOfWeek
                return OpenStatus(
                    false,
                    "until $dayName"
                )
            }
        }
        return OpenStatus(false, "Closed today")
    }

    /**
     * Given operating hours, return whether it is open and when it is open until
     * or when it will next open
     *
     * @param operatingHours A list of pairs mapping the first value day string to second value list of hours open
     */
    fun getOpenStatus(
        operatingHours: List<DayOperatingHours>,
        currentDateTime: LocalDateTime = LocalDateTime.now()
    ): OpenStatus {

        if (operatingHours.isEmpty()) {
            return OpenStatus(false, "Closed today")
        }

        val rotatedOperatingHours = rotateOperatingHours(operatingHours)

        val currentTime = currentDateTime.toLocalTime()
        val todaySchedule =
            rotatedOperatingHours[0].hours // First day should be today after rotation

        // Check if closed today
        if (todaySchedule.any { it.equals("Closed", ignoreCase = true) }) {
            return findOpenNextDay(rotatedOperatingHours)
        }

        val timeRanges = todaySchedule.mapNotNull { parseTimeRange(it) }

        // Check if currently open
        for (range in timeRanges) {
            if (currentTime >= range.first && currentTime < range.second) {
                return OpenStatus(true, "until ${formatTime(range.second)}")
            }
        }

        // Check if opens later today
        for (range in timeRanges) {
            if (currentTime < range.first) {
                return OpenStatus(false, "until ${formatTime(range.first)}")
            }
        }

        // Closed for today, find next open day
        return findOpenNextDay(rotatedOperatingHours)
    }

    private fun parseTimeRange(timeString: String): Pair<LocalTime, LocalTime>? {
        if (timeString.equals("Closed", ignoreCase = true)) return null

        val parts = timeString.split("-").map { it.trim() }
        if (parts.size != 2) return null

        return try {
            val formatter = DateTimeFormatter.ofPattern("h:mm a")
            val start = LocalTime.parse(parts[0], formatter)
            val end = LocalTime.parse(parts[1], formatter)
            start to end
        } catch (e: Exception) {
            null
        }
    }

    private fun formatTime(time: LocalTime): String {
        val formatter = DateTimeFormatter.ofPattern("h:mm a")
        return time.format(formatter)
    }

    /**
     * Return annotated string for open times
     */
    private fun getOpenStatusAnnotatedString(openStatus: OpenStatus): AnnotatedString {
        return buildAnnotatedString {
            if (openStatus.isOpen) {
                withStyle(
                    style = SpanStyle(
                        color = LiveGreen,
                    )
                ) {
                    append("Open")
                }
            } else {
                withStyle(
                    style = SpanStyle(
                        color = LateRed
                    )
                ) {
                    append("Closed")
                }
            }
            withStyle(
                style = SpanStyle(
                    color = SecondaryText
                )
            ) {
                append(" - ")
                append(openStatus.nextChangeTime)
            }
        }
    }

    /**
     * Rotate operating hours to current day, then determine if place is open, then format string
     */
    fun isOpenAnnotatedStringFromOperatingHours(operatingHours: List<DayOperatingHours>): AnnotatedString {
        return getOpenStatusAnnotatedString(
            getOpenStatus(operatingHours)
        )
    }


}