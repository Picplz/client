package com.hm.picplz.ui.screen.search_photographer.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
        Row() {
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
                )
            )
        }
    }
}