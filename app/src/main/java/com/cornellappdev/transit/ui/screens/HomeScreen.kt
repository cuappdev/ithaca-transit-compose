package com.cornellappdev.transit.ui.screens

import android.Manifest
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BottomSheetDefaults
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.Place
import com.cornellappdev.transit.ui.components.home.AddFavoritesSearchSheet
import com.cornellappdev.transit.ui.components.home.BottomSheetContent
import com.cornellappdev.transit.ui.components.LoadingLocationItems
import com.cornellappdev.transit.ui.components.SearchSuggestions
import com.cornellappdev.transit.ui.components.home.EcosystemBottomSheetContent
import com.cornellappdev.transit.ui.theme.DetailsHeaderGray
import com.cornellappdev.transit.ui.theme.DividerGray
import com.cornellappdev.transit.ui.theme.IconGray
import com.cornellappdev.transit.ui.theme.MetadataGray
import com.cornellappdev.transit.ui.viewmodels.FavoritesViewModel
import com.cornellappdev.transit.ui.viewmodels.HomeViewModel
import com.cornellappdev.transit.ui.viewmodels.SearchBarUIState
import com.cornellappdev.transit.util.ECOSYSTEM_FLAG
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import io.morfly.compose.bottomsheet.material3.rememberBottomSheetState
import kotlinx.coroutines.launch

private enum class HomeSheetValue { Collapsed, PartiallyExpanded, Expanded }

/**
 * Composable for the home screen
 */
@OptIn(
    ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    navController: NavController,
    favoritesViewModel: FavoritesViewModel = hiltViewModel()
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val localDensity = LocalDensity.current

    // Permissions dialog
    val permissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    var openDialog by remember { mutableStateOf(true) }

    if (openDialog && !permissionState.status.isGranted) {
        LocationPermissionDialog(
            onDismissRequest = {
                openDialog = false
            },
            onClickNext = {
                permissionState.launchPermissionRequest()
                openDialog = false
            }
        )
    }

    if (permissionState.status.isGranted) {
        homeViewModel.instantiateLocation(context)
    }


    //SheetState for FavoritesBottomSheet
    val favoritesSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = remember {
            SheetState(
                skipPartiallyExpanded = false,
                initialValue = SheetValue.Expanded,
                skipHiddenState = true,
                density = localDensity
            )
        }
    )

    //sheetState for AddFavorites BottomSheet
    val addSheetState = rememberBottomSheetScaffoldState(
        bottomSheetState = remember {
            SheetState(
                skipPartiallyExpanded = true,
                initialValue = SheetValue.Hidden,
                density = localDensity,
                confirmValueChange = {
                    homeViewModel.onAddQueryChange("")
                    true
                }
            )
        }
    )

    // Filter sheet state
    // Using advanced-bottomsheet-compose from https://github.com/Morfly/advanced-bottomsheet-compose
    val filterSheetState = rememberBottomSheetState(
        initialValue = HomeSheetValue.Collapsed,
        defineValues = {
            HomeSheetValue.Collapsed at height(90.dp)

            //Peek to show filters
            // Bottom sheet offset is 50%, i.e. it takes 50% of the screen
            HomeSheetValue.PartiallyExpanded at offset(percent = 50)
            // Wrap full height
            HomeSheetValue.Expanded at offset(percent = 10)
        }
    )

    // Filter scaffold state
    val filterScaffoldState =
        io.morfly.compose.bottomsheet.material3.rememberBottomSheetScaffoldState(filterSheetState)

    // Main search bar flow
    val searchBarValue = homeViewModel.searchBarUiState.collectAsState().value

    // Favorited locations
    val favorites = favoritesViewModel.favoritesStops.collectAsState().value

    // Add search bar
    val addSearchBarValue = homeViewModel.addSearchQuery.collectAsState().value

    // Add search bar query response
    val placeQueryResponse = homeViewModel.placeQueryFlow.collectAsState().value

    val filterStateValue = homeViewModel.filterState.collectAsState().value

    val staticPlaces = homeViewModel.staticPlacesFlow.collectAsState().value

    // Main search bar active/inactive
    var searchActive by remember { mutableStateOf(false) }

    // Intercept clicks outside of search bar and disable search
    @Composable
    fun Modifier.onTapDisableSearch(): Modifier {
        return this.pointerInput(Unit) {
            detectTapGestures(
                onPress = {
                    searchActive = false
                }
            )
        }
    }

    // Close search when not searching and close bottom sheet when searching
    LaunchedEffect(searchActive) {
        if (!searchActive) {
            homeViewModel.onQueryChange("")
        } else {
            filterSheetState.animateTo(HomeSheetValue.Collapsed)
        }
    }

    //Map camera
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(homeViewModel.defaultIthaca, 12f)
    }

    // Main Screen
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
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        )

        // Overlay transparent box to intercept clicks to disable search
        if (searchActive) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .onTapDisableSearch()
            )
        }

        Column(
            modifier = Modifier
                .padding(top = 80.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HomeScreenSearchBar(
                searchBarValue,
                onQueryChange = { s -> homeViewModel.onQueryChange(s) },
                onSearch = {}, // Search occurs automatically when typing
                expanded = searchActive,
                onExpandedChange = { isExpanded -> searchActive = isExpanded },
                onInfoClick = {
                    homeViewModel.onQueryChange("")
                    navController.navigate("settings")
                },
                onFavoriteAdd = {
                    scope.launch {
                        addSheetState.bottomSheetState.expand()
                    }
                },
                onRecentClear = {
                    homeViewModel.clearRecents()
                },
                onItemClick = {
                    homeViewModel.beginRouteOptions(it)
                    navController.navigate("route")
                }
            )
        }
    }

    var editState by remember {
        mutableStateOf(false)
    }
    var editText by remember {
        mutableStateOf("Edit")
    }


    // Favorites BottomSheet (Filters BottomSheet for ecosystem)
    if (ECOSYSTEM_FLAG) {
        io.morfly.compose.bottomsheet.material3.BottomSheetScaffold(
            scaffoldState = filterScaffoldState,
            sheetSwipeEnabled = true,
            sheetContainerColor = DetailsHeaderGray,
            sheetDragHandle = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .onTapDisableSearch()
                        .fillMaxWidth()
                ) {
                    BottomSheetDefaults.DragHandle()
                }
            },
            sheetContent = {
                EcosystemBottomSheetContent(
                    filters = homeViewModel.filterList,
                    activeFilter = filterStateValue,
                    onFilterClick = {
                        homeViewModel.filterState.value = it
                    },
                    modifier = Modifier.onTapDisableSearch(),
                    staticPlaces = staticPlaces,
                    navigateToPlace = {
                        homeViewModel.beginRouteOptions(it)
                        navController.navigate("route")
                    }
                )
            },
            content = {}
        )
    } else {
        BottomSheetScaffold(
            scaffoldState = favoritesSheetState,
            sheetSwipeEnabled = true,
            sheetPeekHeight = 90.dp,
            sheetContainerColor = Color.White,
            sheetContent = {
                BottomSheetContent(
                    editText = editText,
                    editState = editState,
                    favoritesData = favorites,
                    onEditToggleClick = {
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
                        favoritesViewModel.removeFavorite(it)
                    },
                    itemOnClick = {
                        homeViewModel.beginRouteOptions(it)
                        navController.navigate("route")
                    }
                )
            },
            content = {}
        )

        // AddFavorites BottomSheet
        BottomSheetScaffold(
            sheetShape = RoundedCornerShape(16.dp),
            scaffoldState = addSheetState,
            sheetContainerColor = Color.White,
            sheetPeekHeight = 0.dp,
            sheetContent = {
                AddFavoritesSearchSheet(
                    addSearchBarValue = addSearchBarValue,
                    placeQueryResponse = placeQueryResponse,
                    cancelOnClick = {
                        scope.launch {
                            addSheetState.bottomSheetState.hide()
                            homeViewModel.onAddQueryChange("")
                        }
                    },
                    onItemClick = {
                        scope.launch {
                            if (it !in favorites) {
                                addSheetState.bottomSheetState.hide()
                                homeViewModel.onAddQueryChange("")
                                favoritesViewModel.addFavorite(it)
                            } else {
                                Toast.makeText(
                                    context,
                                    "${it.name} is already favorited!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    },
                    onQueryChange = { s -> homeViewModel.onAddQueryChange(s) },
                    onClearChange = { homeViewModel.onAddQueryChange("") }
                )
            },
            content = {}
        )
    }
}

/**
 * Pop-up dialog to prompt for location permissions
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun LocationPermissionDialog(onDismissRequest: () -> Unit, onClickNext: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismissRequest
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
                    painter = painterResource(id = R.drawable.navi_icon_rounded),
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
                    onClick = onClickNext,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Grant Permissions")
                }
            }
        }
    }
}

/**
 * Search bar containing queried places or favorites/recents
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenSearchBar(
    searchBarValue: SearchBarUIState,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onInfoClick: () -> Unit,
    onFavoriteAdd: () -> Unit,
    onRecentClear: () -> Unit,
    onItemClick: (Place) -> Unit
) {

    //If query is blank, display recents and favorites
    DockedSearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = (searchBarValue as? SearchBarUIState.Query)?.queryText ?: "",
                onQueryChange = onQueryChange,
                onSearch = onSearch,
                expanded = expanded,
                onExpandedChange = onExpandedChange,
                placeholder = { Text(text = stringResource(R.string.search_placeholder)) },
                leadingIcon = { Icon(Icons.Outlined.Search, "Search", tint = IconGray) },
                trailingIcon = {
                    Icon(
                        Icons.Outlined.Info,
                        "Info",
                        Modifier.clickable {
                            onInfoClick()
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
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        shape = RoundedCornerShape(size = 8.dp),
        colors = SearchBarDefaults.colors(
            containerColor = Color.White,
            dividerColor = DividerGray,
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .background(Color.White)
        ) {
            //If query is blank, display recents and favorites
            when (searchBarValue) {
                is SearchBarUIState.RecentAndFavorites -> {
                    SearchSuggestions(
                        favorites = searchBarValue.favorites,
                        recents = searchBarValue.recents,
                        onFavoriteAdd = onFavoriteAdd,
                        onRecentClear = onRecentClear,
                        onItemClick = onItemClick,
                    )
                }

                is SearchBarUIState.Query -> {
                    LoadingLocationItems(
                        searchBarValue.searched,
                        onClick = onItemClick
                    )
                }
            }
        }

    }
}
