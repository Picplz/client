package com.hm.picplz.ui.screen.search_photographer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.NavigateToPrev
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.viewmodel.SearchPhotograperViewModel

@Composable
fun SearchPhotographerScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchPhotograperViewModel = viewModel(),
    mainNavController: NavHostController,
) {
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
            KakaoMapView()
        }

    }
}

@Preview(showBackground = true)
@Composable
fun SearchPhotographerPreview() {
    PicplzTheme {
        val mainNavController = rememberNavController()
        SearchPhotographerScreen(mainNavController = mainNavController)
    }
}