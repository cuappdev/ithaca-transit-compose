package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.theme.TransitBlue

@Composable
fun LocationItem(image: Painter, editImage: Painter, label: String, sublabel: String, editing: Boolean){
    Column(modifier =  Modifier.width(88.dp).height(86.dp).padding(horizontal = 8.dp)){
        if(editing && label!="Add"){
            Box(modifier = Modifier.size(48.dp).align(Alignment.CenterHorizontally)){
                Image(
                    editImage,
                    contentDescription = "Place",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp)
                )
                Image(
                    painter = painterResource(id = R.drawable.remove_icon),
                    contentDescription = "Add",
                    modifier = Modifier.align(Alignment.TopEnd).size(20.dp)
                )

            }
        }else{
            Box(modifier = Modifier.height(48.dp).width(48.dp).align(Alignment.CenterHorizontally)) {
                Image(
                    image,
                    contentDescription = "Place",
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                )
            }
        }
        if (label=="Add"){
            Text(
                label,
                fontSize = 12.sp,
                color = TransitBlue,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top=8.dp, bottom=4.dp).height(14.dp).width(72.dp)
            )
        }else{
            Text(
                label,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top=8.dp, bottom=4.dp).height(14.dp).width(72.dp)
            )
        }

        Text(
            sublabel,
            color = Color.Gray,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally).height(12.dp).width(72.dp)
        )
    }
}

@Preview
@Composable
fun previewLocationItem(){
    LocationItem(image = painterResource(id = R.drawable.location_icon), editImage = painterResource(id = R.drawable.location_icon_edit), label = "gates hall", sublabel = "hello", editing = true)

}