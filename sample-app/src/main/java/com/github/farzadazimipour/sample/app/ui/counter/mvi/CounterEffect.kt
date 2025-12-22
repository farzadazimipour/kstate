package com.github.farzadazimipour.sample.app.ui.counter.mvi

import com.github.farzadazimipour.kstate.core.Effect

sealed interface CounterEffect : Effect {
    data class ShowToast(val message: String) : CounterEffect
    data object Vibrate : CounterEffect
}