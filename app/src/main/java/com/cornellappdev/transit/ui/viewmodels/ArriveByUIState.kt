package com.cornellappdev.transit.ui.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import com.cornellappdev.transit.util.TimeUtils
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
 * Return label based on ArriveBy state
 */
@RequiresApi(Build.VERSION_CODES.O)
fun ArriveByUIState.getLabel(): String {
    return when (this) {
        is ArriveByUIState.LeaveNow -> {
            "${this.tag} (${TimeUtils.timeFormatter.format(this.getDate())})"
        }

        is ArriveByUIState.ArriveBy -> {
            // If date is the same as today, don't display date
            "${this.tag} ${
                if (TimeUtils.dateFormatter.format(this.getDate()) == TimeUtils.dateFormatter.format(
                        Date.from(Instant.now())
                    )
                ) "" else (TimeUtils.dateFormatter.format(this.getDate()) + " at ")
            }${
                TimeUtils.timeFormatter.format(
                    this.getDate()
                )
            } "
        }

        is ArriveByUIState.LeaveAt -> {
            // If date is the same as today, don't display date
            "${this.tag} ${
                if (TimeUtils.dateFormatter.format(this.getDate()) == TimeUtils.dateFormatter.format(
                        Date.from(Instant.now())
                    )
                ) "" else (TimeUtils.dateFormatter.format(this.getDate()) + " at ")
            }${
                TimeUtils.timeFormatter.format(
                    this.getDate()
                )
            } "
        }
    }
}