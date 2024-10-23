package com.cornellappdev.transit.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object TimeUtils {
    @RequiresApi(Build.VERSION_CODES.O)
    fun getHHMM(isoString: String): String {

        // Parse the ISO string to Instant, then convert to the given timezone
        val instant = Instant.parse(isoString)
        val zonedDateTime = instant.atZone(ZoneId.of("America/New_York"))

        // Format the ZonedDateTime to AM/PM
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")
        return zonedDateTime.format(formatter)
    }
}