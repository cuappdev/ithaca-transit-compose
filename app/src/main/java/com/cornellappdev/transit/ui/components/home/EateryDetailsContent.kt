package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.ecosystem.Eatery
import com.cornellappdev.transit.ui.theme.DividerGray
import com.cornellappdev.transit.ui.theme.Gray05
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.Style
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.util.StringUtils.createDeepLink
import com.cornellappdev.transit.util.TimeUtils.isOpenAnnotatedStringFromOperatingHours
import com.cornellappdev.transit.util.TimeUtils.rotateOperatingHours

@Composable
fun EateryDetailsContent(
    eatery: Eatery,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    distanceString: String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
            )
    ) {
        PlaceCardImage(
            imageUrl = eatery.imageUrl,
            placeholderRes = R.drawable.olin_library,
            shouldClipBottom = true
        )

        DetailedPlaceHeaderSection(
            eatery.name,
            (eatery.location ?: "") + distanceString,
            leftAnnotatedString = isOpenAnnotatedStringFromOperatingHours(
                eatery.operatingHours()
            ),
            onFavoriteClick = onFavoriteClick,
            isFavorite = isFavorite
        )

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(thickness = 1.dp, color = DividerGray)

        Spacer(modifier = Modifier.height(24.dp))

        val aboutText = eatery.shortAbout
            ?.takeIf { it.isNotBlank() }
            ?: "This is one of Cornell's many eateries."

        Text(
            text = "About",
            style = Style.detailSubtitle,
            color = PrimaryText,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Text(
            text = aboutText,
            style = Style.detailBody,
            color = SecondaryText,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        val (annotatedString, inlineContent) =
            stringResource(R.string.view_menu).createDeepLink(R.drawable.eaterylink)

        Text(
            text = annotatedString,
            inlineContent = inlineContent,
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
                tint = Gray05
            )
            Text(
                text = eatery.location ?: "",
                style = Style.detailBody,
                color = SecondaryText,
                modifier = Modifier.padding(start = 15.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        HorizontalDivider(thickness = 1.dp, color = DividerGray)

        ExpandableOperatingHoursList(
            isOpenAnnotatedStringFromOperatingHours(eatery.operatingHours()),
            rotateOperatingHours(eatery.operatingHours())
        )

    }
}