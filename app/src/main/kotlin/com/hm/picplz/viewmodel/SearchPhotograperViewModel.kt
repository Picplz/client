package com.hm.picplz.viewmodel

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import com.hm.picplz.R
import com.hm.picplz.ui.theme.MainThemeColor
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder
import com.kakao.vectormap.label.LabelTextStyle

class SearchPhotographerViewModel : ViewModel() {

    fun displayLabelsOnMap(kakaoMap: KakaoMap) {
        val labelManager = kakaoMap.labelManager

        val labelStyles = LabelStyles.from(
            "photographerLabel",
            LabelStyle.from(R.drawable.info).setZoomLevel(8),
            LabelStyle.from(R.drawable.info).setZoomLevel(11),
            LabelStyle.from(R.drawable.info)
                .setTextStyles(
                    24,
                    MainThemeColor.Black.toArgb()
                )
                .setZoomLevel(15)
        )

        val styles = labelManager?.addLabelStyles(labelStyles)

        val photographerLocations = listOf(
            LatLng.from(37.406960, 127.115587),
            LatLng.from(37.408960, 127.117587)
        )

        photographerLocations.forEach { location ->
            labelManager?.layer?.addLabel(
                LabelOptions.from(location)
                    .setStyles(styles)
                    .setTexts(
                        LabelTextBuilder()
                            .setTexts("작가")
                    )
            )
        }
    }
}