package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cornellappdev.transit.ui.theme.MetadataGray

@Composable
fun CenteredSpinningIndicator() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(138.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircularProgressIndicator(color = MetadataGray, modifier = Modifier.size(40.dp))
    }
}