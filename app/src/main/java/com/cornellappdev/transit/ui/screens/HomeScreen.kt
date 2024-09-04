package com.cornellappdev.transit.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.components.AddFavoritesSearchSheet
import com.cornellappdev.transit.ui.components.BottomSheetContent
import com.cornellappdev.transit.ui.components.MenuItem
import com.cornellappdev.transit.ui.components.SearchSuggestions
import com.cornellappdev.transit.ui.theme.DividerGrey
import com.cornellappdev.transit.ui.viewmodels.FavoritesViewModel
import com.cornellappdev.transit.ui.viewmodels.HomeViewModel
import com.cornellappdev.transit.ui.viewmodels.RouteViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

/**
 * Composable for the home screen
 */
@OptIn(
    ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    routeViewModel: RouteViewModel = hiltViewModel(),
    navController: NavController,
    favoritesViewModel: FavoritesViewModel = hiltViewModel()
) {
    // Permissions dialog
    val permissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    var openDialog by remember { mutableStateOf(true) }

    if (openDialog && !permissionState.status.isGranted) {
        AlertDialog(
            onDismissRequest = {

                openDialog = false
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
                            openDialog = false
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Allow")
                    }
                }
            }
        }
    }

    val context = LocalContext.current

    if (permissionState.status.isGranted) {
        homeViewModel.instantiateLocation(context)
    }

    val currentLocationValue = homeViewModel.currentLocation.collectAsState().value

    // Search bar flow
    val searchBarValue = homeViewModel.searchQuery.collectAsState().value

    // Collect flow of rides through API
    val stopsApiResponse = homeViewModel.stopFlow.collectAsState().value
    val queryResponse = homeViewModel.queryFlow.collectAsState().value
    val placesResponse = homeViewModel.placeData.collectAsState().value

    //Collect flow of route through API
    val routeApiResponse = homeViewModel.lastRouteFlow.collectAsState().value


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
            properties = MapProperties(
                isMyLocationEnabled = permissionState.status.isGranted
            ),
            onMapClick = { searchActive = false },
            onMapLongClick = { searchActive = false },
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        ) {

        }

        Column(
            modifier = Modifier
                .padding(top = 80.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DockedSearchBar(
                query = searchBarValue,
                onQueryChange = { s -> homeViewModel.onQueryChange(s) },
                onSearch = { it -> searchActive = false; homeViewModel.onSearch(it) },
                active = searchActive,
                onActiveChange = { b -> searchActive = b },
                shape = RoundedCornerShape(size = 8.dp),
                colors = SearchBarDefaults.colors(
                    containerColor = Color.White,
                    dividerColor = DividerGrey,
                ),
                leadingIcon = { Icon(Icons.Outlined.Search, "Search") },
                trailingIcon = { Icon(Icons.Outlined.Info, "Info") },
                placeholder = { Text(text = stringResource(R.string.search_placeholder)) }

            ) {

                //If query is blank, display recents and favorites
                if (searchBarValue.isBlank()) {
                    SearchSuggestions(
                        favorites = emptyList(),
                        recents = emptyList(),
                        onFavoriteAdd = {},
                        onRecentClear = {}
                    )
                } else {
                    LazyColumn {
                        items(queryResponse) {
                            MenuItem(
                                Icons.Filled.Place,
                                label = it.name,
                                sublabel = it.type,
                                onClick = {
                                })

                        }
                    }
                }
            }
        }
    }

    //SheetState for FavoritesBottomSheet
    val scaffoldState = rememberBottomSheetScaffoldState()

    var editState by remember {
        mutableStateOf(false)
    }
    var txt by remember {
        mutableStateOf("Edit")
    }

    val data = favoritesViewModel.favoriteStops.collectAsState().value

    //sheetState for AddFavorites BottomSheet
    val addSheetState = androidx.compose.material.rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        confirmValueChange = {
            true
        }
    )

    val scope = rememberCoroutineScope()

    // Favorites BottomSheet
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetSwipeEnabled = true,
        sheetContainerColor = Color.White,
        sheetContent = {
            BottomSheetContent(
                editText = txt,
                editState = editState, data = data, onclick = {
                    editState = editState == false
                    txt = if (editState) {
                        "Done"
                    } else {
                        "Edit"
                    }
                }, addOnClick = {
                    scope.launch {
                        addSheetState.show()
                    }
                }, navController = navController
            )
        }
    ) {}

    // AddFavorites BottomSheet
    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(16.dp),
        sheetBackgroundColor = Color.White,
        sheetState = addSheetState,
        sheetContent = {
            AddFavoritesSearchSheet(routeViewModel = routeViewModel, homeViewModel=homeViewModel, favoritesViewModel=favoritesViewModel) {

                scope.launch {
                    addSheetState.hide()
                }

            }
        },
    ) {}
}
