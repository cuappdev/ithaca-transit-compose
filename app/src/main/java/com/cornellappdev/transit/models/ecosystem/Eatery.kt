package com.cornellappdev.transit.models.ecosystem

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
    @Json(name = "name") var name: String,
    @Json(name = "menu_summary") var menuSummary: String?,
    @Json(name = "image_url") var imageUrl: String?,
    @Json(name = "location") var location: String?,
    @Json(name = "campus_area") var campusArea: String?,
    @Json(name = "online_order_url") var onlineOrderUrl: String?,
    @Json(name = "latitude") var latitude: Double?,
    @Json(name = "longitude") var longitude: Double?,
    @Json(name = "payment_accepts_meal_swipes") var paymentAcceptsMealSwipes: Boolean?,
    @Json(name = "payment_accepts_brbs") var paymentAcceptsBrbs: Boolean?,
    @Json(name = "payment_accepts_cash") var paymentAcceptsCash: Boolean?,
    @Json(name = "events") val events: List<Event>?
) : DetailedEcosystemPlace {

    /**
     * @Return a list of associated dayOfWeek and hours pairs in [DayOperatingHours] representing
     * each day of the week and the corresponding times that an eatery is open. The list is sorted
     * by day with the custom dayOrder (Sunday first).
     */
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
        type = PlaceType.APPLE_PLACE
    )
}


@JsonClass(generateAdapter = true)
data class Event(
    @Json(name = "id") val id: Int? = null,
    /**
     * Descriptions tend to be "Lunch", "Dinner", etc..
     */
    @Json(name = "event_description") val description: String? = null,
    @Json(name = "start") val startTime: LocalDateTime? = null,
    @Json(name = "end") val endTime: LocalDateTime? = null,
)