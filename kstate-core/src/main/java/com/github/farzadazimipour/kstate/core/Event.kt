package com.github.farzadazimipour.kstate.core

/**
 * Marker interface for events in the MVI pattern.
 * 
 * Events represent user actions or system events that can trigger state changes.
 * Should be implemented as sealed classes or interfaces for type safety.
 * 
 * Example:
 * ```
 * sealed interface LoginEvent : Event {
 *     data class EmailChanged(val email: String) : LoginEvent
 *     data object Submit : LoginEvent
 * }
 * ```
 */
interface Event