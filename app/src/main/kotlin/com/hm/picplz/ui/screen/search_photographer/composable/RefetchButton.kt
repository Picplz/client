package com.hm.picplz.ui.screen.search_photographer.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
fun RefetchButton (
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
){
    Box(
        modifier = modifier
            .size(width = 166.dp, height = 48.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick= onClick
            ),
        contentAlignment = Alignment.Center
    ){
        Box(
            modifier = Modifier
                .background(
                    color = MainThemeColor.White,
                    shape = RoundedCornerShape(50.dp)
                )
                .border(
                    width = 1.dp,
                    color = MainThemeColor.Gray2,
                    shape = RoundedCornerShape(50.dp)
                ),
            contentAlignment = Alignment.Center,

            ) {
            Row (
                modifier = Modifier
                    .padding(
                        horizontal = 15.dp,
                        vertical = 6.dp
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ){
                Text(
                    text = "내 위치 새로고침",
                    style = TextStyle(
                        fontFamily = Pretendard,
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp,
                        lineHeight = 14.sp * 1.4,
                        letterSpacing = 0.sp
                    ),
                    color = MainThemeColor.Gray4
                )
                Spacer(modifier = Modifier.width(3.dp))
                Image(
                    painter = painterResource(id = R.drawable.arrow_rotate_left),
                    contentDescription = "circles"
                )
            }
        }
    }
}