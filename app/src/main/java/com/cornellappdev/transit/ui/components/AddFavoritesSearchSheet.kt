package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.ui.theme.TextButtonGray
import com.cornellappdev.transit.ui.theme.sfProDisplayFamily
import com.cornellappdev.transit.ui.theme.sfProTextFamily
import com.cornellappdev.transit.ui.viewmodels.RouteViewModel

/**
 * Contents of AddFavorites BottomSheet
 * @param routeViewModel The routeViewModel used in the app
 * @param cancelOnClick The function to run when the cancel button is clicked
 */
@Composable
fun AddFavoritesSearchSheet(
    routeViewModel: RouteViewModel,
    cancelOnClick: () -> Unit,
) {


    // Search bar flow
    val searchBarValue = routeViewModel.searchQuery.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxHeight(0.92f)
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Add Favorites",
                        fontFamily = sfProDisplayFamily,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight(600),
                        fontSize = 20.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    TextButton(
                        onClick = cancelOnClick,
                        content = {
                            Text(
                                text = "Cancel",
                                fontFamily = sfProTextFamily,
                                fontStyle = FontStyle.Normal,
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp,
                                fontWeight = FontWeight(500),
                                color = TextButtonGray,
                            )
                        },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )

                }

            }

            SearchTextField(
                value = searchBarValue,
                setValue = { s -> routeViewModel.onQueryChange(s) },
                placeholderText = "Search",
                singleLine = true,
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 7.dp,
                    bottom = 11.dp
                ),
                height = 50.dp,
                prefix = {
                    Icon(
                        Icons.Outlined.Search,
                        "Search",
                        //TODO: replace with actual search function
                        modifier = Modifier.clickable { routeViewModel.onQueryChange("") })
                },
                suffix = {
                    Icon(
                        Icons.Outlined.Clear,
                        "Clear",
                        modifier = Modifier.clickable { routeViewModel.onQueryChange("") })
                }
            )

        }
    }
}