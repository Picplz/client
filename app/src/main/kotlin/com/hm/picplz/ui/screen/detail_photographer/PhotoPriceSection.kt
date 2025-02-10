package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Tab
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.buttonText
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun PhotoPriceSection(modifier: Modifier) {
    // 촬영 가격
    Text(
        text = "촬영 가격",
        style = buttonText,
        modifier = modifier.fillMaxWidth()
    )
    var state by remember { mutableStateOf(0) }
    val tabTitles = listOf("프로필 Only", "카카오톡 패키지", "인스타그램 패키지")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        TabRow(
            selectedTabIndex = state,
            containerColor = MainThemeColor.White,
            contentColor = MainThemeColor.Black,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[state]),
                    color = MainThemeColor.Black,
                )
            }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = state == index,
                    onClick = { state = index },
                    modifier = Modifier
                        .height(23.dp),
                    text = {
                        Text(
                            text = title,
                            style = pretendardTypography.bodySmall,
                            fontSize = 11.5.sp
                        )
                    },
                )
            }
        }
        Text(
            text = "Secondary tab ${state + 1} selected",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}