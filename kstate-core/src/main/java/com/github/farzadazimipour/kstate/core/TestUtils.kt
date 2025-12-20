package com.github.farzadazimipour.kstate.core

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Test implementation of StateMachine for unit testing
 */
class TestStateMachine<S : State, E : Event>(
    initialState: S
) : StateMachine<S, E> {
    
    private val _state = MutableStateFlow(initialState)
    override val state: StateFlow<S> = _state
    
    private val _dispatchedEvents = mutableListOf<E>()
    val dispatchedEvents: List<E> get() = _dispatchedEvents.toList()
    
    override fun dispatch(event: E) {
        _dispatchedEvents.add(event)
    }
    
    override fun cancel() {
        // No-op for testing
    }
    
    /**
     * Manually set the state for testing purposes
     */
    fun setState(newState: S) {
        _state.value = newState
    }
    
    /**
     * Clear the list of dispatched events
     */
    fun clearDispatchedEvents() {
        _dispatchedEvents.clear()
    }
}

/**
 * Test effect handler that captures effects instead of executing them
 */
class TestEffectHandler<Eff : Effect, E : Event> : EffectHandler<Eff, E> {
    
    private val _handledEffects = mutableListOf<Eff>()
    val handledEffects: List<Eff> get() = _handledEffects.toList()
    
    override suspend fun handle(effect: Eff, emit: (E) -> Unit) {
        _handledEffects.add(effect)
    }
    
    /**
     * Clear the list of handled effects
     */
    fun clearHandledEffects() {
        _handledEffects.clear()
    }
}