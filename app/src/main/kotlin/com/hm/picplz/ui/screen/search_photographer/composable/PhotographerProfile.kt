package com.hm.picplz.ui.screen.search_photographer.composable

import android.annotation.SuppressLint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.Pretendard

@SuppressLint("DefaultLocale")
@Composable
fun PhotographerProfile(
    name: String,
    profileImageUri: String,
    isActive: Boolean,
    isSelected: Boolean,
    offset: Offset,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    distance: Double?,
){
    val formattedDistance = String.format("%.0f", distance)

    Column(
        modifier = modifier
            .offset(x = offset.x.dp, y = offset.y.dp)
            .wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val imageSize by animateDpAsState(
            targetValue = if (isSelected) 90.dp else 74.dp,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            ),
            label = "imageSize"
        )

        Image(
            painter = rememberAsyncImagePainter(model = profileImageUri),
            contentDescription = "작가 위치",
            colorFilter = if (!isActive) ColorFilter.colorMatrix(
                ColorMatrix().apply {
                    setToSaturation(0f)
                }
            ) else null,
            modifier = Modifier
                .size(imageSize)
                .then(
                    if (isSelected) {
                        Modifier.shadow(
                            elevation = 8.dp,
                            shape = CircleShape,
                            spotColor = MainThemeColor.Black.copy(alpha = 0.25f)
                        )
                    } else {
                        Modifier
                    }
                )
                .clip(CircleShape)
                .then(
                    if (isSelected) {
                        Modifier
                            .border(2.dp, MainThemeColor.Olive, CircleShape)
                            .border(4.dp, MainThemeColor.Black, CircleShape)
                    } else {
                        Modifier.border(2.dp, MainThemeColor.Black, CircleShape)
                    }
                )
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null,
                    onClick = onClick
                ),
        )
        Spacer(
            modifier = Modifier
                .height(6.dp)
        )
        Row (
            modifier = Modifier
                .zIndex(1f),
            verticalAlignment = Alignment.CenterVertically
        ){
            Spacer(
                modifier = Modifier.width(11.dp)
            )
            Text(
                text = name,
                style = TextStyle(
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    lineHeight = 12.sp * 1.4,
                    letterSpacing = 0.sp
                ),
                color = MainThemeColor.Black
            )
            Spacer(
                modifier = Modifier.width(3.dp)
            )
            if (isActive) {
                Image(
                    painter = painterResource(id = R.drawable.active_dot),
                    contentDescription = "활성 상태 표시",
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.inactive_dot),
                    contentDescription = "활성 상태 표시",
                )
            }
        }
        if (isActive) {
            Text(
                modifier = Modifier
                    .zIndex(1f),
                text = "${formattedDistance}m",
                style = TextStyle(
                    fontFamily = Pretendard,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    lineHeight = 124.sp * 1.4,
                    letterSpacing = 0.sp
                ),
                color = MainThemeColor.Gray4
            )
        }
    }
}