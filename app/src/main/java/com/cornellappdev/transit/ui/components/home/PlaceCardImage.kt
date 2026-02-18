package com.cornellappdev.transit.ui.components.home

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cornellappdev.transit.ui.theme.MetadataGray

/**
 * Rounded image from a network request, fallback to a drawable
 */
@Composable
fun PlaceCardImage(imageUrl: String?, @DrawableRes placeholderRes: Int, shouldClipBottom: Boolean = false) {

    val imageModifier = Modifier
        .then(
        if(shouldClipBottom) Modifier.clip(RoundedCornerShape(12.dp))
            else Modifier.clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)))
        .fillMaxWidth()
        .height(112.dp)
        .background(MetadataGray)

    if (imageUrl.isNullOrBlank()) {
        Image(
            painter = painterResource(id = placeholderRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = imageModifier
        )
    } else {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            error = painterResource(id = placeholderRes),
            contentScale = ContentScale.Crop,
            modifier = imageModifier
        )
    }
}