package com.hm.picplz.ui.screen.search_photographer.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.picplz.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.Pretendard

@Composable
fun ActiveStatusBadge(
    modifier: Modifier = Modifier,
    text: String = "바로 촬영",
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.height(20.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.active_dot),
            contentDescription = "활성 상태 표시",
        )
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = text,
            color = MainThemeColor.Olive,
            style = TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                lineHeight = 12.sp * 1.4,
                letterSpacing = 0.sp
            )
        )
    }
}