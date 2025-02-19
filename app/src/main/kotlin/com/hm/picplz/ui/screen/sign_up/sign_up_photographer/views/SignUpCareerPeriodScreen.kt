package com.hm.picplz.ui.screen.sign_up.sign_up_photographer.views

import CommonOutlinedTextField
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hm.picplz.ui.screen.common.CommonBottomButton
import com.hm.picplz.ui.screen.common.CommonNumberSelector
import com.hm.picplz.ui.screen.common.CommonTopBar
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SelectorType
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerIntent.*
import com.hm.picplz.ui.screen.sign_up.sign_up_photographer.SignUpPhotographerSideEffect
import com.hm.picplz.ui.theme.MainThemeColor
import com.hm.picplz.ui.theme.PicplzTheme
import com.hm.picplz.viewmodel.SignUpPhotographerViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SignUpCareerPeriodScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpPhotographerViewModel = viewModel(),
    signUpPhotographerNavController: NavController
) {
    val currentState = viewModel.state.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.handleIntent(InitializeCareerValues)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MainThemeColor.White
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
        ) {
            CommonTopBar(
                text = "경력 선택",
                onClickBack = {viewModel.handleIntent(NavigateToPrev)}
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 15.dp)
                    .imePadding(),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Spacer(
                        modifier = Modifier
                            .height(80.dp)
                    )
                    Text(
                        text = "경력 기간을 입력해주세요.",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(
                        modifier = Modifier
                            .height(15.dp)
                    )
                    Text(
                        text = "1년 미만일 경우 0년 n개월로 입력해주세요.",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(
                        modifier = Modifier
                            .height(30.dp),
                    )
                    Row(
                        modifier = Modifier
                            .height(40.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        CommonOutlinedTextField(
                            modifier = Modifier.width(85.dp),
                            value = currentState.yearValue?.toString() ?: "",
                            onValueChange = {},
                            placeholder = "0",
                            imeAction = ImeAction.Next,
                            keyboardActions = {},
                            readOnly = true,
                            showError = false,
                            textStyle = MaterialTheme.typography.titleMedium.copy(
                                textAlign = TextAlign.Center
                            ),
                            placeholderStyle = MaterialTheme.typography.titleMedium.copy(
                                textAlign = TextAlign.Center,
                                color = MainThemeColor.Gray
                            ),
                            onClick = {
                                viewModel.handleIntent(SetSelectedSelector(SelectorType.YEAR))
                            },
                        )
                        Spacer(modifier = modifier.width(7.dp))
                        Text(
                            text = "년",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Spacer(modifier = modifier.width(20.dp))
                        CommonOutlinedTextField(
                            modifier = Modifier.width(85.dp),
                            value = currentState.monthValue?.toString() ?: "",
                            onValueChange = {},
                            placeholder = "0",
                            imeAction = ImeAction.Next,
                            keyboardActions = {},
                            readOnly = true,
                            showError = false,
                            textStyle = MaterialTheme.typography.titleMedium.copy(
                                textAlign = TextAlign.Center
                            ),
                            placeholderStyle = MaterialTheme.typography.titleMedium.copy(
                                textAlign = TextAlign.Center,
                                color = MainThemeColor.Gray
                            ),
                            onClick = {
                                viewModel.handleIntent(SetSelectedSelector(SelectorType.MONTH))
                            },
                        )
                        Spacer(modifier = modifier.width(7.dp))
                        Text(
                            text = "개월",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                contentAlignment = Alignment.Center
            ) {
                CommonBottomButton(
                    text = "다음",
                    onClick = {
                        viewModel.handleIntent(SetCareerPeriod)
                        viewModel.handleIntent(InitializeCareerValues)
                    },
                    enabled = currentState.yearValue != null && currentState.monthValue != null,
                    containerColor = MainThemeColor.Black
                )
            }
        }
    }

    CommonNumberSelector(
        currentValue = currentState.yearValue,
        onValueSelected = { year ->
            viewModel.handleIntent(SetYearValue(year))
            viewModel.handleIntent(SetSelectedSelector(SelectorType.NONE))
        },
        maxValue = 50,
        visible = currentState.selectedSelector === SelectorType.YEAR,
        onDismiss = { viewModel.handleIntent(SetSelectedSelector(SelectorType.NONE)) }
    )
    CommonNumberSelector(
        currentValue = currentState.monthValue,
        onValueSelected = { month ->
            viewModel.handleIntent(SetMonthValue(month))
            viewModel.handleIntent(SetSelectedSelector(SelectorType.NONE))
        },
        maxValue = 12,
        visible = currentState.selectedSelector === SelectorType.MONTH,
        onDismiss = { viewModel.handleIntent(SetSelectedSelector(SelectorType.NONE)) }
    )

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { sideEffect ->
            when (sideEffect) {
                is SignUpPhotographerSideEffect.NavigateToPrev -> {
                    signUpPhotographerNavController.popBackStack()
                }
                is SignUpPhotographerSideEffect.Navigate -> {
                    signUpPhotographerNavController.navigate(sideEffect.destination)
                }
                else -> {}
            }
        }
    }
}

@Preview (showBackground = true)
@Composable
fun SignUpCareerPeriodScreenPreview() {
    PicplzTheme {
        val signUpPhotographerNavController = rememberNavController()
        SignUpCareerPeriodScreen(
            signUpPhotographerNavController = signUpPhotographerNavController
        )
    }
}