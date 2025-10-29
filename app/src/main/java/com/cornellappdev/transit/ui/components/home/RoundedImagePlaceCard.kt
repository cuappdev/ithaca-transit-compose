package com.cornellappdev.transit.ui.components.home

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.theme.FavoritesYellow
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.Style

@Composable
fun RoundedImagePlaceCard(
    imageRes: Int,
    title: String,
    subtitle: String,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    leftAnnotatedString: AnnotatedString? = null,
    rightAnnotatedString: AnnotatedString? = null,
    onClick: () -> Unit
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
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(112.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Text content
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
                        if (leftAnnotatedString != null) {
                            Text(
                                text = leftAnnotatedString
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        if (rightAnnotatedString != null) {
                            Text(
                                text = rightAnnotatedString
                            )
                        }
                    }
                }

                // Star
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp)
                        .clickable { onFavoriteClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Star else ImageVector.vectorResource(
                            R.drawable.baseline_star_outline_20
                        ),
                        contentDescription = null,
                        tint = if (isFavorite) FavoritesYellow else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

        }
    }
}

@Preview
@Composable
fun RoundedImagePlaceCardPreview() {
    RoundedImagePlaceCard(
        imageRes = R.drawable.olin_library,
        title = "Olin Library",
        subtitle = "Ho Plaza",
        isFavorite = true,
        onFavoriteClick = {},
        leftAnnotatedString = buildAnnotatedString { append("Open - until 10:00 PM") },
        rightAnnotatedString = buildAnnotatedString { append("70% full") }
    ) {

    }
}