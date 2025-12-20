package com.github.farzadazimipour.kstate.core

import kotlinx.coroutines.flow.StateFlow

interface StateMachine<S : State, E : Event> {
    val state: StateFlow<S>
    fun dispatch(event: E)
    fun cancel()
}