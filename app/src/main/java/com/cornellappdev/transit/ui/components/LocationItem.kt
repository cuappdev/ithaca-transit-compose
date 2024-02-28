package com.cornellappdev.transit.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LocationItem(icon: ImageVector, label: String, sublabel: String, visible: Boolean){
    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally){
        if(visible && label!=""){
            Box(modifier = Modifier.size(65.dp)){
                Icon(
                    icon,
                    contentDescription = "Place",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(60.dp)
                )
                IconButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(20.dp)

                ) {
                    Icon(
                        Icons.Filled.Add,
                        contentDescription = "Add"
                    )
                }

            }
        }else{
            Box(modifier = Modifier.size(65.dp)) {
                Icon(
                    icon,
                    contentDescription = "Place",
                    modifier = Modifier
                        .size(60.dp).align(Alignment.Center)
                )
            }
        }

        Text(label)
        Text(
            sublabel,
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}