package com.cornellappdev.transit.util.ecosystem

import android.icu.util.Calendar
import com.cornellappdev.transit.GymListQuery
import com.cornellappdev.transit.fragment.GymFields
import com.cornellappdev.transit.fragment.OpenHoursFields
import com.cornellappdev.transit.models.ecosystem.gym.TimeInterval
import com.cornellappdev.transit.models.ecosystem.gym.TimeOfDay
import com.cornellappdev.transit.models.ecosystem.gym.UpliftCapacity
import com.cornellappdev.transit.models.ecosystem.gym.UpliftGym

/**
 * Parses the output of [pullHours] for a NONE input.
 */
fun List<List<Pair<TimeInterval, String>>?>.toTimeInterval(): List<List<TimeInterval>?> {
    return this.map { list -> list?.map { pair -> pair.first } }
}

/**
 * Returns a list of all the [UpliftGym]s that this gym query represents.
 *
 * Example: Teagle Gym in backend has both Teagle Up and Teagle Down as separate fitness centers.
 * This should separate them into distinct gyms.
 */
fun GymListQuery.GetAllGym.toUpliftGyms(): List<UpliftGym> {
    val fitnessFacilities = gymFields.facilities?.filterNotNull()?.filter { facility ->
        facility.facilityFields.facilityType.toString() == "FITNESS"
    } ?: listOf()

    val gyms = fitnessFacilities.map { facility ->
        UpliftGym(
            name = facility.pullName(gymFields.name),
            id = gymFields.id,
            facilityId = facility.facilityFields.id,
            // Need replace because there's a typo with the single quote.
            imageUrl = gymFields.imageUrl?.replace("'", "")
                ?.replace("toni-morrison-outside", "toni_morrison_outside"),
            hours = pullHours(facility.facilityFields.hours?.map { it?.openHoursFields }).toTimeInterval(),
            upliftCapacity = pullCapacity(facility),
            latitude = gymFields.latitude,
            longitude = gymFields.longitude,
        )
    }

    return gyms
}

/**
 * Returns the name of the given gym facility.
 *
 * Serves as a temporary fix to the gym vs. fitness facility debacle.
 */
fun GymFields.Facility.pullName(gymName: String): String {
    if (gymName.lowercase().contains("teagle")) {
        return if (facilityFields.name.lowercase().contains("up")) "Teagle Up"
        else "Teagle Down"
    }
    return gymName
}


enum class HourAdditionalData {
    NONE, WOMEN_ONLY, COURT_TYPE
}

/**
 * Returns the hours for the given open hour list. Works for any open hour list.
 *
 * Can package some additional data as the second in the tuple:
 * - If additional data is NONE, packages an empty string.
 * - If additional data is WOMEN_ONLY, packages either "true" or "false".
 * - If additional data is COURT_TYPE, packages the name of the court.
 */
fun pullHours(
    hoursFields: List<OpenHoursFields?>?,
    additionalData: HourAdditionalData = HourAdditionalData.NONE
): List<List<Pair<TimeInterval, String>>?> {
    // Initialize to always closed.
    val hoursList: MutableList<MutableList<Pair<TimeInterval, String>>?> = MutableList(7) { null }

    // If fitness facility doesn't exist (...it always should...), return.
    val hours = hoursFields ?: return hoursList

    hours.forEach { openHour ->
        if (openHour != null) {
            val startMillis: Long = openHour.startTime.toLong() * 1000
            val endMillis: Long = openHour.endTime.toLong() * 1000

            val startCal = Calendar.getInstance()
            startCal.timeInMillis = startMillis
            val endCal = Calendar.getInstance()
            endCal.timeInMillis = endMillis

            val day = when (startCal.get(Calendar.DAY_OF_WEEK)) {
                Calendar.SUNDAY -> 6
                Calendar.MONDAY -> 0
                Calendar.TUESDAY -> 1
                Calendar.WEDNESDAY -> 2
                Calendar.THURSDAY -> 3
                Calendar.FRIDAY -> 4
                Calendar.SATURDAY -> 5
                else -> -1
            }

            val data = when (additionalData) {
                HourAdditionalData.NONE -> ""
                HourAdditionalData.WOMEN_ONLY -> (openHour.isWomen == true).toString()
                HourAdditionalData.COURT_TYPE -> openHour.courtType.toString()
            }.lowercase()

            val newTimeInterval = TimeInterval(
                start = TimeOfDay(
                    hour = startCal.get(Calendar.HOUR_OF_DAY),
                    minute = startCal.get(Calendar.MINUTE)
                ),
                end = TimeOfDay(
                    hour = endCal.get(Calendar.HOUR_OF_DAY),
                    minute = endCal.get(Calendar.MINUTE)
                )
            )

            // Initialize hours at index day if it doesn't have an entry.
            if (hoursList[day] == null) {
                hoursList[day] = mutableListOf()
            }

            // Know it is non-null from if statement above.
            hoursList[day]!!.add(Pair(newTimeInterval, data))
        }
    }

    for (i in 0 until hoursList.size) {
        if (hoursList[i] != null) {
            hoursList[i]!!.sortWith { h1, h2 ->
                h1.first.end.compareTo(h2.first.end)
            }
        }
    }

    return hoursList
}

/**
 * Returns the capacity at the given gym query.
 */
fun pullCapacity(
    facilityIn: GymFields.Facility?
): UpliftCapacity? {
    // If fitness facility doesn't exist (...it always should...), return.
    val facility = facilityIn ?: return null
    if (facility.facilityFields.capacity == null
        || facility.facilityFields.capacity.capacityFields.percent < 0.0
    ) return null

    val highCap = facility.facilityFields.capacity.capacityFields.let {
        it.count / it.percent
    }

    if (highCap.isNaN()) return null

    // Ex: "2023-09-19T18:42:00"
    val cal = Calendar.getInstance()
    cal.timeInMillis = facility.facilityFields.capacity.capacityFields.updated * 1000L

    return UpliftCapacity(
        percent = facility.facilityFields.capacity.capacityFields.percent,
        updated = cal
    )
}

