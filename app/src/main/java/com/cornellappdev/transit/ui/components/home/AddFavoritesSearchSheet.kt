package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.ui.components.LoadingLocationItems
import com.cornellappdev.transit.ui.theme.DividerGray
import com.cornellappdev.transit.ui.theme.IconGray
import com.cornellappdev.transit.ui.theme.MetadataGray
import com.cornellappdev.transit.ui.theme.TextButtonGray
import com.cornellappdev.transit.ui.theme.robotoFamily


/**
 * Contents of AddFavorites BottomSheet
 * @param cancelOnClick The function to run when the cancel button is clicked
 * @param onItemClick The function to run when a menu item from a search is clicked
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFavoritesSearchSheet(
    addSearchBarValue: String,
    placeQueryResponse: ApiResponse<List<Place>>,
    onQueryChange: (String) -> Unit,
    onClearChange: () -> Unit,
    cancelOnClick: () -> Unit,
    onItemClick: (Place) -> Unit,
) {

    var addSearchActive by remember { mutableStateOf(false) }

    val keyboardController = LocalSoftwareKeyboardController.current


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
                        fontFamily = robotoFamily,
                        fontStyle = FontStyle.Normal,
                        fontWeight = FontWeight(600),
                        fontSize = 20.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    TextButton(
                        onClick = {
                            cancelOnClick()
                            keyboardController?.hide()
                        },
                        content = {
                            Text(
                                text = "Cancel",
                                fontFamily = robotoFamily,
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
                inputField = {
                    SearchBarDefaults.InputField(
                        query = addSearchBarValue,
                        onQueryChange = onQueryChange,
                        onSearch = {}, // Search occurs automatically when typing
                        expanded = addSearchActive,
                        onExpandedChange = { isExpanded ->
                            addSearchActive = isExpanded
                        },
                        placeholder = { Text(text = "Search for a stop to add") },
                        leadingIcon = { Icon(Icons.Outlined.Search, "Search", tint = IconGray) },
                        colors = SearchBarDefaults.inputFieldColors(
                            focusedTextColor = Color.Black,
                            focusedPlaceholderColor = MetadataGray,
                            unfocusedTextColor = Color.Black,
                            unfocusedPlaceholderColor = MetadataGray
                        ),
                        trailingIcon = {
                            if (addSearchBarValue.isNotEmpty()) {
                                Icon(
                                    Icons.Outlined.Clear,
                                    "Clear",
                                    modifier = Modifier.clickable { onClearChange() })
                            }
                        },
                        modifier = Modifier.border(
                            width = 2.dp,
                            color = DividerGray,
                            shape = RoundedCornerShape(size = 8.dp)
                        )
                    )
                },
                expanded = addSearchActive,
                onExpandedChange = { isExpanded -> addSearchActive = isExpanded },
                shape = RoundedCornerShape(size = 8.dp),
                colors = SearchBarDefaults.colors(
                    containerColor = Color.White,
                    dividerColor = DividerGray,
                )
            ) {
                LoadingLocationItems(
                    placeQueryResponse,
                    onClick = { onItemClick(it); keyboardController?.hide() }
                )
            }
        }
    }
}