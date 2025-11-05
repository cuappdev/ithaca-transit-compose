package com.cornellappdev.transit.ui.components.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cornellappdev.transit.R
import com.cornellappdev.transit.ui.theme.FavoritesYellow

@Composable
fun BoxScope.FavoritesStar(onFavoriteClick: () -> Unit, isFavorite: Boolean) {
    Box(
        modifier = Modifier
            .align(Alignment.TopEnd)
            .size(20.dp)
            .clickable { onFavoriteClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Default.Star else ImageVector.vectorResource(
                R.drawable.baseline_star_outline_20
            ),
            contentDescription = null,
            tint = if (isFavorite) FavoritesYellow else Color.Gray,
            modifier = Modifier.size(20.dp)
        )
    }
}