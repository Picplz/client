package com.hm.picplz.viewmodel

import android.util.Log
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hm.picplz.R
import com.hm.picplz.data.model.KaKaoAddressRequest
import com.hm.picplz.data.source.KakaoMapSource
import com.hm.picplz.ui.screen.search_photographer.SearchPhotographerIntent
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchPhotographerViewModel: ViewModel() {
    private val _state = MutableStateFlow(SearchPhotographerState.idle())
    val state : StateFlow<SearchPhotographerState> get() = _state

    private val kakaoSource = KakaoMapSource()

    fun handleIntent(intent: SearchPhotographerIntent) {
        when (intent) {
            is SearchPhotographerIntent.SetAddress -> {
                _state.update { it.copy(address = intent.address) }
            }
            is SearchPhotographerIntent.GetAddress -> {
                viewModelScope.launch {
                    kakaoSource.getAddressFromCoords(KaKaoAddressRequest(intent.longitude.toString(), intent.latitude.toString()))
                        .onSuccess { response ->
                            val twoDepthRegion = response.documents.firstOrNull()?.address?.region_2depth_name ?: ""
                            val threeDepthRegion = response.documents.firstOrNull()?.address?.region_3depth_name ?: ""
                            val address = "$twoDepthRegion $threeDepthRegion"
                            handleIntent(SearchPhotographerIntent.SetAddress(address))
                        }
                        .onFailure { error ->
                            Log.e("kakaoMapAddressSearch", "좌표 검색 실패 : ", error)
                        }
                }
            }
        }
    }

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
}