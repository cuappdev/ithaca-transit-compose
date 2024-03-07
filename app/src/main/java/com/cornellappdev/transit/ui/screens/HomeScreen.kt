package com.cornellappdev.transit.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.viewmodels.HomeViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.accompanist.permissions.rememberPermissionState
import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.ui.components.BottomSheetContent
import com.cornellappdev.transit.ui.components.LocationItem
import com.cornellappdev.transit.ui.components.MenuItem
import com.cornellappdev.transit.ui.theme.TransitBlue

/**
 * Composable for the home screen
 */
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    // Permissions dialog
    val permissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value && !permissionState.status.isGranted) {
        AlertDialog(
            onDismissRequest = {

                openDialog.value = false
            }
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(80.dp),
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(40.dp)) {
                    Text(
                        text = stringResource(R.string.permission_dialog_text)
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    TextButton(
                        onClick = {
                            permissionState.launchPermissionRequest()
                            openDialog.value = false
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Allow")
                    }
                }
            }
        }
    }


    // Collect flow of rides through API
    val stopsApiResponse = homeViewModel.stopFlow.collectAsState().value
    val placesResponse = homeViewModel.placeData.collectAsState().value


    //Map camera
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(homeViewModel.defaultIthaca, 12f)
    }

    // Search bar active/inactive
    var searchActive by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = permissionState.status.isGranted),
            onMapClick = { searchActive = false },
            onMapLongClick = { searchActive = false }
        ) {
            //TODO: Not actually any sort of functionality, just demonstrate connection to backend
            when (stopsApiResponse) {
                is ApiResponse.Error -> {
                }

                is ApiResponse.Pending -> {

                }

                is ApiResponse.Success -> {
                    stopsApiResponse.data.stops.forEach { stop ->
                        Marker(
                            state = MarkerState(
                                position = LatLng(
                                    stop.latitude,
                                    stop.longitude
                                )
                            ),
                            title = stop.name
                        )

                    }
                }
            }

        }

        Column(
            modifier = Modifier
                .padding(top = 80.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DockedSearchBar(
                query = homeViewModel.searchQuery.value,
                onQueryChange = { s -> homeViewModel.onQueryChange(s) },
                onSearch = { it -> searchActive = false; homeViewModel.onSearch(it) },
                active = searchActive,
                onActiveChange = { b -> searchActive = b },
                shape = SearchBarDefaults.dockedShape,
                colors = SearchBarDefaults.colors(
                    containerColor = Color.White,
                    dividerColor = Color.Gray,
                ),
                leadingIcon = { Icon(Icons.Outlined.Search, "Search") },
                trailingIcon = { Icon(Icons.Outlined.Info, "Info") },
                placeholder = { Text(text = stringResource(R.string.search_placeholder)) }

            ) {
                LazyColumn {
                    items(placesResponse) {
                        if (!homeViewModel.searchQuery.value.isBlank() && it.lowercase()
                                .contains(homeViewModel.searchQuery.value.lowercase())
                        ) {
                            MenuItem(Icons.Filled.Place, label = it, sublabel = it)
                        }
                    }
                }
            }
        }
    }

    val scaffoldState = rememberBottomSheetScaffoldState()
    var editState by remember {
        mutableStateOf(false)
    }
    var txt by remember {
        mutableStateOf("Edit")
    }
    val data = placesResponse.toMutableList()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetSwipeEnabled = true,
        sheetContent = {
            BottomSheetContent(txt, editState, data){
                editState = editState == false
                txt = if (editState) {
                    "Done"
                } else {
                    "Edit"
                }
            }
        }
    ) {
    }
}
