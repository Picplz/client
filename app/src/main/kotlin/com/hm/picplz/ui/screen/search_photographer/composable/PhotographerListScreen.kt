import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hm.picplz.R
import com.hm.picplz.data.model.ChipMode
import com.hm.picplz.ui.theme.MainThemeColor

data class StatusTagData(
    val label: String,
    val iconResId: Int
)

private val statusTags = listOf(
    StatusTagData("바로 촬영", R.drawable.tag_circle),
    StatusTagData("팔로우", R.drawable.tag_check),
    StatusTagData("바로 촬영 가능", R.drawable.tag_camera),
)

private val vibeTags = listOf(
    "#을지로 감성",
    "#키치 감성",
    "#MZ 감성",
    "#퇴폐 감성"
)

@Composable
fun PhotographerListScreen() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row {
            statusTags.forEach { statusTag ->
                CommonStatusTag(
                    label = statusTag.label,
                    icon = painterResource(id = statusTag.iconResId)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            itemsIndexed(vibeTags) { index, vibeTag ->
                CommonChip(
                    id = index.toString(),
                    label = vibeTag,
                    initialMode = ChipMode.DEFAULT,
                    isEditable = false,
                    height = ChipHeight.MEDIUM,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotographerListScreenPreview() {
    PhotographerListScreen()
}

