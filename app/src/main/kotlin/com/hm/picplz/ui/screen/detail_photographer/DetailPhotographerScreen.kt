package com.hm.picplz.ui.screen.detail_photographer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailPhotographerScreen(navController: NavHostController) {
    val paddingModifier = Modifier.padding(horizontal = 15.dp)
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MainThemeColor.White,
        topBar = {
            CommonTopBar(text = "", onClickBack = { /*TODO*/ })
        },
        floatingActionButton = {
            Box(modifier = Modifier.padding(start = 30.dp)) {
                CommonBottomButton(
                    text = "예약하기",
                    onClick = { },
                    containerColor = MainThemeColor.Black,
                )
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth() // fillMaxWidth로 너비를 맞추고
                    .verticalScroll(rememberScrollState()) // verticalScroll을 적용
                    .padding(innerPadding) // innerPadding을 사용
            ) {
                DetailProfileSection(modifier = paddingModifier)

                Divider(
                    color = MainThemeColor.Gray2,
                    thickness = 10.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                )

                ReviewSection(modifier = paddingModifier)

                Spacer(modifier = Modifier.height(30.dp))

                PortfolioSection(modifier = paddingModifier)

                Spacer(modifier = Modifier.height(30.dp))

                PhotoPriceSection(modifier = paddingModifier)

                Spacer(modifier = Modifier.height(130.dp))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DetailPhotographerScreenPreview() {
    val navController = rememberNavController()

    PicplzTheme {
        DetailPhotographerScreen(navController = navController)
    }
}