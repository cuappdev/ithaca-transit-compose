package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.Image
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.ecosystem.Library
import com.cornellappdev.transit.ui.theme.DividerGray
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.Style
import com.cornellappdev.transit.ui.theme.TransitBlue

@Composable
fun LibraryDetailsContent(
    library: Library,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    leftAnnotatedString: AnnotatedString? = null,
    rightAnnotatedString: AnnotatedString? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.olin_library),
            contentDescription = library.location,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(112.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = library.location,
                    style = Style.detailHeading,
                    color = PrimaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(end = 32.dp, bottom = 12.dp)
                )
                Text(
                    text = library.address,
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

            FavoritesStar(onFavoriteClick, isFavorite)
        }

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(thickness = 1.dp, color = DividerGray)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "About",
            style = Style.detailSubtitle,
            color = PrimaryText,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Text(
            text = stringResource(R.string.library_about_placeholder),
            style = Style.detailBody,
            color = SecondaryText,
            modifier = Modifier.padding(bottom = 15.dp)
        )

        Text(
            text = stringResource(R.string.library_reserve),
            style = Style.heading2,
            color = TransitBlue
        )

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(thickness = 1.dp, color = DividerGray)

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painterResource(R.drawable.location_pin_gray),
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp),
                tint = Color.Gray
            )
            Text(
                text = library.location,
                style = Style.detailBody,
                color = SecondaryText,
                modifier = Modifier.padding(start = 15.dp)
            )

        }

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(thickness = 1.dp, color = DividerGray)

        // TODO: Hours
        Spacer(modifier = Modifier.height(300.dp))

    }
}