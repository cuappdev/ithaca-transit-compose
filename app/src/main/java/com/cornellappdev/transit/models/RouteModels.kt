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
sealed class Transport {
    abstract val startTime: String
    abstract val arriveTime: String
    abstract val lateness: BusLateness

    class WalkOnly(
        override val startTime: String,
        override val arriveTime: String,
        override val lateness: BusLateness = BusLateness.NONE,
        val distance: String,
        val dest: String
    ) : Transport()

    class WalkAndBus(
        override val startTime: String,
        override val arriveTime: String,
        val distance: String,
        val start: String,
        val dest: String,
        val timeToBoard: Int,
        val endStop: String,
        val bus: Int,
        override val lateness: BusLateness,
        val transferList: List<Direction>,
    ) : Transport()

    class BusOnly(
        override val startTime: String,
        override val arriveTime: String,
        val distance: String,
        val start: String,
        val dest: String,
        val transferStop: String,
        val timeToBoard: Int,
        val firstBus: Int,
        val secondBus: Int,
        val transferList: List<Direction>,
        override val lateness: BusLateness
    ) : Transport()
}

/**
 * Create a Transport object from Route
 */
@RequiresApi(Build.VERSION_CODES.O)
fun Route.toTransport(): Transport {
    // TODO: We need to change the Transport class to accurately reflect all possible route configs
    val allBus = this.directions.all { dir ->
        dir.type == DirectionType.DEPART
    }
    val containsBus = this.directions.any { dir ->
        dir.type == DirectionType.DEPART
    }

    if (containsBus) {
        return Transport.BusOnly(
            startTime = TimeUtils.getHHMM(this.departureTime),
            arriveTime = TimeUtils.getHHMM(this.arrivalTime),
            distance = String.format("%.1f", this.travelDistance),
            start = this.startName,
            dest = this.endName,
            firstBus = this.directions[0].routeId?.toInt() ?: 0,
            lateness = BusLateness.LATE,
            secondBus = this.directions[1].routeId?.toInt() ?: 0,
            timeToBoard = 0,
            transferStop = "",
            transferList = this.directions
        )
    }
    else {
        return Transport.WalkOnly(
            startTime = TimeUtils.getHHMM(this.departureTime),
            arriveTime = TimeUtils.getHHMM(this.arrivalTime),
            dest = this.endName,
            distance = String.format("%.1f", this.travelDistance)
        )
    }

}