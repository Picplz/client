package com.hm.picplz.ui.screen.search_photographer.composable

import CommonChip
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.R
import com.hm.picplz.data.model.ChipMode
import com.hm.picplz.ui.model.Photographer
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.Pretendard

@Composable
fun PhotographerCard (
    modifier: Modifier = Modifier,
    photographer: Photographer,
) {
    Row (
        modifier = modifier
            .background(color = MainThemeColor.White)
            .height(140.dp)
            .padding(vertical = 20.dp)
            .width(345.dp)
    ) {
        Image(
            painter = rememberAsyncImagePainter(model = photographer.profileImageUri),
            contentDescription = "작가 카드 프로필",
            modifier = Modifier
                .size(90.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column (
            modifier = Modifier.fillMaxSize()
        ){
            Row (
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .fillMaxSize()
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
            ){
                Column {
                    Text(
                        text = photographer.name,
                        style = TextStyle(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            lineHeight = 16.sp * 1.4,
                            letterSpacing = 0.sp,
                        )
                    )
                    Text(
                        text = "${photographer.distance}m",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MainThemeColor.Gray4
                    )
                }
//                if (photographer.isActive) {
                    ActiveStatusBadge(text = "바로 촬영")
//                }
            }
            LazyRow(
                modifier = Modifier
                    .height(30.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                val vibeTags = listOf(
                    "#을지로 감성",
                    "#키치 감성",
                    "#MZ 감성",
                    "#퇴폐 감성"
                )

                itemsIndexed(vibeTags) { index, vibeTag ->
                    CommonChip(
                        id = index.toString(),
                        label = vibeTag,
                        initialMode = ChipMode.DEFAULT,
                        isEditable = false,
                        height = ChipHeight.MEDIUM,
                        backgroundColor = MainThemeColor.Gray2,
                        unselectedBorderColor = MainThemeColor.Gray2,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PhotographerCardPreview() {
    PhotographerCard(
        photographer = Photographer(
            id = 1,
            name = "작가1",
            location = null,
            profileImageUri = "https://picsum.photos/200",
            isActive = false,
            workingArea = "마포구 서교동",
            distance = 100,
            followers = listOf(1, 2, 3),
            socialAccount = "@account",
        )
    )
}