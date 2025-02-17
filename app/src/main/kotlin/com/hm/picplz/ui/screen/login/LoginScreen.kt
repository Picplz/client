import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.MainActivity
import com.hm.picplz.R
import com.hm.picplz.ui.screen.login.LoginIntent
import com.hm.picplz.ui.screen.login.LoginSideEffect
import com.hm.picplz.ui.screen.login.LoginViewModel
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.ui.theme.buttonText
import com.hm.picplz.ui.theme.pretendardTypography
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current
    val view = LocalView.current
    val activity = LocalContext.current as? MainActivity

    LaunchedEffect(Unit) {
        activity?.window?.apply {
            WindowCompat.getInsetsController(this, view).isAppearanceLightStatusBars = false
            statusBarColor = MainThemeColor.Black.toArgb()
        }
    }

    val pageTexts = listOf("내 인생샷 찍어줄\n픽플과 위치기반 매칭!", "인생샷 맛집\n핫플레이스 추천", "나의 인생 프사,\n이젠 픽플즈가 함께")
//    FIXME: 이미지 리소스 대체 필요
    val pageImages = listOf(R.drawable.user_deselected, R.drawable.user_selected, R.drawable.logo)
    val pagerState = rememberPagerState(pageCount = { pageTexts.size })
    val coroutineScope = rememberCoroutineScope()
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val indicatorOffset = remember { mutableStateOf(0.dp) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MainThemeColor.White)
            ) { page ->
                val text = pageTexts[page]
                val imgRes = pageImages[page]

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(screenHeight * 0.65f)
                    ) {
                        Image(
                            painter = painterResource(id = imgRes),
                            contentDescription = "Background Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.matchParentSize()
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(text = text,
                        style = pretendardTypography.headlineMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(Alignment.CenterVertically)
                            .layout { measurable, constraints ->
                                // Measure the text and update the offset
                                val placeable = measurable.measure(constraints)
                                indicatorOffset.value = placeable.height.toDp() + 48.dp
                                layout(placeable.width, placeable.height) {
                                    placeable.place(0, 0)
                                }
                            })

                    if (page == 2) {
                        Spacer(modifier = Modifier.height(66.dp))

                        Button(
                            onClick = { viewModel.handleIntent(LoginIntent.NavigateToKaKao) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFEB3B),
                                contentColor = MainThemeColor.Black
                            ),
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Image(
                                    painter = painterResource(id = R.drawable.kakao),
                                    contentDescription = "Kakao Logo",
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "카카오로 계속하기",
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    style = buttonText
                                )
                            }
                        }
                        // 테스트용 임시 버튼
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { navController.navigate("search-photographer") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 15.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MainThemeColor.Black,
                                contentColor = MainThemeColor.White
                            ),
                            shape = RoundedCornerShape(5.dp)
                        ) {
                            Text(
                                text = "지도 기능 테스트", style = buttonText
                            )
                        }
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = screenHeight * 0.65f + indicatorOffset.value),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    repeat(pagerState.pageCount) { iteration ->
                        val color =
                            if (pagerState.currentPage == iteration) MainThemeColor.Black else MainThemeColor.White
                        Box(modifier = Modifier
                            .padding(5.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(1.dp, MainThemeColor.Black, CircleShape)
                            .size(12.dp)
                            .clickable {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(
                                        page = iteration,
                                        animationSpec = tween(durationMillis = 400) // Adjust the duration as needed
                                    )
                                }
                            })
                    }
                }

            }
        }
    }

// 🔹 ActivityResultLauncher 설정 (카카오 로그인 결과 받기)
    val authLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            if (result.resultCode == Activity.RESULT_OK && data != null) {
                val uri = data.data
                uri?.let {
                    val token = it.getQueryParameter("accessToken") // ✅ 카카오에서 받은 액세스 토큰
                    if (token != null) {
                        viewModel.handleIntent(LoginIntent.LoginSuccess(token)) // 🔹 서버로 전달
                    } else {
                        viewModel.handleIntent(LoginIntent.LoginFailed)
                    }
                }
            }
        }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is LoginSideEffect.NavigateToKaKao -> {
                    /**
                     * Todo : 카카오 로그인 관련 로직 추가
                     *  로그인 성공한 경우 -> 로그인 정보를 가지고 메인 페이지로 이동
                     *  로그인 정보가 없는 경우 -> 카카오와 연동된 회원가입 -> 회원가입 정보를 가지고 sign-up 스크린으로 이동
                     */
                    val kakaoLoginUrl = "http://3.36.183.87:8080/api/v1/oauth2/authorization/kakao"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(kakaoLoginUrl))

                    // 🔹 로그인 화면으로 이동
                    authLauncher.launch(intent)

                    Toast.makeText(context, "카카오 로그인", Toast.LENGTH_LONG).show()
                    Handler(Looper.getMainLooper()).postDelayed({
                        navController.navigate("sign-up") {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }, 500)
                }

                is LoginSideEffect.LoginSuccess -> {
                    Toast.makeText(context, "로그인 성공! 토큰: ${sideEffect.token}", Toast.LENGTH_LONG)
                        .show()
                }

                is LoginSideEffect.LoginFailed -> {
                    Toast.makeText(context, "로그인 실패!", Toast.LENGTH_LONG).show()

                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val navController = rememberNavController()
    PicplzTheme {
        LoginScreen(navController = navController)
    }
}