package com.hm.picplz.ui.screen.login

sealed class LoginSideEffect {
    object NavigateToKaKao : LoginSideEffect()
    data class LoginSuccess(val token: String) : LoginSideEffect()
    object LoginFailed : LoginSideEffect()
}