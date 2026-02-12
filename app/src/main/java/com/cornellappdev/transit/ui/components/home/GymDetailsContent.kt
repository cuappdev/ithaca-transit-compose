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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.ecosystem.UpliftGym
import com.cornellappdev.transit.ui.theme.DividerGray
import com.cornellappdev.transit.ui.theme.Gray05
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.Style
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.viewmodels.HomeViewModel
import com.cornellappdev.transit.util.getAboutContent
import com.cornellappdev.transit.util.getGymLocationString

@Composable
fun GymDetailsContent(
    homeViewModel: HomeViewModel = hiltViewModel(),
    gym: UpliftGym,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
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
            imageUrl = gym.imageUrl,
            placeholderRes = R.drawable.olin_library,
            shouldClipBottom = true
        )

        DetailedPlaceHeaderSectionWithWidget(
            gym.name,
            getGymLocationString(gym.name),
            onFavoriteClick = onFavoriteClick,
            isFavorite = isFavorite,
            leftAnnotatedString = homeViewModel.isOpenAnnotatedStringFromOperatingHours(
                gym.operatingHours()
            ),
            widget = {
                GymCapacityIndicator(
                    capacity = gym.upliftCapacity,
                    label = null,
                    closed = false
                )
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

        Text(
            text = stringResource(R.string.view_gym),
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
            homeViewModel.isOpenAnnotatedStringFromOperatingHours(gym.operatingHours()),
            homeViewModel.rotateOperatingHours(gym.operatingHours())
        )

    }
}