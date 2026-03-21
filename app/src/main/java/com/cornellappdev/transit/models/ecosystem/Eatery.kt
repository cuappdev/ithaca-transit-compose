package com.cornellappdev.transit.models.ecosystem

import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.PlaceType
import com.cornellappdev.transit.util.TimeUtils.dayOrder
import com.cornellappdev.transit.util.TimeUtils.toPascalCaseString
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Data class representing an eatery
 */
@JsonClass(generateAdapter = true)
data class Eatery(
    @Json(name = "id") var id: Int,
    @Json(name = "cornellId") var cornellId: Int? = null,
    @Json(name = "announcements") var announcements: List<Any> = emptyList(),
    @Json(name = "name") var name: String,
    @Json(name = "shortName") var shortName: String? = null,
    @Json(name = "about") var about: String? = null,
    @Json(name = "shortAbout") var shortAbout: String? = null,
    @Json(name = "cornellDining") var cornellDining: Boolean? = null,
    @Json(name = "menuSummary") var menuSummary: String? = null,
    @Json(name = "imageUrl") var imageUrl: String? = null,
    @Json(name = "location") var location: String? = null,
    @Json(name = "campusArea") var campusArea: String? = null,
    @Json(name = "onlineOrderUrl") var onlineOrderUrl: String? = null,
    @Json(name = "contactPhone") var contactPhone: String? = null,
    @Json(name = "contactEmail") var contactEmail: String? = null,
    @Json(name = "latitude") var latitude: Double? = null,
    @Json(name = "longitude") var longitude: Double? = null,
    @Json(name = "paymentMethods") var paymentMethods: List<String>? = emptyList(),
    @Json(name = "eateryTypes") var eateryTypes: List<String>? = emptyList(),
    @Json(name = "events") val events: List<Event>? = null
) : DetailedEcosystemPlace {

    override fun operatingHours(): List<DayOperatingHours> {
        val dailyHours = getOperatingHours()

        // Convert map to list and sort by custom day order
        return dailyHours.entries
            .sortedBy { entry ->
                val dayName =
                    entry.key.toPascalCaseString()
                dayOrder[dayName] ?: Int.MAX_VALUE
            }
            .map { entry ->
                val dayName =
                    entry.key.toPascalCaseString()
                DayOperatingHours(dayName, entry.value)
            }
    }

    /**
     * @Return a map of each day of the week to its list of operating hours
     */
    private fun getOperatingHours(): Map<DayOfWeek, MutableList<String>> {
        val dailyHours = mutableMapOf<DayOfWeek, MutableList<String>>()

        events?.forEach { event ->
            val dayOfWeek = event.startTime?.dayOfWeek
            val openTime = event.startTime?.format(DateTimeFormatter.ofPattern("h:mm a"))
            val closeTime = event.endTime?.format(DateTimeFormatter.ofPattern("h:mm a"))

            if (openTime == null || closeTime == null) {
                return@forEach
            }

            val timeString = "$openTime - $closeTime"

            if (dayOfWeek != null && dailyHours[dayOfWeek]?.none { it.contains(timeString) } != false) {
                dailyHours.computeIfAbsent(dayOfWeek) { mutableListOf() }.add(timeString)
            }
        }

        DayOfWeek.entries.forEach { dayOfWeek ->
            dailyHours.computeIfAbsent(dayOfWeek) { mutableListOf("Closed") }
        }

        return dailyHours
    }

    override fun toPlace(): Place = Place(
        latitude = this.latitude ?: 0.0,
        longitude = this.longitude ?: 0.0,
        name = this.name,
        detail = this.location,
        type = PlaceType.EATERY
    )
}


@JsonClass(generateAdapter = true)
data class Event(
    @Json(name = "id") val id: Int? = null,
    @Json(name = "type") val type: String? = null,
    @Json(name = "startTimestamp") val startTime: LocalDateTime? = null,
    @Json(name = "endTimestamp") val endTime: LocalDateTime? = null,
)