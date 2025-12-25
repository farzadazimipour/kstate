package com.github.farzadazimipour.sample.app.ui.login.mvi

import com.github.farzadazimipour.kstate.core.Effect

sealed interface LoginEffect : Effect {
    data class AttemptLogin(val email: String, val password: String) : LoginEffect
}