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
     * Return the operating hours starting from Sunday
     */
    fun operatingHours(): List<DayOperatingHours>
}