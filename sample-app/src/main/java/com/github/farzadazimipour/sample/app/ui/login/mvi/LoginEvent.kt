package com.github.farzadazimipour.sample.app.ui.login.mvi

import com.github.farzadazimipour.kstate.core.Event

sealed interface LoginEvent : Event {
    data class EmailChanged(val email: String) : LoginEvent
    data class PasswordChanged(val password: String) : LoginEvent
    data object LoginClicked : LoginEvent
    data object LoginSuccess : LoginEvent
    data class LoginFailure(val error: String) : LoginEvent
    data object ClearErrors : LoginEvent
}