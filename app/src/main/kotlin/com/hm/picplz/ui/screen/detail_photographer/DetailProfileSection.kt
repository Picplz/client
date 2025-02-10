package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.picplz.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.pretendardTypography

@Composable
fun DetailProfileSection(modifier: Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "128")
        Spacer(modifier = Modifier.width(6.dp))
        Button(
            onClick = { /*TODO*/ }, modifier = Modifier
                .width(56.dp)
                .height(21.dp)
        ) {
            Text(text = "팔로우 +")
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        Image(
            painterResource(id = R.drawable.default_profile),
            contentDescription = "작가 프로필",
            modifier = Modifier
                .size(74.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(text = "유가영 작가", style = pretendardTypography.titleSmall)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.spicky3),
                    contentDescription = "별점",
                    modifier = Modifier.size(10.dp)
                )
                Spacer(modifier = Modifier.width(3.dp))
                ClickableText(
                    text = AnnotatedString("Gayoung"),
                    style = pretendardTypography.bodySmall.copy(
                        color = MainThemeColor.Gray3,
                        textDecoration = TextDecoration.Underline
                    ),
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "10/31 이후 예약 가능합니다. 어쩌고저쩌고어마ㅣ;ㅓㄹ어ㅓ어ㅓ어어",
                style = pretendardTypography.labelSmall
            )
        }
    }

    Divider(
        color = MainThemeColor.Gray2,
        thickness = 1.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    )
    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = "촬영지",
            style = pretendardTypography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "촬영지",
            style = pretendardTypography.labelMedium,
            color = MainThemeColor.Gray4
        )
    }

    Spacer(modifier = Modifier.height(10.dp))

    Row(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = "키워드",
            style = pretendardTypography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "촬영지",
            style = pretendardTypography.labelMedium,
            color = MainThemeColor.Gray4
        )
    }
}