package com.hm.picplz.ui.screen.search_photographer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.viewmodel.SearchPhotographerViewModel
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapOverlay
import kotlinx.coroutines.flow.collectLatest

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SearchPhotographerScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchPhotographerViewModel = viewModel(),
    mainNavController: NavHostController,
) {
    val context = LocalContext.current
    val currentState = viewModel.state.collectAsState().value

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) ||
                    permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                viewModel.handleIntent(SearchPhotographerIntent.GetCurrentLocation(context))
            }
            else -> {
                Toast.makeText(
                    context,
                    "위치 권한이 필요합니다",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED -> {
                viewModel.handleIntent(SearchPhotographerIntent.GetCurrentLocation(context))
            }
            else -> {
                launcher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

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
                onClickBack = {
                    viewModel.handleIntent(SearchPhotographerIntent.NavigateToPrev)
                }
            )
            Box(
                modifier = Modifier.weight(1f)
            ) {
                if (currentState.isFetchingGPS && currentState.userLocation == null) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator(
                                color = MainThemeColor.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "위치 정보 로딩",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MainThemeColor.Black
                            )
                        }
                    }
                } else {
                    KakaoMapView(
                        onMapReady = { kakaoMap ->
                            viewModel.displayLabelsOnMap(kakaoMap)
                            viewModel.handleIntent(SearchPhotographerIntent.GetAddress(LatLng.from(37.406960, 127.115587)))

                            kakaoMap.hideOverlay(MapOverlay.ROADVIEW_LINE)
                            kakaoMap.hideOverlay(MapOverlay.HILLSHADING)
                            kakaoMap.hideOverlay(MapOverlay.BICYCLE_ROAD)
                            kakaoMap.hideOverlay(MapOverlay.SKYVIEW_HYBRID)
                            kakaoMap.isPoiVisible = false

                        },
                        onCameraMoveEnd = {kakaoMap, cameraPosition, _ ->
                            viewModel.handleIntent(SearchPhotographerIntent.SetCenterCoords(cameraPosition.position))
                            viewModel.displayLabelsOnMap(kakaoMap)
                        },
                        initialPosition = currentState.userLocation ?: LatLng.from(37.406960, 127.115587),
                        isGestureEnabled = false,
                        initialZoomLevel = 14,
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
    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SearchPhotographerSideEffect.NavigateToPrev -> {
                    mainNavController.popBackStack()
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