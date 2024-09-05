package com.hm.picplz.ui.screen.sign_up

import android.net.Uri
import android.os.Bundle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.os.bundleOf
import androidx.core.view.WindowCompat
import com.hm.picplz.viewmodel.SignUpViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.MainActivity
import com.hm.picplz.ui.theme.PicplzTheme
import kotlinx.coroutines.flow.collectLatest
import com.hm.picplz.ui.screen.sign_up.SignUpIntent.*
import com.hm.picplz.ui.screen.sign_up.common.SignUpNicknameView
import com.hm.picplz.ui.screen.sign_up.common.SignUpProfileImageView
import com.hm.picplz.ui.screen.sign_up.common.SignUpSelectTypeView
import com.hm.picplz.ui.theme.MainThemeColor

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = viewModel(),
    navController: NavController,
) {
    /** 상태바 스타일 설정 **/
    val view = LocalView.current
    val activity = LocalContext.current as? MainActivity

    LaunchedEffect(Unit) {
        activity?.window?.apply {
            statusBarColor = Color.Transparent.toArgb()
            WindowCompat.getInsetsController(this, view).isAppearanceLightStatusBars = true
        }
        viewModel.handleIntent(ResetSelectedUserType)
    }

    val currentState = viewModel.state.collectAsState().value

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MainThemeColor.White
    ) { innerPadding ->
        /** 파일 피커 **/
        val filePickerLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri: Uri? ->
            if (uri != null) {
                viewModel.handleIntent(SetProfileImageUri(uri))
            }
        }

        when(currentState.currentStep) {
            0 -> SignUpNicknameView(
                modifier = modifier,
                innerPadding = innerPadding,
                currentNickname = currentState.nickname,
                onClickBackIcon = {viewModel.handleIntent(NavigateToPrev)},
                onNicknameFieldChange = { newNickname -> viewModel.handleIntent(SetNickname(newNickname))},
                onClickBottomButton = { viewModel.handleIntent(ChangeStep(1)) }
            )
            1 -> SignUpProfileImageView(
                modifier= modifier,
                innerPadding = innerPadding,
                currentNickname = currentState.nickname,
                currentImageUri = currentState.profileImageUri,
                onClickPrevIcon = { viewModel.handleIntent(ChangeStep(0)) },
                onClickBottomButton = { viewModel.handleIntent(ChangeStep(2)) },
                isBottomButtonEnabled = currentState.nickname.isNotEmpty() && currentState.profileImageUri != null,
                filePickerLauncher = filePickerLauncher,
            )
            2 -> SignUpSelectTypeView(
                modifier = modifier,
                innerPadding = innerPadding,
                selectedUserType = currentState.selectedUserType
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpSideEffect.NavigateToSelected -> {
                    val bundle = bundleOf("userInfo" to sideEffect.user)
                    navController.navigate(sideEffect.destination.route, bundle)
                }
                is SignUpSideEffect.NavigateToPrev -> {
                    navController.popBackStack()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview() {
    val navController = rememberNavController()
    PicplzTheme {
        SignUpScreen(navController = navController)
    }
}

fun NavController.navigate(
    route: String,
    args: Bundle,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val nodeId = graph.findNode(route)?.id
    if (nodeId != null) {
        navigate(nodeId, args, navOptions, navigatorExtras)
    }
}