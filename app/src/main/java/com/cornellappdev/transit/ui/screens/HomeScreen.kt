package com.cornellappdev.transit.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.cornellappdev.transit.R
import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.ui.components.AddFavoritesSearchSheet
import com.cornellappdev.transit.ui.components.BottomSheetContent
import com.cornellappdev.transit.ui.components.LocationNotFound
import com.cornellappdev.transit.ui.components.MenuItem
import com.cornellappdev.transit.ui.components.ProgressCircle
import com.cornellappdev.transit.ui.components.SearchSuggestions
import com.cornellappdev.transit.ui.theme.DividerGray
import com.cornellappdev.transit.ui.theme.IconGray
import com.cornellappdev.transit.ui.theme.MetadataGray
import com.cornellappdev.transit.ui.viewmodels.HomeViewModel
import com.cornellappdev.transit.ui.viewmodels.LocationUIState
import com.cornellappdev.transit.ui.viewmodels.SearchBarUIState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
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
    navController: NavController,
) {
    // Permissions dialog
    val permissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)
    var openDialog by remember { mutableStateOf(true) }

    val scope = rememberCoroutineScope()

    //SheetState for FavoritesBottomSheet
    val favoritesSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            skipPartiallyExpanded = false,
            initialValue = SheetValue.Expanded,
            skipHiddenState = true,
            density = LocalDensity.current
        )
    )

    //sheetState for AddFavorites BottomSheet
    val addSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = SheetState(
            skipPartiallyExpanded = true,
            initialValue = SheetValue.Hidden,
            density = LocalDensity.current
        )
    )

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
                shape = RoundedCornerShape(20.dp),
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(40.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.app_icon_rounded),
                        contentDescription = stringResource(R.string.app_name),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(30.dp))
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
                        Text("Grant Permissions")
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
    val searchBarValue = homeViewModel.searchBarUiState.collectAsState().value

    //Collect flow of route through API
    val routeApiResponse = homeViewModel.lastRouteFlow.collectAsState().value

    //Map camera
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(homeViewModel.defaultIthaca, 12f)
    }

    // Search bar active/inactive
    var searchActive by remember { mutableStateOf(false) }

    if (!searchActive) {
        homeViewModel.onQueryChange("")
    }

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
        )

        Column(
            modifier = Modifier
                .padding(top = 80.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            //If query is blank, display recents and favorites
            DockedSearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = (searchBarValue as? SearchBarUIState.Query)?.queryText ?: "",
                        onQueryChange = { s -> homeViewModel.onQueryChange(s) },
                        onSearch = { it -> searchActive = false; homeViewModel.onSearch(it) },
                        expanded = searchActive,
                        onExpandedChange = { b -> searchActive = b },
                        placeholder = { Text(text = stringResource(R.string.search_placeholder)) },
                        leadingIcon = { Icon(Icons.Outlined.Search, "Search", tint = IconGray) },
                        trailingIcon = {
                            Icon(
                                Icons.Outlined.Info,
                                "Info",
                                Modifier.clickable {
                                    homeViewModel.onQueryChange("")
                                    navController.navigate("settings")
                                },
                                tint = IconGray
                            )
                        },
                        colors = SearchBarDefaults.inputFieldColors(
                            focusedTextColor = Color.Black,
                            focusedPlaceholderColor = MetadataGray,
                            unfocusedTextColor = Color.Black,
                            unfocusedPlaceholderColor = MetadataGray
                        ),
                    )
                },
                expanded = searchActive,
                onExpandedChange = { b -> searchActive = b },
                shape = RoundedCornerShape(size = 8.dp),
                colors = SearchBarDefaults.colors(
                    containerColor = Color.White,
                    dividerColor = DividerGray,
                )
            ) {
                //If query is blank, display recents and favorites
                when (searchBarValue) {
                    is SearchBarUIState.RecentAndFavorites -> {
                        SearchSuggestions(
                            favorites = searchBarValue.favorites,
                            recents = searchBarValue.recents,
                            onFavoriteAdd = {
                                scope.launch {
                                    addSheetState.bottomSheetState.expand()
                                }
                            },
                            onRecentClear = {
                                homeViewModel.clearRecents()
                            },
                            navController = navController,
                            changeStartLocation = { place ->
                                homeViewModel.changeStartLocation(place)
                            },
                            changeEndLocation = { place ->
                                homeViewModel.changeEndLocation(place)
                            },
                            onStopPressed = { place ->
                                homeViewModel.addRecent(place)
                            },
                        )
                    }

                    is SearchBarUIState.Query -> {
                        when (searchBarValue.searched) {
                            is ApiResponse.Error -> {
                                LocationNotFound()
                            }

                            is ApiResponse.Pending -> {
                                ProgressCircle()
                            }

                            is ApiResponse.Success -> {
                                if (searchBarValue.searched.data.isEmpty()) {
                                    LocationNotFound()
                                } else {
                                    LazyColumn {
                                        items(searchBarValue.searched.data) {
                                            MenuItem(
                                                type = it.type,
                                                label = it.name,
                                                sublabel = it.subLabel,
                                                onClick = {
                                                    homeViewModel.addRecent(it)
                                                    homeViewModel.changeStartLocation(
                                                        LocationUIState.CurrentLocation
                                                    )
                                                    homeViewModel.changeEndLocation(
                                                        LocationUIState.Place(
                                                            it.name,
                                                            LatLng(
                                                                it.latitude,
                                                                it.longitude
                                                            )
                                                        )
                                                    )
                                                    homeViewModel.onQueryChange("")
                                                    navController.navigate("route")
                                                })

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    var editState by remember {
        mutableStateOf(false)
    }
    var editText by remember {
        mutableStateOf("Edit")
    }

    // Favorites BottomSheet
    BottomSheetScaffold(
        scaffoldState = favoritesSheetState,
        sheetSwipeEnabled = true,
        sheetPeekHeight = 90.dp,
        sheetContainerColor = Color.White,
        sheetContent = {
            BottomSheetContent(
                editText = editText,
                editState = editState,
                onclick = {
                    editState = !editState
                    editText = if (editState) {
                        "Done"
                    } else {
                        "Edit"
                    }
                }, addOnClick = {
                    scope.launch {
                        if (!favoritesSheetState.bottomSheetState.hasExpandedState) {
                            favoritesSheetState.bottomSheetState.expand()
                        }
                        if (editState) {
                            editState = false
                        }
                        addSheetState.bottomSheetState.expand()
                    }
                },
                removeOnClick = {
                    scope.launch {
                        favoritesSheetState.bottomSheetState.expand()
                    }
                },
                changeEndLocation = { place ->
                    homeViewModel.changeEndLocation(place)
                },
                navController = navController
            )
        }
    ) {}

    // AddFavorites BottomSheet
    BottomSheetScaffold(
        sheetShape = RoundedCornerShape(16.dp),
        scaffoldState = addSheetState,
        sheetContainerColor = Color.White,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            AddFavoritesSearchSheet(
                homeViewModel = homeViewModel,
            ) {
                scope.launch {
                    addSheetState.bottomSheetState.hide()
                    homeViewModel.onAddQueryChange("")
                }
            }
        },

        ) {}
}
