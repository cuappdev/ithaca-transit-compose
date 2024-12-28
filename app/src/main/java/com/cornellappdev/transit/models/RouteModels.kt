package com.cornellappdev.transit.models

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import com.cornellappdev.transit.ui.theme.LateRed
import com.cornellappdev.transit.ui.theme.LiveGreen
import com.cornellappdev.transit.util.TimeUtils

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
class Transport (
    val startTime: String,
    val arriveTime: String,
    val lateness: BusLateness,
    val distance: String,
    val start: String,
    val dest: String,
    val walkOnly: Boolean,
    val timeToBoard: Int,
    val directionList: List<Direction>
)

/**
 * Create a Transport object from Route
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Route.toTransport(): Transport {

    val containsBus = this.directions.any { dir ->
        dir.type == DirectionType.DEPART
    }

    return Transport(
        startTime = TimeUtils.getHHMM(this.departureTime),
        arriveTime = TimeUtils.getHHMM(this.arrivalTime),
        distance = String.format("%.1f", this.travelDistance),
        start = this.startName,
        dest = this.endName,
        walkOnly = !containsBus,
        lateness = if (containsBus) BusLateness.LATE else BusLateness.NONE,
        timeToBoard = 0,
        directionList = this.directions
    )
    }