package com.github.farzadazimipour.sample.app.ui.counter.mvi

import com.github.farzadazimipour.kstate.core.DefaultLogger
import com.github.farzadazimipour.kstate.core.DefaultStateMachine
import com.github.farzadazimipour.kstate.core.Next
import com.github.farzadazimipour.kstate.viewmodel.StateMachineViewModel
import kotlinx.coroutines.delay

class CounterViewModel : StateMachineViewModel<CounterState, CounterEvent>(
    machine = DefaultStateMachine(
        logger = DefaultLogger,
        initialState = CounterState(),
        reducer = { state, event ->
            when (event) {
                CounterEvent.Increment -> {
                    val newCount = state.count + 1
                    val effects = if (newCount % 10 == 0) {
                        listOf(CounterEffect.ShowToast("Milestone: $newCount!"))
                    } else emptyList()
                    Next(
                        state.copy(count = newCount, lastAction = "Incremented"),
                        effects
                    )
                }

                CounterEvent.Decrement -> {
                    val newCount = state.count - 1
                    val effects = if (newCount < 0) {
                        listOf(CounterEffect.Vibrate)
                    } else emptyList()
                    Next(
                        state.copy(count = newCount, lastAction = "Decremented"),
                        effects
                    )
                }

                CounterEvent.Reset -> Next(
                    CounterState(lastAction = "Reset"),
                    listOf(CounterEffect.ShowToast("Counter reset!"))
                )

                is CounterEvent.ToastShown -> Next(
                    state.copy(lastAction = "Toast: ${event.message}")
                )
            }
        },
        effectHandler = { effect, emit ->
            when (effect) {
                is CounterEffect.ShowToast -> {
                    delay(100) // Simulate toast display
                    emit(CounterEvent.ToastShown(effect.message))
                }

                CounterEffect.Vibrate -> {
                    delay(50) // Simulate vibration
                    emit(CounterEvent.ToastShown("Vibrated (negative count)"))
                }
            }
        }
    )
)