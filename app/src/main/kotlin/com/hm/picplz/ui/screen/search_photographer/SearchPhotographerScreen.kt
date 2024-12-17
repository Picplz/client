package com.hm.picplz.ui.screen.search_photographer

import PhotographerListScreen
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.ui.screen.common.CommonBottomSheetScaffold
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.Pretendard
import com.hm.picplz.viewmodel.SearchPhotographerViewModel
import kotlinx.coroutines.flow.collectLatest

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun SearchPhotographerScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchPhotographerViewModel = viewModel(),
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
                    .height(45.dp)
                    .padding(start = 15.dp),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = "내 주변 작가 찾기",
                    style = TextStyle(
                        fontFamily = Pretendard,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 16.sp * 1.4,
                        letterSpacing = 0.sp
                    )
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
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
        SearchPhotographerScreen(
            tempView = true,
            mainNavController = mainNavController
        )
    }
}
