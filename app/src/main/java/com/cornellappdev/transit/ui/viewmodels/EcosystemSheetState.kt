package com.cornellappdev.transit.ui.viewmodels

import com.cornellappdev.transit.models.ecosystem.DetailedEcosystemPlace

/**
 * Sheet state for navigating the ecosystem bottom sheet
 */
sealed class EcosystemSheetState {
    /** Filters screen between place categories */
    object Tabs : EcosystemSheetState()

    /** Details screen after clicking into a place */
    data class Details(val place: DetailedEcosystemPlace) : EcosystemSheetState()
}