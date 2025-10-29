package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.models.ecosystem.DetailedEcosystemPlace
import com.cornellappdev.transit.models.ecosystem.Eatery
import com.cornellappdev.transit.models.ecosystem.Library
import com.cornellappdev.transit.models.ecosystem.UpliftGym
import com.cornellappdev.transit.ui.theme.DividerGray
import com.cornellappdev.transit.ui.theme.FavoritesYellow
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.Style
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.util.BOTTOM_SHEET_MAX_HEIGHT_PERCENT
import com.cornellappdev.transit.util.ecosystem.toPlace

@Composable
fun DetailedPlaceSheetContent(
    ecosystemPlace: DetailedEcosystemPlace,
    favorites: Set<Place>,
    navigateToTabs: () -> Unit,
    navigateToPlace: (Place) -> Unit,
    addFavorite: (Place) -> Unit,
    removeFavorite: (Place) -> Unit,
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

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
            // Main Content
            when (ecosystemPlace) {
                is Eatery -> {
                    //TODO
                    Text(ecosystemPlace.name)
                }

                is Library -> {
                    LibraryDetailsContent(
                        ecosystemPlace,
                        isFavorite = ecosystemPlace.toPlace() in favorites,
                        onFavoriteClick = {
                            if (ecosystemPlace.toPlace() !in favorites) {
                                addFavorite(ecosystemPlace.toPlace())
                            } else {
                                removeFavorite(ecosystemPlace.toPlace())
                            }
                        })

                }

                is UpliftGym -> {
                    //TODO
                    Text(ecosystemPlace.name)
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
                                    //TODO
                                }

                                is Library -> {
                                    navigateToPlace(
                                        ecosystemPlace.toPlace()
                                    )
                                }

                                is UpliftGym -> {
                                    //TODO
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
            // Text content
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
                    .size(20.dp)
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