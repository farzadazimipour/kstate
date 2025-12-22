package com.github.farzadazimipour.sample.app.ui.counter.mvi

import com.github.farzadazimipour.kstate.core.Event

sealed interface CounterEvent : Event {
    data object Increment : CounterEvent
    data object Decrement : CounterEvent
    data object Reset : CounterEvent
    data class ToastShown(val message: String) : CounterEvent
}