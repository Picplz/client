package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CommonBottomSheetScaffold(
    modifier: Modifier = Modifier,
    sheetContent: @Composable ColumnScope.() -> Unit,
    sheetPeekHeight: Float = 64f,
    sheetShape: Shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    content: @Composable (PaddingValues) -> Unit,
) {
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        modifier = modifier,
        scaffoldState = scaffoldState,
        sheetContent = {
            sheetContent()
        },
        sheetPeekHeight = sheetPeekHeight.dp,
        sheetShape = sheetShape,
        content =  content,
    )
}