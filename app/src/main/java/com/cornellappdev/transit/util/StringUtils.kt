package com.cornellappdev.transit.util

import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

object StringUtils {
    fun String.toURLString(): String {
        return URLEncoder.encode(this, StandardCharsets.UTF_8.toString())
    }

    fun String.fromURLString() : String {
        return URLDecoder.decode(this, StandardCharsets.UTF_8.toString())
    }
}