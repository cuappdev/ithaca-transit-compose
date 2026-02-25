package com.cornellappdev.transit.models.ecosystem

import com.cornellappdev.transit.models.Place

/**
 * Specific places such as eateries or gyms
 */
interface EcosystemPlace {

    /**
     * Convert from a specific ecosystem place to the generic [Place] class
     */
    fun toPlace(): Place
}


/**
 * Interface for working with places in ecosystem with special details, i.e. hours or capacity
 */
sealed interface DetailedEcosystemPlace: EcosystemPlace {

    /**
     * @Return a list of associated dayOfWeek and hours pairs in [DayOperatingHours] representing
     * each day of the week and the corresponding times that an eatery is open. The list is sorted
     * by day with the custom dayOrder (Sunday first).
     */
    fun operatingHours(): List<DayOperatingHours>
}