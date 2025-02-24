package com.hm.picplz.ui.screen.search_photographer.composable

import CommonChip
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hm.picplz.data.model.ChipMode
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun VibeTags (
    modifier: Modifier = Modifier,
    tags: List<String> = emptyList(),
) {
    LazyRow(
        modifier = modifier
            .height(30.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        itemsIndexed(tags) { index, vibeTag ->
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