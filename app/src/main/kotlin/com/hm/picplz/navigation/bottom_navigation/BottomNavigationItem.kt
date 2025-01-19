package com.hm.picplz.navigation.bottom_navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavigationItem(
    val route: String,
    val label: String,
    val icon: ImageVector
) {
    object Main : BottomNavigationItem(
        "main",
        "홈",
        Icons.Default.Home
    )

    object Map : BottomNavigationItem(
        "search-photographer",
        "지도",
        Icons.Default.LocationOn
    )

    object Reservation : BottomNavigationItem(
        "reservation",
        "받은 예약",
        Icons.Default.DateRange
    )

    object Feed : BottomNavigationItem(
        "feed",
        "피드",
        Icons.Default.Add
    )

    object Chat : BottomNavigationItem(
        "chat",
        "채팅",
        Icons.Default.Email
    )

    object MyPage : BottomNavigationItem(
        "mypage",
        "마이페이지",
        Icons.Default.AccountCircle
    )
}