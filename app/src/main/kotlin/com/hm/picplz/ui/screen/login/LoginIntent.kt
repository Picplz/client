package com.hm.picplz.ui.screen.login

sealed class LoginIntent {
    object NavigateToKaKao : LoginIntent()
    data class LoginSuccess(val token: String) : LoginIntent()
    object LoginFailed : LoginIntent()
}