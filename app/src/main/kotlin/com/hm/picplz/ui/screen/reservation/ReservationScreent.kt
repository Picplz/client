package com.hm.picplz.ui.screen.my_page

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.hm.picplz.navigation.bottom_navigation.BottomNavigationBar
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun ReservationScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    Scaffold(
        backgroundColor = MainThemeColor.White,
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) { innerPadding ->
        Text(
            text = "받은예약 화면",
            modifier = Modifier.padding(innerPadding)
        )
    }
}