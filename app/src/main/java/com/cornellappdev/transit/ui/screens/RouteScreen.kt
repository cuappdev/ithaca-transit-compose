package com.cornellappdev.transit.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.theme.DividerGrey
import com.cornellappdev.transit.ui.theme.IconGrey
import com.cornellappdev.transit.ui.theme.MetadataGrey
import com.cornellappdev.transit.ui.theme.PrimaryText
import com.cornellappdev.transit.ui.theme.sfProDisplayFamily
import com.google.accompanist.permissions.ExperimentalPermissionsApi

/**
 * Composable for the route screen, which specifies a location, destination, and routes between them
 */
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RouteScreen(
    //homeViewModel: HomeViewModel = hiltViewModel()
    navController: NavController
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
                IconButton(onClick = { /*TODO make this button nav back to whatever screen*/ }) {
                    Icon(
                        imageVector = Icons.Outlined.KeyboardArrowLeft,
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
                val (fromtext, fromstop, totext, tostop, line) = createRefs()

                Text(text = "From", color = PrimaryText,
                    fontFamily = sfProDisplayFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp, modifier = Modifier.constrainAs(fromtext) {
                        top.linkTo(parent.top, margin = 3.dp)
                    })

                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.boarding_stop),
                    contentDescription = "",
                    tint = IconGrey,
                    modifier = Modifier
                        .size(12.dp)
                        .constrainAs(fromstop) {
                            top.linkTo(fromtext.top, margin = 3.dp)
                            bottom.linkTo(fromtext.bottom)
                            start.linkTo(fromtext.end, margin = 12.dp)
                            end.linkTo(parent.end)
                        })
                Text(
                    text = "To",
                    color = PrimaryText,
                    fontFamily = sfProDisplayFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                    modifier = Modifier.constrainAs(totext) {
                        top.linkTo(fromtext.bottom, margin = 24.dp)
                        bottom.linkTo(parent.bottom, margin = 2.dp)
                        start.linkTo(fromtext.start)
                    }
                )

                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.bus_route_line),
                    contentDescription = "",
                    tint = IconGrey,
                    modifier = Modifier.constrainAs(line) {
                        top.linkTo(fromstop.bottom)
                        bottom.linkTo(tostop.top)
                        start.linkTo(fromstop.start)
                        end.linkTo(fromstop.end)
                    })

                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.destination_stop),
                    contentDescription = "",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(16.dp)
                        .constrainAs(tostop) {
                            top.linkTo(totext.top, margin = 3.dp)
                            bottom.linkTo(totext.bottom)
                            start.linkTo(fromstop.start)
                            end.linkTo(fromstop.end)
                        }
                )
            }

            //TODO user should be able to enter location/destination
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .background(color = DividerGrey, shape = RoundedCornerShape(8.dp))
                        .fillMaxWidth(0.9f)
                ) {
                    Text(
                        text = "Current Location",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        color = PrimaryText,
                        fontFamily = sfProDisplayFamily,
                        fontWeight = FontWeight.Normal, fontSize = 14.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .background(color = DividerGrey, shape = RoundedCornerShape(8.dp))
                        .fillMaxWidth(0.9f)
                ) {
                    Text(
                        text = "Destination",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        fontFamily = sfProDisplayFamily,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
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
                text = "Leave Now (12:15AM)", //PLACEHOLDER!!!
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

@Preview
@Composable
fun PreviewRouteScreen() {
    val previewNav = rememberNavController()
}
