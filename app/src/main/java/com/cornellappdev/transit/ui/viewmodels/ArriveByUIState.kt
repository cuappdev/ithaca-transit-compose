package com.cornellappdev.transit.ui.viewmodels

import android.os.Build
import com.cornellappdev.transit.util.TimeUtils
import java.time.Instant
import java.util.Date

/**
 * Wrapper for state of the search bar in HomeScreen
 */
sealed class ArriveByUIState {

    abstract val date: Date
    abstract val tag: String
    abstract val label: String

    data class LeaveNow constructor(
        override val tag: String = "Leave now"
    ) :
        ArriveByUIState() {
        override val date: Date
            get() = Date.from(Instant.now())

        override val label: String
            get() = "${this.tag} (${TimeUtils.timeFormatter.format(this.date)})"
    }

    data class LeaveAt(
        override val date: Date,
        override val tag: String = "Leave at"
    ) :
        ArriveByUIState() {
        override val label: String
            get() = "${this.tag} ${
                if (TimeUtils.dateFormatter.format(this.date) == TimeUtils.dateFormatter.format(
                        Date.from(Instant.now())
                    )
                ) "" else (TimeUtils.dateFormatter.format(this.date) + " at ")
            }${
                TimeUtils.timeFormatter.format(
                    this.date
                )
            } "
    }

    data class ArriveBy(
        override val date: Date,
        override val tag: String = "Arrive by"
    ) :
        ArriveByUIState() {
        override val label: String
            get() = "${this.tag} ${
                if (TimeUtils.dateFormatter.format(this.date) == TimeUtils.dateFormatter.format(
                        Date.from(Instant.now())
                    )
                ) "" else (TimeUtils.dateFormatter.format(this.date) + " at ")
            }${
                TimeUtils.timeFormatter.format(
                    this.date
                )
            } "
    }
}