package com.hm.picplz.ui.screen.search_photographer.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.Pretendard
import com.hm.picplz.viewmodel.SearchPhotographerViewModel

@Composable
fun PhotographerSheet (
    viewModel: SearchPhotographerViewModel = hiltViewModel()
) {
    val currentState = viewModel.state.collectAsState().value
    val selectedPhotographer = currentState.selectedPhotographerId?.let { selectedId ->
        currentState.nearbyPhotographers.let { photographers ->
            (photographers.active + photographers.inactive).find { it.id == selectedId }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = selectedPhotographer?.profileImageUri),
                contentDescription = "작가 프로필 이미지",
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .border(1.dp, MainThemeColor.Gray2, CircleShape)
            )
            Text(
                text = selectedPhotographer?.name ?: "",
                style = TextStyle(
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    letterSpacing = 0.sp
                ),
                modifier = Modifier
                    .padding(start = 4.dp)
            )
            Text(
                text = selectedPhotographer?.socialAccount ?: "",
                style = TextStyle(
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    letterSpacing = 0.sp
                ),
                modifier = Modifier
                    .padding(start = 4.dp)
            )
        }
        Row(
            modifier = Modifier
                .padding(start = 4.dp, top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            ActiveStatusBadge(text = "바로 촬영")
            DistanceText(
                distance = selectedPhotographer?.distance.toString(),
                duration = "도보 10분 거리",
                modifier = Modifier
                    .padding(start = 4.dp)
            )
        }
        val vibeTags = listOf(
            "#을지로 감성",
            "#키치 감성",
            "#MZ 감성",
            "#퇴폐 감성"
        )
        VibeTags(
            modifier = Modifier.padding(top = 20.dp),
            tags = vibeTags
        )
        LazyRow(
            modifier = Modifier
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            selectedPhotographer?.portfolioPhotos?.let { photos ->
                itemsIndexed(photos) { _, photoUrl ->
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = photoUrl,
                            contentScale = ContentScale.Crop
                        ),
                        contentDescription = "포트폴리오 사진",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(5.dp))
                    )
                }
            }
        }
    }
}