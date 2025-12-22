package com.github.farzadazimipour.sample.app.ui.counter.mvi

import com.github.farzadazimipour.kstate.core.State

data class CounterState(
    val count: Int = 0,
    val lastAction: String = "Initial"
) : State