import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hm.picplz.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.Pretendard

@Composable
fun CommonStatusTag(
    modifier: Modifier = Modifier,
    label: String,
    icon: Painter? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .height(25.dp)
            .border(
                width = 1.dp,
                color = MainThemeColor.Gray2,
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                color = MainThemeColor.Gray1,
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 8.dp)
            .then(
                if (onClick != null) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = true),
                        onClick = onClick
                    )
                } else Modifier
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(
                painter = icon,
                contentDescription = "tag-icon",
                tint = MainThemeColor.Olive,
                modifier = Modifier.height(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
        }
        Text(
            text = label,
            style = TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp,
                lineHeight = 12.sp * 1.4,
                letterSpacing = 0.sp
            ),
            color = MainThemeColor.Gray4
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommonFilterChipPreview() {
    PicplzTheme {
        CommonStatusTag(
            label = "바로 촬영",
            icon = painterResource(id = R.drawable.tag_circle)
        )
    }
}