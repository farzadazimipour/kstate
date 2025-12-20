package com.github.farzadazimipour.kstate.core

data class Next<S : State, Eff : Effect>(
    val state: S,
    val effects: List<Eff> = emptyList()
)
