package com.hm.picplz.viewmodel

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import com.hm.picplz.R
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerState
import com.hm.picplz.ui.theme.MainThemeColor
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LabelTextBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

class SearchPhotographerViewModel: ViewModel() {
    private val _state = MutableStateFlow(SearchPhotographerState.idle())
    val state : StateFlow<SearchPhotographerState> get() = _state

    fun displayLabelsOnMap(kakaoMap: KakaoMap) {
        val labelManager = kakaoMap.labelManager

        val labelStyles = LabelStyles.from(
            "photographerLabel",
            LabelStyle.from(R.drawable.marker).setZoomLevel(8),
            LabelStyle.from(R.drawable.marker).setZoomLevel(11),
            LabelStyle.from(R.drawable.marker)
                .setTextStyles(
                    24,
                    MainThemeColor.Black.toArgb()
                )
                .setZoomLevel(15)
        )

        val photographerLocations = listOf(
            LatLng.from(37.406960, 127.115587),
            LatLng.from(37.408960, 127.117587)
        )

        val styles = labelManager?.addLabelStyles(labelStyles)
        val texts = LabelTextBuilder().setTexts("작가")

        photographerLocations.forEach { location ->
            val options = LabelOptions.from(location)
                .setStyles(styles)
                .setTexts(texts)
            val layer = labelManager?.layer
            layer?.addLabel(options)
        }
    }

    fun displayAddressOnMap(kakaoMap: KakaoMap, context: Context) {
        val geocoder = Geocoder(context, Locale.KOREA)
        try {
            val addresses = geocoder.getFromLocation(37.406960, 127.115587, 1)
            Log.d("Geocoder", "addresses: $addresses")
        } catch (e: Exception) {
            Log.e("Geocoder", "에러: ${e.message}")
        }
    }
}