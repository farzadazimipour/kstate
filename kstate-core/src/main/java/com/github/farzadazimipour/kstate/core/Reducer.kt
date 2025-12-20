package com.github.farzadazimipour.kstate.core

fun interface Reducer<S : State, E : Event, Eff : Effect> {
    fun reduce(state: S, event: E): Next<S, Eff>
}