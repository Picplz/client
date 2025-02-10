package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hm.picplz.R
import com.hm.picplz.ui.theme.buttonText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortfolioSection(modifier: Modifier) {
    // 포트폴리오
    Text(
        modifier = modifier.fillMaxWidth(),
        text = "포트폴리오",
        style = buttonText
    )

    val images = listOf(
        R.drawable.camera_circle,
        R.drawable.edit_grey4,
        R.drawable.edit_grey4,
        R.drawable.edit_grey4,
        R.drawable.edit_grey4,
        R.drawable.edit_grey4,
        R.drawable.edit_grey4,
        R.drawable.edit_grey4,
        R.drawable.edit_grey4,
    )

    // 3열의 고정 그리드를 직접 구성
    val chunkedImages = images.chunked(3) // 3개씩 나눔

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 7.dp)
    ) {
        chunkedImages.forEach { rowImages ->
            Row(modifier = Modifier.fillMaxWidth()) {
                rowImages.forEach { imageRes ->
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = "포트폴리오 이미지",
                        modifier = Modifier
                            .weight(1f) // 각 이미지가 동일한 크기를 가짐
                            .aspectRatio(1f) // 1:1 비율
                            .padding(2.dp)
                    )
                }
                // 남은 빈 공간 채우기
                repeat(3 - rowImages.size) {
                    Spacer(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .padding(2.dp)
                    )
                }
            }
        }
    }
}