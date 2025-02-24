package com.cornellappdev.transit.models

import androidx.compose.ui.graphics.Color
import com.cornellappdev.transit.ui.theme.LateRed
import com.cornellappdev.transit.ui.theme.LiveGreen
import com.cornellappdev.transit.util.StringUtils.fromMetersToMiles
import com.cornellappdev.transit.util.TimeUtils
import java.time.Instant
import java.util.Locale

/**
 * Enum representing whether a bus is late
 */
enum class BusLateness {
    LATE {
        override fun color() = LateRed
        override fun text() = "Delayed"
    },
    NORMAL {
        override fun color() = LiveGreen
        override fun text() = "On Time"
    },
    NONE {
        override fun color() = Color.Black
        override fun text() = ""
    };

    abstract fun color(): Color
    abstract fun text(): String
}

/**
 * Class representing a total path for a route
 */
data class Transport(
    val startTime: String,
    val arriveTime: String,
    val lateness: BusLateness,
    val distanceMeters: String,
    val start: String,
    val end: String,
    val walkOnly: Boolean,
    val timeToBoard: String,
    val directionList: List<Direction>
) {
    val distance: String
        get() = distanceMeters.fromMetersToMiles()
}

/**
 * Create a Transport object from Route
 */
fun Route.toTransport(): Transport {

    val containsBus = this.directions.any { dir ->
        dir.type == DirectionType.DEPART
    }

    return Transport(
        startTime = TimeUtils.getHHMM(this.departureTime),
        arriveTime = TimeUtils.getHHMM(this.arrivalTime),
        distanceMeters = String.format(Locale.US, "%.1f", this.travelDistance),
        start = this.startName,
        end = this.endName,
        walkOnly = !containsBus,
        lateness = if (containsBus) {
            if (this.busDelayed) {
                BusLateness.LATE
            } else {
                BusLateness.NORMAL
            }
        } else {
            BusLateness.NONE
        },
        timeToBoard = TimeUtils.dayHourMinuteDifference(
            Instant.now().toString(),
            this.departureTime
        ),
        directionList = this.directions
    )
}