package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hm.picplz.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.buttonText
import com.hm.picplz.ui.theme.pretendardTypography

data class ReviewItem(
    val imageUri: Int, // 이미지 URI (리소스 ID)
    val label: String, // 텍스트 레이블
    val value: Int // 값 (예: "12"과 같은 숫자)
)

@Composable
fun ReviewSection(modifier: Modifier) {
    // 아이템 리스트 정의
    val items = listOf(
        ReviewItem(
            imageUri = R.drawable.default_profile,
            label = "사진을 예쁘게 찍어줘요",
            value = 14
        ),
        ReviewItem(
            imageUri = R.drawable.user_selected,
            label = "포즈 추천을 잘 해줘요",
            value = 10
        ),
        ReviewItem(
            imageUri = R.drawable.center_char,
            label = "친절해요",
            value = 9
        ),
        ReviewItem(
            imageUri = R.drawable.user_deselected,
            label = "보정을 잘 해요",
            value = 5
        ),
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "촬영 만족도", style = buttonText)
        Spacer(modifier = Modifier.height(10.dp))

        // 별점 부분
        Row(verticalAlignment = Alignment.CenterVertically) {
            repeat(5) {
                Image(
                    painter = painterResource(id = R.drawable.spicky3),
                    contentDescription = "별점",
                    modifier = Modifier.size(23.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
            }
            Text(
                text = "4.0",
                style = pretendardTypography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                color = MainThemeColor.Gray4
            )
        }

        Spacer(modifier = Modifier.height(17.dp))

        // 아이템 리스트를 돌면서 표시
        items.forEach { item ->
            Box(
                modifier = modifier
                    .fillMaxWidth()
                    .height(39.dp)
                    .background(MainThemeColor.Gray1, RoundedCornerShape(5.dp))
                    .border(1.dp, MainThemeColor.Gray2, RoundedCornerShape(5.dp))
            ) {
                // FIXME: 자식 박스의 너비는 value에 따라 다르게 설정
                Box(
                    modifier = Modifier
                        .width((item.value * 10).dp) // value에 따라 width 계산 (예: 14 * 10dp)
                        .height(39.dp)
                        .background(MainThemeColor.Black, RoundedCornerShape(5.dp))
                )

                Image(
                    painterResource(id = item.imageUri),
                    contentDescription = "리뷰 아이콘",
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(24.dp)
                        .padding(start = 9.dp)
                )

                Text(
                    text = item.label,
                    color = MainThemeColor.White,
                    style = pretendardTypography.bodyMedium,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 30.dp)
                )

                Text(
                    text = item.value.toString(),
                    color = MainThemeColor.Gray4,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
        }

        Text(
            "전체 리뷰 보러가기 () >",
            modifier = modifier
                .align(Alignment.End)
                .clickable { },
        )
    }
}