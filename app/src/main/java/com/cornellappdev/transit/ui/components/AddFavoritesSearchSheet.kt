package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.ui.theme.DividerGrey
import com.cornellappdev.transit.ui.theme.TextButtonGray
import com.cornellappdev.transit.ui.theme.sfProDisplayFamily
import com.cornellappdev.transit.ui.theme.sfProTextFamily
import com.cornellappdev.transit.ui.viewmodels.FavoritesViewModel
import com.cornellappdev.transit.ui.viewmodels.HomeViewModel

/**
 * Contents of AddFavorites BottomSheet
 * @param homeViewModel the homeViewModel used in the app
 * @param cancelOnClick The function to run when the cancel button is clicked
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFavoritesSearchSheet(
    homeViewModel: HomeViewModel,
    favoritesViewModel: FavoritesViewModel,
    cancelOnClick: () -> Unit,
) {

    val addSearchBarValue = homeViewModel.addSearchQuery.collectAsState().value
    val addQueryResponse = homeViewModel.addQueryFlow.collectAsState().value
    var addSearchActive by remember { mutableStateOf(false) }
    val favorites = favoritesViewModel.favoriteStops.collectAsState().value

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

            DockedSearchBar(
                query = addSearchBarValue,
                onQueryChange = { s -> homeViewModel.onAddQueryChange(s) },
                onSearch = { addSearchActive = false; homeViewModel.onSearch(it) },
                active = addSearchActive,
                onActiveChange = { b -> addSearchActive = b },
                shape = RoundedCornerShape(size = 8.dp),
                colors = SearchBarDefaults.colors(
                    containerColor = Color.White,
                    dividerColor = DividerGrey,
                ),
                leadingIcon = { Icon(Icons.Outlined.Search, "Search") },
                trailingIcon = { Icon(Icons.Outlined.Info, "Info") },
                placeholder = { Text(text = "Search for a stop to add") }

            ) {
                LazyColumn {
                    items(addQueryResponse) {
                        MenuItem(
                            Icons.Filled.Place,
                            label = it.name,
                            sublabel = it.type,
                            onClick = {
                                //Add to favorites if not in favorites
                                if (it in favorites){
                                    //Do something
                                }else{
                                    favoritesViewModel.addFavorite(it.name)
                                }
                            })
                    }
                }
            }

        }
    }
}