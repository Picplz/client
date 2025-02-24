package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonNumberSelector(
    currentValue: Int? = null,
    onValueSelected: (Int) -> Unit,
    maxValue: Int,
    minValue: Int = 0,
    onDismiss: () -> Unit,
    visible: Boolean
) {
    if (visible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            containerColor = Color.White,
            modifier = Modifier.height(400.dp),
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    items(maxValue - minValue + 1) { index ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .clickable {
                                    onValueSelected(index + minValue)
                                    onDismiss()
                                },
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = (index + minValue).toString(),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = if (index + minValue == currentValue)
                                        MainThemeColor.Black
                                    else
                                        MainThemeColor.Gray3
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}