package com.cornellappdev.transit.ui.theme

import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

object Style {
    /**
     * TextStyle for Card titles, On time/Late, and Board time
     */
    val heading3 = TextStyle(
        fontSize = 14.sp,
        fontFamily = sfProDisplayFamily,
        lineHeight = 16.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        ),
    )

    /**
     * TextStyle for Descriptions, Stop names, and Route numbers
     */
    val paragraph = TextStyle(
        fontSize = 10.sp,
        color = SecondaryText,
        fontFamily = sfProDisplayFamily,
        lineHeight = 12.sp,
        platformStyle = PlatformTextStyle(
            includeFontPadding = false
        )
    )
}