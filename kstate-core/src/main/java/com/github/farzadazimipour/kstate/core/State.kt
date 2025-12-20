package com.github.farzadazimipour.kstate.core

/**
 * Marker interface for representing application state in the MVI pattern.
 * 
 * Implementations should be immutable data classes that represent
 * the complete state of a feature or screen at a given point in time.
 * 
 * Example:
 * ```
 * data class LoginState(
 *     val email: String = "",
 *     val loading: Boolean = false,
 *     val error: String? = null
 * ) : State
 * ```
 */
interface State