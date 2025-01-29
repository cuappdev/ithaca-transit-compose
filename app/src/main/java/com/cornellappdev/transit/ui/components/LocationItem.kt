package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.theme.TransitBlue
import com.cornellappdev.transit.ui.theme.robotoFamily

/**
 * Card for each entry in favourite locations list
 * @param image The icon for the item
 * @param editImage The icon for the item when the list if being edited
 * @param label The label for the item
 * @param sublabel The sublabel for each item
 * @param editing Whether or nto the list if currently being edited
 * @param addOnClick The Function to run when the add button is clicked
 * @param removeOnClick The Function to run when the remove button is clicked
 */
@Composable
fun LocationItem(
    image: Painter,
    editImage: Painter,
    label: String,
    sublabel: String,
    editing: Boolean,
    itemOnClick: () -> Unit,
    addOnClick: () -> Unit,
    removeOnClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .width(88.dp)
            .padding(horizontal = 8.dp)
    ) {
        if (editing && label != "Add") {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Image(
                    editImage,
                    contentDescription = "Place",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)

                )
                Image(
                    painter = painterResource(id = R.drawable.remove_icon),
                    contentDescription = "AdRemoved",
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(20.dp)
                        .clickable(onClick = removeOnClick)
                )

            }
        } else {
            Box(
                modifier = Modifier
                    .height(48.dp)
                    .width(48.dp)
                    .align(Alignment.CenterHorizontally)
            ) {

                if (label == "Add") {
                    Image(
                        image,
                        contentDescription = "Place",
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                            .clickable(onClick = addOnClick)
                    )

                    Image(
                        editImage,
                        contentDescription = "Place",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(30.dp)
                    )
                } else {
                    Image(
                        image,
                        contentDescription = "Place",
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.Center)
                            .clickable(onClick = itemOnClick)
                    )

                }

            }
        }
        Text(
            label,
            fontSize = 12.sp,
            color = if (label == "Add") TransitBlue else Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 8.dp, bottom = 4.dp),
            fontWeight = FontWeight(400),
            fontFamily = robotoFamily,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            sublabel,
            color = Color.Gray,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            fontWeight = FontWeight(400),
            fontFamily = robotoFamily,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun PreviewLocationItem() {
    LocationItem(
        image = painterResource(id = R.drawable.location_icon),
        editImage = painterResource(id = R.drawable.location_icon_edit),
        label = "gates hall",
        sublabel = "hello",
        editing = true,
        {},
        {},
        {}
    )

}
