package com.cornellappdev.transit.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
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
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.components.RouteOptionsSearchSheet
import com.cornellappdev.transit.ui.theme.DividerGrey
import com.cornellappdev.transit.ui.theme.IconGrey
import com.cornellappdev.transit.ui.theme.MetadataGrey
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.sfProDisplayFamily
import com.cornellappdev.transit.ui.viewmodels.RouteViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

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

    val startLocation = routeViewModel.startPl.collectAsState().value
    val endLocation = routeViewModel.destPl.collectAsState().value

    val keyboardController = LocalSoftwareKeyboardController.current

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

            Divider(thickness = 1.dp, color = DividerGrey)
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
                        tint = IconGrey,
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
                        tint = IconGrey,
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
                            .background(color = DividerGrey, shape = RoundedCornerShape(8.dp))
                            .fillMaxWidth(0.9f)
                            .clickable {
                                coroutineScope.launch {
                                    if(startLocation == "Current Location") {
                                        routeViewModel.onQueryChange("")
                                    } else {
                                        routeViewModel.onQueryChange(startLocation)
                                    }
                                    sheetState.show()
                                }
                            }
                    ) {
                        Text(
                            text = startLocation,
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
                            .background(color = DividerGrey, shape = RoundedCornerShape(8.dp))
                            .fillMaxWidth(0.9f)
                            .clickable {
                                coroutineScope.launch {
                                    if(endLocation == "Current Location") {
                                        routeViewModel.onQueryChange("")
                                    } else {
                                        routeViewModel.onQueryChange(endLocation)
                                    }
                                    sheetState.show()
                                }
                            }
                    ) {
                        Text(
                            text = endLocation,
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

            Divider(thickness = 1.dp, color = DividerGrey)

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
                    color = MetadataGrey, fontSize = 14.sp
                )
            }


            LazyColumn(modifier = Modifier
                .background(color = DividerGrey)
                .fillMaxSize(), content = {})
        }

    }

}
