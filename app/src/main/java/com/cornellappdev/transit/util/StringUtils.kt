package com.cornellappdev.transit.util

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.R
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

    /**
     * Creates an annotated string link with an arrow and an optional icon.
     *
     * Example Usage:
     *
     *          val (annotatedString, inlineContent) =
     *             stringResource(R.string.view_menu).createDeepLink(R.drawable.eaterylink)
     *
     *         Text(
     *             text = annotatedString,
     *             inlineContent = inlineContent,
     *             style = Style.heading2,
     *             color = TransitBlue
     *         )
     */
    @Composable
    fun String.createDeepLink(
        @DrawableRes iconSvgResId: Int? = null,
        @DrawableRes endSvgResId: Int = R.drawable.linkarrow,
        svgSize: Int = 20
    ): Pair<AnnotatedString, Map<String, InlineTextContent>> {

        val text = this
        val iconTag = "svg_icon"
        val endTag = "svg_end"

        val annotatedString = buildAnnotatedString {
            append(text)
            append(" ")
            if (iconSvgResId != null) {
                appendInlineContent(iconTag, "[icon]")
                append(" ")
            }
            appendInlineContent(endTag, "[end]")
        }

        val inlineContent = mutableMapOf<String, InlineTextContent>()

        if (iconSvgResId != null) {
            inlineContent[iconTag] = InlineTextContent(
                Placeholder(
                    width = svgSize.sp,
                    height = svgSize.sp,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                )
            ) {
                Image(
                    painter = painterResource(id = iconSvgResId),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        inlineContent[endTag] = InlineTextContent(
            Placeholder(
                width = 11.sp,
                height = 11.sp,
                placeholderVerticalAlign = PlaceholderVerticalAlign.Center
            )
        ) {
            Image(
                painter = painterResource(id = endSvgResId),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }

        return Pair(annotatedString, inlineContent)
    }
}