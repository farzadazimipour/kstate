package com.github.farzadazimipour.sample.app.ui.login.mvi

import com.github.farzadazimipour.kstate.core.DefaultLogger
import com.github.farzadazimipour.kstate.core.DefaultStateMachine
import com.github.farzadazimipour.kstate.core.Next
import com.github.farzadazimipour.kstate.viewmodel.StateMachineViewModel
import kotlinx.coroutines.delay

class LoginViewModel : StateMachineViewModel<LoginState, LoginEvent>(
    machine = DefaultStateMachine(
        logger = DefaultLogger,
        initialState = LoginState(),
        reducer = { state, event ->
            when (event) {
                is LoginEvent.EmailChanged -> Next(
                    state.copy(
                        email = event.email,
                        emailError = null,
                        generalError = null
                    )
                )

                is LoginEvent.PasswordChanged -> Next(
                    state.copy(
                        password = event.password,
                        passwordError = null,
                        generalError = null
                    )
                )

                LoginEvent.LoginClicked -> {
                    val emailError = if (state.email.isBlank()) "Email is required"
                    else if (!state.email.contains("@")) "Invalid email format"
                    else null

                    val passwordError = if (state.password.isBlank()) "Password is required"
                    else if (state.password.length < 6) "Password must be at least 6 characters"
                    else null

                    if (emailError != null || passwordError != null) {
                        Next(state.copy(emailError = emailError, passwordError = passwordError))
                    } else {
                        Next(
                            state.copy(isLoading = true, emailError = null, passwordError = null),
                            listOf(LoginEffect.AttemptLogin(state.email, state.password))
                        )
                    }
                }

                LoginEvent.LoginSuccess -> Next(
                    state.copy(isLoading = false, isLoggedIn = true)
                )

                is LoginEvent.LoginFailure -> Next(
                    state.copy(isLoading = false, generalError = event.error)
                )

                LoginEvent.ClearErrors -> Next(
                    state.copy(emailError = null, passwordError = null, generalError = null)
                )
            }
        },
        effectHandler = { effect, emit ->
            when (effect) {
                is LoginEffect.AttemptLogin -> {
                    delay(2000) // Simulate network call
                    if (effect.email == "test@example.com" && effect.password == "password123") {
                        emit(LoginEvent.LoginSuccess)
                    } else {
                        emit(LoginEvent.LoginFailure("Invalid credentials"))
                    }
                }
            }
        }
    )
)