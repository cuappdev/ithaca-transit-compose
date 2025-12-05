package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.ecosystem.DetailedEcosystemPlace
import com.cornellappdev.transit.models.ecosystem.Eatery
import com.cornellappdev.transit.models.ecosystem.Library
import com.cornellappdev.transit.models.ecosystem.UpliftGym
import com.cornellappdev.transit.ui.theme.DividerGray
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.Style
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.util.BOTTOM_SHEET_MAX_HEIGHT_PERCENT

@Composable
fun DetailedPlaceSheetContent(
    ecosystemPlace: DetailedEcosystemPlace,
    favorites: Set<Place>,
    onBackButtonPressed: () -> Unit,
    navigateToPlace: (Place) -> Unit,
    onFavoriteStarClick: (Place) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(BOTTOM_SHEET_MAX_HEIGHT_PERCENT.toFloat() / 100f)
    ) {
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
                    onBackButtonPressed()
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

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Main Content
            when (ecosystemPlace) {
                is Eatery -> {
                    EateryDetailsContent(
                        eatery = ecosystemPlace,
                        isFavorite = ecosystemPlace.toPlace() in favorites,
                        onFavoriteClick = {
                            onFavoriteStarClick(ecosystemPlace.toPlace())
                        }
                    )
                }

                is Library -> {
                    LibraryDetailsContent(
                        ecosystemPlace,
                        isFavorite = ecosystemPlace.toPlace() in favorites,
                        onFavoriteClick = {
                            onFavoriteStarClick(ecosystemPlace.toPlace())
                        })

                }

                is UpliftGym -> {
                    GymDetailsContent(
                        gym = ecosystemPlace,
                        isFavorite = ecosystemPlace.toPlace() in favorites,
                        onFavoriteClick = {
                            onFavoriteStarClick(ecosystemPlace.toPlace())
                        }
                    )
                }
            }
        }

        //To Route Screen Button
        Column {
            HorizontalDivider(thickness = 1.dp, color = DividerGray)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Row(
                    modifier = Modifier
                        .background(
                            color = TransitBlue,
                            shape = RoundedCornerShape(60.dp)
                        )
                        .width(144.dp)
                        .height(44.dp)
                        .clickable {
                            when (ecosystemPlace) {
                                is Eatery -> {
                                    navigateToPlace(
                                        ecosystemPlace.toPlace()
                                    )
                                }

                                is Library -> {
                                    navigateToPlace(
                                        ecosystemPlace.toPlace()
                                    )
                                }

                                is UpliftGym -> {
                                    navigateToPlace(
                                        ecosystemPlace.toPlace()
                                    )
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
}

@Preview(showBackground = true)
@Composable
private fun DetailedPlaceSheetContentPreview() {
    DetailedPlaceSheetContent(
        Library(
            id = 1,
            location = "Olin Library",
            address = "Ho Plaza",
            latitude = 1.0,
            longitude = 1.0
        ),
        favorites = emptySet(),
        onBackButtonPressed = {},
        navigateToPlace = {},
        onFavoriteStarClick = {},
        modifier = Modifier.background(Color.White)
    )
}