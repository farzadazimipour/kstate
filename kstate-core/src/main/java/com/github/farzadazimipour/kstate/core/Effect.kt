package com.github.farzadazimipour.kstate.core

/**
 * Marker interface for side effects in the MVI pattern.
 * 
 * Effects represent side effects like API calls, navigation, or other
 * operations that don't directly modify state but may trigger new events.
 * 
 * Example:
 * ```
 * sealed interface LoginEffect : Effect {
 *     data class SubmitLogin(val email: String, val password: String) : LoginEffect
 *     data object NavigateToHome : LoginEffect
 * }
 * ```
 */
interface Effect