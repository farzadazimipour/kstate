package com.github.farzadazimipour.kstate.viewmodel

import androidx.lifecycle.ViewModel
import com.github.farzadazimipour.kstate.core.Event
import com.github.farzadazimipour.kstate.core.State
import com.github.farzadazimipour.kstate.core.StateMachine

abstract class StateMachineViewModel<S : State, E : Event>(
    protected val machine: StateMachine<S, E>
) : ViewModel() {

    val state = machine.state

    fun dispatch(event: E) {
        machine.dispatch(event)
    }

    override fun onCleared() {
        super.onCleared()
        machine.cancel()
    }
}