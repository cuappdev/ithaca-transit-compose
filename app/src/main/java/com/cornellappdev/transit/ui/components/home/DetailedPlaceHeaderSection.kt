package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.Style

/**
 * Text area of detailed place header with favorites star
 */
@Composable
fun DetailedPlaceHeaderSection(
    title: String,
    subtitle: String?,
    leftAnnotatedString: AnnotatedString? = null,
    rightAnnotatedString: AnnotatedString? = null,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart),
        ) {
            Text(
                text = title,
                style = Style.detailHeading,
                color = PrimaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 32.dp, bottom = 12.dp)
            )
            subtitle?.let {
                Text(
                    text = subtitle,
                    style = Style.cardSubtitle,
                    color = SecondaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 32.dp, bottom = 8.dp)

                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                leftAnnotatedString?.let {
                    Text(
                        text = leftAnnotatedString,
                        style = Style.cardSubtitle
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                rightAnnotatedString?.let {
                    Text(
                        text = rightAnnotatedString,
                        style = Style.cardSubtitle
                    )
                }
            }
        }

        FavoritesStar(onFavoriteClick, isFavorite)
    }
}

/**
 * Text area of detailed place header with favorites star on the top right and widget on the bottom right
 */
@Composable
fun DetailedPlaceHeaderSectionWithWidget(
    title: String,
    subtitle: String?,
    leftAnnotatedString: AnnotatedString? = null,
    widget: @Composable BoxScope.() -> Unit,
    onFavoriteClick: () -> Unit,
    isFavorite: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart),
        ) {
            Text(
                text = title,
                style = Style.detailHeading,
                color = PrimaryText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 32.dp, bottom = 12.dp)
            )
            subtitle?.let {
                Text(
                    text = subtitle,
                    style = Style.cardSubtitle,
                    color = SecondaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 32.dp, bottom = 8.dp)

                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                leftAnnotatedString?.let {
                    Text(
                        text = leftAnnotatedString,
                        style = Style.cardSubtitle
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Box(Modifier.align(BottomEnd)) {
            widget()
        }
        FavoritesStar(onFavoriteClick, isFavorite)
    }
}

@Preview(showBackground = true)
@Composable
private fun DetailedPlaceHeaderSectionPreview() {
    DetailedPlaceHeaderSection(
        title = "Atrium Cafe",
        subtitle = "Sage Hall",
        onFavoriteClick = {},
        isFavorite = false
    )
}