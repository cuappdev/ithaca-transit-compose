package com.cornellappdev.transit.ui.components.home

import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.Style

/**
 * Card for a place with a rounded image on top
 */
@Composable
fun RoundedImagePlaceCard(
    imageUrl: String? = null,
    title: String,
    subtitle: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    leftAnnotatedString: AnnotatedString? = null,
    rightAnnotatedString: AnnotatedString? = null,
    @DrawableRes placeholderRes: Int,
    onClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 10.dp)
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(12.dp))
        ) {
            PlaceCardImage(imageUrl, placeholderRes)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = title,
                        style = Style.cardH1,
                        color = PrimaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 32.dp)
                    )
                    Text(
                        text = subtitle,
                        style = Style.heading3,
                        color = SecondaryText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(end = 32.dp)

                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        leftAnnotatedString?.let {
                            Text(
                                text = leftAnnotatedString
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        rightAnnotatedString?.let {
                            Text(
                                text = rightAnnotatedString
                            )
                        }
                    }
                }

                FavoritesStar(onFavoriteClick = onFavoriteClick, isFavorite = isFavorite)
            }

        }
    }
}

@Composable
fun PlaceCardImage(imageUrl: String?, @DrawableRes placeholderRes: Int) {
    val imageModifier = Modifier
        .fillMaxWidth()
        .height(112.dp)
        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))

    if (imageUrl.isNullOrBlank()) {
        Image(
            painter = painterResource(id = placeholderRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = imageModifier
        )
    } else {
        Log.d("RoundedImagePlaceCard", imageUrl.toString())
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(placeholderRes),
            error = painterResource(placeholderRes),
            onError = {
                Log.d("RoundedImagePlaceCard", it.toString())
            },
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = imageModifier
        )
    }
}

@Preview
@Composable
fun RoundedImagePlaceCardPreview() {
    RoundedImagePlaceCard(
        placeholderRes = R.drawable.olin_library,
        title = "Olin Library",
        subtitle = "Ho Plaza",
        isFavorite = true,
        onFavoriteClick = {},
        leftAnnotatedString = buildAnnotatedString { append("Open - until 10:00 PM") },
        rightAnnotatedString = buildAnnotatedString { append("70% full") }
    ) {

    }
}