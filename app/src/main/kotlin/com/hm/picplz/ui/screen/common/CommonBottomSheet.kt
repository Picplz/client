package com.hm.picplz.ui.screen.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hm.picplz.ui.theme.MainThemeColor


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CommonBottomSheetScaffold(
    modifier: Modifier = Modifier,
    sheetContent: @Composable ColumnScope.() -> Unit,
    sheetPeekHeight: Dp = 30.dp,
    sheetShape: Shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    navigationBarPadding: Boolean = false,
    content: @Composable (PaddingValues) -> Unit,
    ) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded,
            skipHiddenState = true
        )
    )
    val navigationBarHeight = with(LocalDensity.current) {
        WindowInsets.navigationBars.getBottom(this).toDp()
    }
    BottomSheetScaffold(
        modifier = modifier
            .safeDrawingPadding(),
        scaffoldState = scaffoldState,
        sheetContainerColor = MainThemeColor.Gray3,
        sheetContent = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 1.dp)
                    .clip(sheetShape),
                color = MainThemeColor.White
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier
                            .width(40.dp)
                            .height(4.dp),
                        shape = RoundedCornerShape(2.dp),
                        color = MainThemeColor.Gray3
                    ) {}
                }
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    sheetContent()
                }
            }

        },
        sheetPeekHeight = sheetPeekHeight + if(navigationBarPadding) navigationBarHeight else 0.dp,
        sheetShape = sheetShape,
        sheetDragHandle = null,
        content = content,
    )
}
