package com.cornellappdev.transit.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * High fidelity text styles
 */
object Style {

    val heading2 = TextStyle(
        fontFamily = robotoFamily,
        fontSize = 16.sp,
        lineHeight = 20.sp,
        fontWeight = FontWeight(600),
        color = Color.White,
        platformStyle = PlatformTextStyle(includeFontPadding = false)
    )

    /**
     * TextStyle for Card titles, On time/Late, and Board time
     */
    val heading3 = TextStyle(
        fontSize = 14.sp,
        fontFamily = robotoFamily,
        lineHeight = 16.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
    )

    val heading3Emphasized = TextStyle(
        fontSize = 14.sp,
        fontFamily = robotoFamily,
        lineHeight = 16.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        fontWeight = FontWeight(500)
    )

    /**
     * TextStyle for headers of route types
     */
    val heading4 = TextStyle(
        fontSize = 12.sp,
        fontFamily = robotoFamily,
        lineHeight = 14.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    )

    /**
     * TextStyle for Descriptions, Stop names, and Route numbers
     */
    val paragraph = TextStyle(
        fontSize = 10.sp,
        fontFamily = robotoFamily,
        lineHeight = 12.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    )

    /**
     * TextStyle for Card headers
     */
    val cardH1 = TextStyle(
        fontSize = 16.sp,
        fontFamily = robotoFamily,
        lineHeight = 18.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
        fontWeight = FontWeight(500)
    )
}