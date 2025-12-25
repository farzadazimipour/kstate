package com.github.farzadazimipour.sample.app.ui.login.mvi

import com.github.farzadazimipour.kstate.core.State

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null
) : State