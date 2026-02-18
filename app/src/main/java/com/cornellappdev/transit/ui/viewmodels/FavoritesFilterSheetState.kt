package com.cornellappdev.transit.ui.viewmodels

import androidx.annotation.DrawableRes
import com.cornellappdev.transit.R
enum class FavoritesFilterSheetState(@DrawableRes val iconId: Int, val label: String) {
    PRINTERS(iconId = R.drawable.printer_filter_icon, label = "Printers"),
    GYMS(iconId = R.drawable.gym_filter_icon, label = "Gyms"),
    EATERIES(iconId = R.drawable.eatery_filter_icon, label = "Eateries"),
    LIBRARIES(iconId = R.drawable.library_filter_icon, label = "Libraries"),
    OTHER(iconId = R.drawable.other_filter_icon, label = "Other"),
}