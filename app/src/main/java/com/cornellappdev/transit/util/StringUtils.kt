package com.cornellappdev.transit.util

import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Extra utility functions for strings
 */
object StringUtils {

    /**
     * Convert a string to a URL safe string
     */
    fun String.toURLString(): String {
        return URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
    }

    /**
     * Convert a string from a URL safe string to a normal string
     */
    fun String.fromURLString(): String {
        return URLDecoder.decode(this, StandardCharsets.UTF_8.toString())
    }

    fun String.fromMetersToMiles(): String {
        return "%.1f".format(this.toDouble() / 1609.34)
    }
}