package com.cornellappdev.transit.ui.screens

import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.MapState
import com.cornellappdev.transit.models.Route
import com.cornellappdev.transit.models.RouteOptions
import com.cornellappdev.transit.models.Transport
import com.cornellappdev.transit.models.toTransport
import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.ui.components.CurrentLocationItem
import com.cornellappdev.transit.ui.components.DatePicker
import com.cornellappdev.transit.ui.components.LoadingLocationItems
import com.cornellappdev.transit.ui.components.LocationNotFound
import com.cornellappdev.transit.ui.components.MenuItem
import com.cornellappdev.transit.ui.components.ProgressCircle
import com.cornellappdev.transit.ui.components.RouteCell
import com.cornellappdev.transit.ui.components.SearchTextField
import com.cornellappdev.transit.ui.components.TernarySelector
import com.cornellappdev.transit.ui.components.TimePicker
import com.cornellappdev.transit.ui.components.TransitPullToRefreshBox
import com.cornellappdev.transit.ui.theme.DividerGray
import com.cornellappdev.transit.ui.theme.IconGray
import com.cornellappdev.transit.ui.theme.MetadataGray
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.SecondaryText
import com.cornellappdev.transit.ui.theme.Style
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.robotoFamily
import com.cornellappdev.transit.ui.viewmodels.ArriveByUIState
import com.cornellappdev.transit.ui.viewmodels.LocationUIState
import com.cornellappdev.transit.ui.viewmodels.RouteViewModel
import com.cornellappdev.transit.ui.viewmodels.SearchBarUIState
import com.cornellappdev.transit.util.TimeUtils
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Date

/**
 * Composable for the route screen, which specifies a location, destination, and routes between them
 */
@OptIn(
    ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
fun RouteScreen(
    navController: NavController,
    routeViewModel: RouteViewModel
) {

    val selectedRoute = routeViewModel.selectedRoute.collectAsState().value

    val keyboardController = LocalSoftwareKeyboardController.current

    val lastRoute = routeViewModel.lastRouteFlow.collectAsState().value

    val startSheetState =
        rememberModalBottomSheetState(
            ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmValueChange = {
                keyboardController?.hide()
                true
            }
        )

    val destSheetState =
        rememberModalBottomSheetState(
            ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmValueChange = {
                keyboardController?.hide()
                true
            }
        )

    val coroutineScope = rememberCoroutineScope()


    /**
     * Separate bottom sheets for start query and destination query, wrapping composable of
     * entire main menu screen
     */
    ModalBottomSheetLayout(
        sheetState = startSheetState,
        sheetContent = {
            RouteOptionsSearchSheet(routeViewModel, onCancelClicked = {
                coroutineScope.launch {
                    startSheetState.hide()
                }
            }, onItemClicked = {
                coroutineScope.launch {
                    startSheetState.hide()
                }
            }, isStart = true)
        },
        sheetShape = RoundedCornerShape(8.dp)
    ) {
        ModalBottomSheetLayout(
            sheetState = destSheetState,
            sheetContent = {
                RouteOptionsSearchSheet(routeViewModel, onCancelClicked = {
                    coroutineScope.launch {
                        destSheetState.hide()
                    }
                }, onItemClicked = {
                    coroutineScope.launch {
                        destSheetState.hide()
                    }
                }, isStart = false)
            },
            sheetShape = RoundedCornerShape(8.dp)
        ) {
            RouteOptionsMainMenu(
                routeViewModel = routeViewModel,
                navController = navController,
                coroutineScope = coroutineScope,
                startLocation = selectedRoute.startPlace,
                endLocation = selectedRoute.endPlace,
                lastRoute = lastRoute,
                startSheetState = startSheetState,
                destSheetState = destSheetState
            ) { route ->
                routeViewModel.setMapState(
                    MapState(
                        true,
                        route
                    )
                );navController.navigate("details")
            }
        }
    }

}

/**
 * Bottom sheet to select Leave Now/Leave At/Arrive By
 */
@Composable
private fun ArriveByBottomSheet(
    routeViewModel: RouteViewModel,
    onCancelClicked: () -> Unit = {}, onDoneClicked: () -> Unit = {
    }
) {

    Column(
        modifier = Modifier.height(
            240.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                TextButton(
                    onClick = onCancelClicked,
                    content = {
                        Text(
                            text = "Cancel",
                            fontFamily = robotoFamily,
                            fontStyle = FontStyle.Normal,
                            textAlign = TextAlign.Center,
                            color = SecondaryText
                        )
                    },
                    modifier = Modifier.align(Alignment.CenterStart)
                )
                TextButton(
                    onClick = {
                        onDoneClicked();
                        if (routeViewModel.selectedArriveByButton.value == 0) {
                            routeViewModel.changeArriveBy(
                                ArriveByUIState.LeaveNow()
                            )
                        } else if (routeViewModel.selectedArriveByButton.value == 1) {
                            routeViewModel.changeArriveBy(
                                ArriveByUIState.LeaveAt(
                                    date = TimeUtils.dateTimeFormatter.parse(
                                        routeViewModel.dateState.value + " " + routeViewModel.timeState.value
                                    )
                                )
                            )
                        } else {
                            routeViewModel.changeArriveBy(
                                ArriveByUIState.ArriveBy(
                                    date = TimeUtils.dateTimeFormatter.parse(
                                        routeViewModel.dateState.value + " " + routeViewModel.timeState.value
                                    )
                                )
                            )
                        }
                    },
                    content = {
                        Text(
                            text = "Done",
                            fontFamily = robotoFamily,
                            fontStyle = FontStyle.Normal,
                            textAlign = TextAlign.Center,
                            color = TransitBlue
                        )
                    },
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }
        }

        TernarySelector(
            firstButtonLabel = "Leave Now",
            secondButtonLabel = "Leave At",
            thirdButtonLabel = "Arrive By",
            buttonWidth = 120.dp,
            buttonHeight = 40.dp,
            selected = routeViewModel.selectedArriveByButton.value,
            onSelectChanged = { it ->
                routeViewModel.selectedArriveByButton.value = it
            }
        )

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                DatePicker(
                    date = routeViewModel.dateState.value,
                    dateFormatter = TimeUtils.dateFormatter,
                    modifier = Modifier.padding(horizontal = 5.dp),
                    disabled = routeViewModel.selectedArriveByButton.value == 0,
                    onDateChanged = { it -> routeViewModel.dateState.value = it }
                )
                TimePicker(
                    time = routeViewModel.timeState.value,
                    timeFormatter = TimeUtils.timeFormatter,
                    modifier = Modifier.padding(horizontal = 5.dp),
                    disabled = routeViewModel.selectedArriveByButton.value == 0,
                    onTimeChanged = { it -> routeViewModel.timeState.value = it }
                )
            }
        }
    }

}

/**
 * Main menu of Route Options
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
private fun RouteOptionsMainMenu(
    routeViewModel: RouteViewModel,
    navController: NavController,
    coroutineScope: CoroutineScope,
    startLocation: LocationUIState,
    endLocation: LocationUIState,
    lastRoute: ApiResponse<RouteOptions>,
    startSheetState: ModalBottomSheetState,
    destSheetState: ModalBottomSheetState,
    onClick: (Route) -> Unit
) {

    //SheetState for Arrive By Route Options
    val arriveBySheetState =
        rememberModalBottomSheetState(
            ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true
        )

    val arriveBy = routeViewModel.arriveByFlow.collectAsState().value


    ModalBottomSheetLayout(
        sheetState = arriveBySheetState,
        sheetContent = {
            ArriveByBottomSheet(
                routeViewModel,
                onCancelClicked = {
                    coroutineScope.launch {
                        arriveBySheetState.hide()
                    }
                }, onDoneClicked = {
                    coroutineScope.launch {
                        arriveBySheetState.hide()
                    }
                })
        },
        sheetShape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    Text(
                        text = "Route Options",
                        fontFamily = robotoFamily,
                        fontStyle = FontStyle.Normal
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowLeft,
                            contentDescription = ""
                        )
                    }
                }

            )

            Divider(thickness = 1.dp, color = DividerGray)
            Row(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                ConstraintLayout(modifier = Modifier.heightIn(max = 68.dp)) {
                    val (fromText, fromStop, toText, toStop, line) = createRefs()

                    Text(
                        text = "From", color = PrimaryText,
                        fontFamily = robotoFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp, modifier = Modifier.constrainAs(fromText) {
                            top.linkTo(parent.top, margin = 3.dp)
                        })

                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.boarding_stop),
                        contentDescription = "",
                        tint = IconGray,
                        modifier = Modifier
                            .size(12.dp)
                            .constrainAs(fromStop) {
                                top.linkTo(fromText.top, margin = 3.dp)
                                bottom.linkTo(fromText.bottom)
                                start.linkTo(fromText.end, margin = 12.dp)
                                end.linkTo(parent.end)
                            })
                    Text(
                        text = "To",
                        color = PrimaryText,
                        fontFamily = robotoFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        modifier = Modifier.constrainAs(toText) {
                            top.linkTo(fromText.bottom, margin = 24.dp)
                            bottom.linkTo(parent.bottom, margin = 2.dp)
                            start.linkTo(fromText.start)
                        }
                    )

                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.bus_route_line),
                        contentDescription = "",
                        tint = IconGray,
                        modifier = Modifier.constrainAs(line) {
                            top.linkTo(fromStop.bottom)
                            bottom.linkTo(toStop.top)
                            start.linkTo(fromStop.start)
                            end.linkTo(fromStop.end)
                        })

                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.destination_stop),
                        contentDescription = "",
                        tint = Color.Unspecified,
                        modifier = Modifier
                            .size(16.dp)
                            .constrainAs(toStop) {
                                top.linkTo(toText.top, margin = 3.dp)
                                bottom.linkTo(toText.bottom)
                                start.linkTo(fromStop.start)
                                end.linkTo(fromStop.end)
                            }
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier
                            .background(color = DividerGray, shape = RoundedCornerShape(8.dp))
                            .fillMaxWidth(0.9f)
                            .clickable {
                                coroutineScope.launch {
                                    when (startLocation) {
                                        is LocationUIState.CurrentLocation -> {
                                            routeViewModel.onQueryChange("")
                                        }

                                        is LocationUIState.Place -> {
                                            routeViewModel.onQueryChange(startLocation.name)
                                        }
                                    }
                                    startSheetState.show()
                                }
                            }
                    ) {
                        Text(
                            text = if (startLocation is LocationUIState.Place) startLocation.name else "Current Location",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = PrimaryText,
                            fontFamily = robotoFamily,
                            fontWeight = FontWeight.Normal, fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(color = DividerGray, shape = RoundedCornerShape(8.dp))
                            .fillMaxWidth(0.9f)
                            .clickable {
                                coroutineScope.launch {
                                    when (endLocation) {
                                        is LocationUIState.CurrentLocation -> {
                                            routeViewModel.onQueryChange("")
                                        }

                                        is LocationUIState.Place -> {
                                            routeViewModel.onQueryChange(endLocation.name)
                                        }
                                    }
                                    destSheetState.show()
                                }
                            }
                    ) {
                        Text(
                            text = if (endLocation is LocationUIState.Place) endLocation.name else "Current Location",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontFamily = robotoFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = R.drawable.swap
                    ),
                    contentDescription = "",
                    modifier = Modifier
                        .size(width = 20.dp, height = 20.dp)
                        .clickable { routeViewModel.swapLocations() },
                    tint = Color.Unspecified,
                )

            }

            Divider(thickness = 1.dp, color = DividerGray)

            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = R.drawable.clock
                    ),
                    contentDescription = "",
                    modifier = Modifier.size(12.dp),
                    tint = Color.Unspecified
                )
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            // If stale datetime in viewmodel, update to current datetime
                            if (TimeUtils.dateTimeFormatter.parse(
                                    routeViewModel.dateState.value + " " + routeViewModel.timeState.value
                                ) <= Date.from(Instant.now())
                            ) {
                                routeViewModel.dateState.value = TimeUtils.dateFormatter.format(
                                    Date.from(Instant.now())
                                )
                                routeViewModel.timeState.value = TimeUtils.timeFormatter.format(
                                    Date.from(Instant.now())
                                )
                            }
                            arriveBySheetState.show()
                        }
                    }
                ) {
                    Text(
                        text = arriveBy.label,
                        fontFamily = robotoFamily,
                        fontWeight = FontWeight.Normal,
                        color = MetadataGray, fontSize = 14.sp
                    )
                }
            }

            RouteList(lastRoute, onClick, onRefresh = {
                routeViewModel.refreshOptions()
            })
        }
    }

}

/**
 * Route cell with padding
 */
@Composable
private fun PaddedRouteCell(transport: Transport, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(12.dp)
            .clickable { onClick() }) {
        RouteCell(transport)
    }
}

/**
 * List of routes from a route query
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RouteList(
    lastRouteResponse: ApiResponse<RouteOptions>,
    onClick: (Route) -> Unit,
    onRefresh: () -> Unit,
) {
    TransitPullToRefreshBox(
        isRefreshing = lastRouteResponse is ApiResponse.Pending,
        onRefresh = onRefresh
    ) {
        LazyColumn(
            modifier = Modifier
                .background(color = DividerGray)
                .fillMaxSize()
        ) {
            when (lastRouteResponse) {
                is ApiResponse.Error -> {
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                        Text(
                            text = "No Routes Found",
                            fontFamily = robotoFamily,
                            fontWeight = FontWeight.Normal,
                            color = MetadataGray,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                }

                is ApiResponse.Pending -> {
                    //PullToRefreshBox provides the loading circle
                }

                is ApiResponse.Success -> {
                    lastRouteResponse.data.fromStop?.let {
                        items(it) { item ->
                            PaddedRouteCell(item.toTransport()) {
                                onClick(
                                    item
                                )
                            }
                        }
                    }
                    if (lastRouteResponse.data.boardingSoon?.isNotEmpty() == true) {
                        item {
                            Text(
                                "Boarding Soon From Nearby Stops",
                                style = Style.heading4,
                                color = MetadataGray,
                                modifier = Modifier.padding(
                                    start = 12.dp,
                                    top = if (lastRouteResponse.data.fromStop?.isEmpty() == true) 12.dp else 0.dp
                                )
                            )
                        }
                    }
                    lastRouteResponse.data.boardingSoon?.let {
                        items(it) { item ->
                            PaddedRouteCell(item.toTransport()) {
                                onClick(
                                    item
                                )
                            }
                        }
                    }
                    if (lastRouteResponse.data.walking?.isNotEmpty() == true) {
                        item {
                            Text(
                                "By Walking",
                                style = Style.heading4,
                                color = MetadataGray,
                                modifier = Modifier.padding(
                                    start = 12.dp,
                                    top = if (lastRouteResponse.data.boardingSoon?.isEmpty() == true) 12.dp else 0.dp
                                )
                            )
                        }
                    }
                    lastRouteResponse.data.walking?.let {
                        items(it) { item ->
                            PaddedRouteCell(item.toTransport()) {
                                onClick(
                                    item
                                )
                            }
                        }
                    }

                }
            }
        }

    }
}

/**
 * Route options select sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RouteOptionsSearchSheet(
    routeViewModel: RouteViewModel,
    onCancelClicked: () -> Unit,
    onItemClicked: () -> Unit,
    isStart: Boolean = true
) {

    // Search bar flow
    val searchBarValue = routeViewModel.searchBarUiState.collectAsState().value

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
                        text = "Route Options",
                        fontFamily = robotoFamily,
                        fontStyle = FontStyle.Normal,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    TextButton(
                        onClick = onCancelClicked,
                        content = {
                            Text(
                                text = "Cancel",
                                fontFamily = robotoFamily,
                                fontStyle = FontStyle.Normal,
                                textAlign = TextAlign.Center,
                                color = SecondaryText
                            )
                        },
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            }

            SearchTextField(
                value = if (searchBarValue is SearchBarUIState.Query) searchBarValue.queryText else "",
                setValue = { s -> routeViewModel.onQueryChange(s) },
                placeholderText = "Search",
                singleLine = true,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 7.dp, bottom = 11.dp),
                height = 50.dp,
                prefix = {
                    Icon(
                        Icons.Outlined.Search,
                        "Search",
                        tint = IconGray
                    )
                },
                suffix = {
                    Icon(
                        Icons.Outlined.Clear,
                        "Clear",
                        modifier = Modifier.clickable { routeViewModel.onQueryChange("") },
                        tint = IconGray
                    )
                }
            )
            when (searchBarValue) {
                is SearchBarUIState.Query -> {
                    LoadingLocationItems(
                        searchBarValue.searched,
                        onClick = {
                            if (isStart) {
                                routeViewModel.setStartPlace(
                                    LocationUIState.Place(
                                        it.name,
                                        LatLng(it.latitude, it.longitude)
                                    )
                                )
                            } else {
                                routeViewModel.setDestPlace(
                                    LocationUIState.Place(
                                        it.name,
                                        LatLng(it.latitude, it.longitude)
                                    )
                                )
                            }
                            onItemClicked()
                        }
                    )
                }

                is SearchBarUIState.RecentAndFavorites -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(12.dp))
                            CurrentLocationItem {
                                if (isStart) {
                                    routeViewModel.setStartPlace(
                                        LocationUIState.CurrentLocation
                                    )
                                } else {
                                    routeViewModel.setDestPlace(
                                        LocationUIState.CurrentLocation
                                    )
                                }
                                onItemClicked()
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        item {
                            Text(
                                "Favorites",
                                style = Style.heading4,
                                color = MetadataGray,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                        items(searchBarValue.favorites.toList()) {
                            MenuItem(
                                type = it.type,
                                label = it.name,
                                sublabel = it.subLabel,
                                onClick = {
                                    if (isStart) {
                                        routeViewModel.setStartPlace(
                                            LocationUIState.Place(
                                                it.name,
                                                LatLng(it.latitude, it.longitude)
                                            )
                                        )
                                    } else {
                                        routeViewModel.setDestPlace(
                                            LocationUIState.Place(
                                                it.name,
                                                LatLng(it.latitude, it.longitude)
                                            )
                                        )
                                    }
                                    onItemClicked()
                                }
                            )
                        }
                        item {
                            Text(
                                "Recents",
                                style = Style.heading4,
                                color = MetadataGray,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                        items(searchBarValue.recents) {
                            MenuItem(
                                type = it.type,
                                label = it.name,
                                sublabel = it.subLabel,
                                onClick = {
                                    if (isStart) {
                                        routeViewModel.setStartPlace(
                                            LocationUIState.Place(
                                                it.name,
                                                LatLng(it.latitude, it.longitude)
                                            )
                                        )
                                    } else {
                                        routeViewModel.setDestPlace(
                                            LocationUIState.Place(
                                                it.name,
                                                LatLng(it.latitude, it.longitude)
                                            )
                                        )
                                    }
                                    onItemClicked()
                                }
                            )
                        }
                    }
                }
            }
        }
    }

}
