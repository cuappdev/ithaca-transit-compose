package com.cornellappdev.transit.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.cornellappdev.transit.R
import com.cornellappdev.transit.models.Route
import com.cornellappdev.transit.models.RouteOptions
import com.cornellappdev.transit.models.Transport
import com.cornellappdev.transit.models.createTransport
import com.cornellappdev.transit.networking.ApiResponse
import com.cornellappdev.transit.ui.components.RouteCell
import com.cornellappdev.transit.ui.components.RouteOptionsSearchSheet
import com.cornellappdev.transit.ui.theme.DividerGray
import com.cornellappdev.transit.ui.theme.IconGray
import com.cornellappdev.transit.ui.theme.MetadataGray
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.sfProDisplayFamily
import com.cornellappdev.transit.ui.viewmodels.RouteViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.launch

/**
 * Composable for the route screen, which specifies a location, destination, and routes between them
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(
    ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)
@Composable
fun RouteScreen(
    navController: NavController,
    routeViewModel: RouteViewModel
) {

    val startLocation = routeViewModel.startPl.collectAsState().value
    val endLocation = routeViewModel.destPl.collectAsState().value

    val keyboardController = LocalSoftwareKeyboardController.current

    val lastRoute = routeViewModel.lastRouteFlow.collectAsState().value

    val sheetState =
        androidx.compose.material.rememberModalBottomSheetState(
            ModalBottomSheetValue.Hidden,
            skipHalfExpanded = true,
            confirmValueChange = {
                keyboardController?.hide()
                true
            }
        )
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetContent = {
            RouteOptionsSearchSheet(routeViewModel, onCancelClicked = {
                coroutineScope.launch {
                    sheetState.hide()
                }
            }, onItemClicked = {
                coroutineScope.launch {
                    sheetState.hide()
                }
            })
        },
        sheetShape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            //TODO make an AppBarColors class w/ the right colors and correct icon
            TopAppBar(
                title = {
                    Text(
                        text = "Route Options",
                        fontFamily = sfProDisplayFamily,
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

                    Text(text = "From", color = PrimaryText,
                        fontFamily = sfProDisplayFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp, modifier = Modifier.constrainAs(fromText) {
                            top.linkTo(parent.top, margin = 3.dp)
                        })

                    Icon(imageVector = ImageVector.vectorResource(id = R.drawable.boarding_stop),
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
                        fontFamily = sfProDisplayFamily,
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
                                    if (startLocation.first == "Current Location") {
                                        routeViewModel.onQueryChange("")
                                    } else {
                                        routeViewModel.onQueryChange(startLocation.first)
                                    }
                                    sheetState.show()
                                }
                            }
                    ) {
                        Text(
                            text = startLocation.first,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            color = PrimaryText,
                            fontFamily = sfProDisplayFamily,
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
                                    if (endLocation.first == "Current Location") {
                                        routeViewModel.onQueryChange("")
                                    } else {
                                        routeViewModel.onQueryChange(endLocation.first)
                                    }
                                    sheetState.show()
                                }
                            }
                    ) {
                        Text(
                            text = endLocation.first,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            fontFamily = sfProDisplayFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                //TODO this should probably be an IconButton that swaps current/dest
                Icon(
                    imageVector = ImageVector.vectorResource(
                        id = R.drawable.swap
                    ),
                    contentDescription = "",
                    modifier = Modifier.size(width = 20.dp, height = 20.dp),
                    tint = Color.Unspecified
                )

            }

            Divider(thickness = 1.dp, color = DividerGray)

            //TODO the text should be set to what the time is (passed in by screen call?)
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
                Text(
                    text = "Leave Now (" + routeViewModel.time + ")",
                    fontFamily = sfProDisplayFamily,
                    fontWeight = FontWeight.Normal,
                    color = MetadataGray, fontSize = 14.sp
                )
            }

            RouteList(lastRoute)
        }

    }

}

@Composable
private fun PaddedRouteCell(transport: Transport) {
    Row(modifier = Modifier.padding(12.dp)) {
        RouteCell(transport)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
private fun RouteList(lastRouteResponse: ApiResponse<RouteOptions>) {
    when (lastRouteResponse) {
        is ApiResponse.Error -> {
            Text("Error")
        }

        is ApiResponse.Pending -> {
            Text("Pending")
        }

        is ApiResponse.Success -> {

            LazyColumn(modifier = Modifier
                .background(color = DividerGray)
                .fillMaxSize(), content = {
                items(lastRouteResponse.data.fromStop, itemContent = { item ->
                    PaddedRouteCell(createTransport(item))
                })
                if (lastRouteResponse.data.boardingSoon.isNotEmpty()) {
                    item {
                        Text("Boarding Soon From Nearby Stops")
                    }
                }
                items(lastRouteResponse.data.boardingSoon, itemContent = { item ->
                    PaddedRouteCell(createTransport(item))
                })
                if (lastRouteResponse.data.walking.isNotEmpty()) {
                    item {
                        Text("By Walking")
                    }
                }
                items(lastRouteResponse.data.walking, itemContent = { item ->
                    PaddedRouteCell(createTransport(item))
                })
            })
        }
    }
}
