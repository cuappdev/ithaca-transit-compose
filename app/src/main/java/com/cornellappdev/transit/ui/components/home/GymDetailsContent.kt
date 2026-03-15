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
import com.cornellappdev.transit.models.ecosystem.UpliftGym
import com.cornellappdev.transit.ui.theme.DividerGray
import com.cornellappdev.transit.ui.theme.Gray05
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.Style
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.util.StringUtils.createDeepLink
import com.cornellappdev.transit.util.TimeUtils.getOpenStatus
import com.cornellappdev.transit.util.TimeUtils.isOpenAnnotatedStringFromOperatingHours
import com.cornellappdev.transit.util.TimeUtils.rotateOperatingHours
import com.cornellappdev.transit.util.getAboutContent
import com.cornellappdev.transit.util.getGymLocationString

/**
 * Displays the full detail view for an individual gym within the ecosystem bottom sheet.
 */
@Composable
fun GymDetailsContent(
    gym: UpliftGym,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    distanceString: String
) {
    val isOpen = getOpenStatus(gym.operatingHours()).isOpen

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp
            )
    ) {
        PlaceCardImage(
            imageUrl = gym.imageUrl,
            placeholderRes = R.drawable.olin_library,
            shouldClipBottom = true
        )

        DetailedPlaceHeaderSection(
            gym.name,
            getGymLocationString(gym.name) + distanceString,
            onFavoriteClick = onFavoriteClick,
            isFavorite = isFavorite,
            leftAnnotatedString = isOpenAnnotatedStringFromOperatingHours(
                gym.operatingHours()
            ),
            widget = {
                if (gym.upliftCapacity != null && isOpen) {
                    GymCapacityIndicator(
                        capacity = gym.upliftCapacity,
                        label = null,
                    )
                }
            },
        )

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
            text = getAboutContent(gym.name),
            style = Style.detailBody,
            color = SecondaryText,
            modifier = Modifier.padding(bottom = 15.dp)
        )

        val (annotatedString, inlineContent) =
            stringResource(R.string.view_gym).createDeepLink(R.drawable.upliftlink)

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
                text = getGymLocationString(gym.name) ?: "",
                style = Style.detailBody,
                color = SecondaryText,
                modifier = Modifier.padding(start = 15.dp)
            )

        }

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider(thickness = 1.dp, color = DividerGray)

        ExpandableOperatingHoursList(
            isOpenAnnotatedStringFromOperatingHours(gym.operatingHours()),
            rotateOperatingHours(gym.operatingHours())
        )

    }
}