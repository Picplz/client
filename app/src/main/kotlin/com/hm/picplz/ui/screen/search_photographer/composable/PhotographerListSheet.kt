package com.hm.picplz.ui.screen.search_photographer.composable

import ChipHeight
import CommonChip
import CommonStatusTag
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hm.picplz.R
import com.hm.picplz.data.model.ChipMode
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.viewmodel.SearchPhotographerViewModel

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
fun PhotographerListSheet(
    viewModel: SearchPhotographerViewModel = hiltViewModel()
) {
    val currentState = viewModel.state.collectAsState().value

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
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "거리순",
                color = MainThemeColor.Gray5
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                painter = painterResource(id = R.drawable.arrow_down),
                contentDescription = "정렬 방식 선택",
                modifier = Modifier.size(12.dp),
                tint = MainThemeColor.Gray5
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            currentState.nearbyPhotographers.let { photographers ->
                items(photographers.active + photographers.inactive) { photographer ->
                    PhotographerCard(
                        photographer = photographer
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhotographerListScreenPreview() {
    PhotographerListSheet()
}

