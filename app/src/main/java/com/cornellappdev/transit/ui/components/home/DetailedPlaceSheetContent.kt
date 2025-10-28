package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.PlaceType
import com.cornellappdev.transit.models.ecosystem.DetailedEcosystemPlace
import com.cornellappdev.transit.models.ecosystem.Eatery
import com.cornellappdev.transit.models.ecosystem.Library
import com.cornellappdev.transit.models.ecosystem.UpliftGym
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.Style
import com.cornellappdev.transit.ui.theme.TransitBlue

@Composable
fun DetailedPlaceSheetContent(
    place: DetailedEcosystemPlace,
    navigateToTabs: () -> Unit,
    navigateToPlace: (Place) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Back Button
        Row(
            modifier = Modifier
                .padding(
                    bottom = 20.dp,
                    start = 20.dp,
                    end = 20.dp,
                    top = 2.dp
                )
                .clickable {
                    navigateToTabs()
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                contentDescription = "Back",
                tint = SecondaryText,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "Back",
                style = Style.heading3,
                color = SecondaryText

            )
        }

        // Main Content
        when (place) {
            is Eatery -> {
                //TODO
                Text(place.name)
            }

            is Library -> {

                Text(place.location)
            }

            is UpliftGym -> {
                //TODO
                Text(place.name)
            }
        }

        //To Route Screen Button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Row(
                modifier = modifier
                    .background(
                        color = TransitBlue,
                        shape = RoundedCornerShape(60.dp)
                    )
                    .width(144.dp)
                    .height(44.dp)
                    .clickable {
                        when (place) {
                            is Eatery -> {

                            }

                            is Library -> {
                                navigateToPlace(
                                    Place(
                                        latitude = place.latitude,
                                        longitude = place.longitude,
                                        name = place.location,
                                        detail = place.address,
                                        type = PlaceType.APPLE_PLACE
                                    )
                                )
                            }

                            is UpliftGym -> {

                            }
                        }
                    }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(
                        horizontal = 16.dp,
                        vertical = 8.dp
                    )
                ) {

                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.bus),
                        contentDescription = "Bus",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(24.dp)
                            .padding(4.dp)
                    )
                    Text(
                        text = "Directions",
                        color = Color.White,
                        style = Style.heading2
                    )

                }
            }
        }
    }
}

@Composable
fun LibraryDetailsContent(library: Library) {

}