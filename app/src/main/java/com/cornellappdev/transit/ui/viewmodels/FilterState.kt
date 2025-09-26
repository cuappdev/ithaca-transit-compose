package com.cornellappdev.transit.ui.viewmodels

import androidx.annotation.DrawableRes
import com.cornellappdev.transit.R

/**
 * Enumeration of different states of the bottom sheet filter
 */
enum class FilterState(@DrawableRes val iconId: Int, val label: String) {
    FAVORITES(iconId = R.drawable.favorites_icon, label = "Favorites"),
    PRINTERS(iconId = R.drawable.printer_icon, label = "Printers"),
    GYMS(iconId = R.drawable.gym_icon, label = "Gyms"),
    EATERIES(iconId = R.drawable.eatery_icon, label = "Eateries"),
    LIBRARIES(iconId = R.drawable.library_icon, label = "Libraries")
}