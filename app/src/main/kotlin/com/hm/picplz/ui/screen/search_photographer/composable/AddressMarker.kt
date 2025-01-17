package com.hm.picplz.ui.screen.search_photographer.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.picplz.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.Pretendard

@Composable
fun AddressMarker (
    modifier: Modifier = Modifier,
    address: String?,
) {
    Row(
        modifier = modifier
            .padding(
                horizontal = 15.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.End
    ) {
        Image(
            painter = painterResource(id = R.drawable.marker_map),
            contentDescription = "지도 표시 마커"
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = address?: "마포구 서교동",
            modifier = Modifier,
            color = MainThemeColor.Black,
            style = TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                lineHeight = 18.sp * 1.4,
                letterSpacing = 0.sp
            ),
            maxLines = 1
        )
    }
}