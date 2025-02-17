package com.hm.picplz.ui.screen.search_photographer.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun DistanceText(
    distance: String,
    duration: String,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
    ) {
        Text(
            text = "${distance}m / $duration",
            style = MaterialTheme.typography.bodyMedium,
            color = MainThemeColor.Gray4
        )
    }
}