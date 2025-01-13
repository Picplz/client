package com.hm.picplz.ui.screen.search_photographer

import PhotographerListScreen
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.hm.picplz.ui.screen.common.CommonBottomSheetScaffold
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.Pretendard
import com.hm.picplz.viewmodel.SearchPhotographerViewModel
import kotlinx.coroutines.flow.collectLatest
import com.hm.picplz.R
import com.hm.picplz.data.model.Photographer
import com.hm.picplz.data.model.dummyPhotographers
import com.hm.picplz.data.repository.PhotographerRepository
import com.hm.picplz.utils.LocationUtil
import com.hm.picplz.utils.LocationUtil.getDistance
import com.kakao.vectormap.LatLng
import kotlin.math.abs

@SuppressLint("DefaultLocale")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SearchPhotographerScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchPhotographerViewModel = hiltViewModel(),
    mainNavController: NavHostController,
    tempView: Boolean = false  // 개발용 임시 파라미터
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

    CommonBottomSheetScaffold (
        modifier = Modifier
            .fillMaxSize(),
        sheetContent = {
            PhotographerListScreen()
        },
    ){
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MainThemeColor.White),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MainThemeColor.Gray1)
            ) {
                if (!tempView && (currentState.isFetchingGPS && currentState.userLocation == null)) {
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
                    Row (
                        modifier = modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, start = 15.dp, end = 15.dp)
                        ,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(
                                    horizontal = 15.dp,
                                ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.marker_map),
                                contentDescription = "지도 표시 마커"
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = currentState.address ?: "마포구 서교동",
                                modifier = Modifier,
                                color = MainThemeColor.Black,
                                style = TextStyle(
                                    fontFamily = Pretendard,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 18.sp,
                                    lineHeight = 18.sp * 1.4,
                                    letterSpacing = 0.sp
                                ),
                                maxLines = 1
                            )
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MainThemeColor.White,
                                    shape = RoundedCornerShape(50.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = MainThemeColor.Gray2,
                                    shape = RoundedCornerShape(50.dp)
                                ),
                            contentAlignment = Alignment.Center,

                        ) {
                            Row (
                                modifier = Modifier
                                    .padding(
                                        horizontal = 15.dp,
                                        vertical = 6.dp
                                    ),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                            ){
                                Text(
                                    text = "내 위치 새로고침",
                                    style = TextStyle(
                                        fontFamily = Pretendard,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp,
                                        lineHeight = 14.sp * 1.4,
                                        letterSpacing = 0.sp
                                    ),
                                    color = MainThemeColor.Gray4
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.arrow_rotate_left),
                                    contentDescription = "circles"
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.circles),
                            contentDescription = "범위 지정 이미지"
                        )
                        Image(
                            painter = painterResource(id = R.drawable.center_char),
                            contentDescription = "작가 탐색 중앙 캐릭터"
                        )
                        currentState.nearbyPhotographers.forEach {  ( name, photographerLocation, profileImageUri )  ->
//                            val userLocation = currentState.userLocation
//                            val (relativeX, relativeY) = LocationUtil.calculateRelativeDistance(
//                                from = userLocation!!,
//                                to = photographerLocation
//                            )
                            val dummyUserLocation = LatLng.from(37.402960, 127.115587)
                            val (relativeX, relativeY) = LocationUtil.calculateRelativeDistance(
                                from = dummyUserLocation,
                                to = photographerLocation
                            )
                            val distanceInMeters = getDistance(dummyUserLocation, photographerLocation) * 1000
                            val formattedDistance = String.format("%.0f", distanceInMeters)
                            val screenWidthDp = LocalConfiguration.current.screenWidthDp
                            val maxRadius = screenWidthDp * 0.50f
                            val scale = maxRadius / 2f

                            Image(
                                painter = rememberAsyncImagePainter(model = profileImageUri),
                                contentDescription = "작가 위치",
                                modifier = Modifier
                                    .offset(
                                        x = (relativeX * scale).dp,
                                        y = -(relativeY * scale).dp
                                    )
                                    .size(74.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, MainThemeColor.Black, CircleShape),
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(8.dp)
                                    .offset(
                                        x = (relativeX * scale).dp,
                                        y = -(relativeY * scale).dp
                                    ),
                            )
                            Text(
                                modifier = Modifier
                                    .offset(
                                        x = (relativeX * scale).dp,
                                        y = (-(relativeY * scale)+50).dp
                                    )
                                    .zIndex(1f),
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
                            Text(
                                modifier = Modifier
                                    .offset(
                                        x = (relativeX * scale).dp,
                                        y = (-(relativeY * scale)+63).dp
                                    )
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
