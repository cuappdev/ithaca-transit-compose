package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.robotoFamily
import com.cornellappdev.transit.ui.viewmodels.FavoritesViewModel
import com.cornellappdev.transit.ui.viewmodels.LocationUIState
import com.google.android.gms.maps.model.LatLng


/**
 * Contents of BottomSheet in HomeScreen
 * @param editText The text in the edit/done button
 * @param editState The state of the lazyRow, whether it's currently being edited or not
 * @param data The data the lazyRow contains
 * @param onclick The Function to run when the edit/done button is clicked
 */
@Composable
fun BottomSheetContent(
    editText: String,
    editState: Boolean,
    onclick: () -> Unit,
    addOnClick: () -> Unit,
    removeOnClick: () -> Unit,
    changeEndLocation: (LocationUIState) -> Unit,
    favoritesViewModel: FavoritesViewModel = hiltViewModel(),
    navController: NavController
) {

    val data = favoritesViewModel.favoritesStops.collectAsState().value.toList()

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = 20.dp,
                    start = 20.dp,
                    end = 20.dp,
                    top = 2.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Favorites",
                fontWeight = FontWeight(600),
                fontSize = 20.sp,
                fontFamily = robotoFamily
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = editText,
                modifier = Modifier.clickable(onClick = onclick),
                color = TransitBlue,
                textAlign = TextAlign.Right,
                fontSize = 14.sp,
                fontWeight = FontWeight(500),
                fontFamily = robotoFamily
            )
        }

        LazyRow(modifier = Modifier.padding(bottom = 20.dp)) {
            item {
                LocationItem(
                    image = painterResource(id = R.drawable.ellipse),
                    editImage = painterResource(id = R.drawable.add_icon),
                    label = "Add",
                    sublabel = "",
                    editing = editState,
                    {},
                    addOnClick = addOnClick,
                    removeOnClick = {}
                )
            }
            items(data) {
                LocationItem(
                    image = painterResource(id = R.drawable.location_icon),
                    editImage = painterResource(id = R.drawable.location_icon_edit),
                    label = it.name,
                    sublabel = "",
                    editing = editState,
                    {
                        changeEndLocation(
                            LocationUIState.Place(
                                it.name,
                                LatLng(
                                    it.latitude,
                                    it.longitude
                                )
                            )
                        )
                        navController.navigate("route")
                    },
                    addOnClick = {},
                    removeOnClick = { favoritesViewModel.removeFavorite(it); removeOnClick() },
                )
            }
        }
    }
}