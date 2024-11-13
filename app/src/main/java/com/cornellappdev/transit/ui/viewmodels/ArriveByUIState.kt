package com.cornellappdev.transit.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.util.Date

/**
 * Wrapper for state of the search bar in HomeScreen
 */
sealed class ArriveByUIState {
    data class LeaveNow(val tag: String = "Leave now") :
        ArriveByUIState() {
        val date
            @RequiresApi(Build.VERSION_CODES.O)
            get() = Date.from(Instant.now())

    }

    data class LeaveAt(
        val date: Date,
        val tag: String = "Leave at",
    ) :
        ArriveByUIState()

    data class ArriveBy(
        val date: Date,
        val tag: String = "Arrive by"
    ) :
        ArriveByUIState()
}

/**
 * Get the Date object of the ArriveBy state
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ArriveByUIState.getDate(): Date {
    return when (this) {
        is ArriveByUIState.LeaveNow -> {
            this.date
        }

        is ArriveByUIState.ArriveBy -> {
            this.date
        }

        is ArriveByUIState.LeaveAt -> {
            this.date
        }
    }
}

/**
 * Get the String value of the ArriveBy state
 */
fun ArriveByUIState.getTag(): String {
    return when (this) {
        is ArriveByUIState.LeaveNow -> {
            this.tag
        }

        is ArriveByUIState.ArriveBy -> {
            this.tag
        }

        is ArriveByUIState.LeaveAt -> {
            this.tag
        }
    }
}