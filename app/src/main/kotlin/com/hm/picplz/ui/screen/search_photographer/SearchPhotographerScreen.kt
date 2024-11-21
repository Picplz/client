package com.hm.picplz.ui.screen.search_photographer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.viewmodel.SearchPhotographerViewModel
import com.kakao.vectormap.LatLng

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SearchPhotographerScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchPhotographerViewModel = viewModel(),
    mainNavController: NavHostController,
) {
    val context = LocalContext.current

    val currentState = viewModel.state.collectAsState().value

    Scaffold (
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MainThemeColor.White
    ){ innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            CommonTopBar(
                text = "내 주변 작가 찾기",
                onClickBack = {}
            )
            Box(
                modifier = Modifier.weight(1f)
            ) {
                KakaoMapView(
                    onMapReady = { kakaoMap ->
                        viewModel.displayLabelsOnMap(kakaoMap)
                        viewModel.handleIntent(SearchPhotographerIntent.GetAddress(LatLng.from(37.406960, 127.115587)))
                    },
                    onCameraMoveEnd = {_, cameraPosition, _ ->
                        viewModel.handleIntent(SearchPhotographerIntent.SetCenterCoords(cameraPosition.position))
                    }
                )
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = MainThemeColor.White,
                ) {
                    Text(
                        text = currentState.address ?: "",
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        ),
                        color = MainThemeColor.Black,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Preview(showBackground = true)
@Composable
fun SearchPhotographerPreview() {
    PicplzTheme {
        val mainNavController = rememberNavController()
        SearchPhotographerScreen(mainNavController = mainNavController)
    }
}