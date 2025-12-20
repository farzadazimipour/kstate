package com.github.farzadazimipour.kstate.core

fun interface EffectHandler<Eff : Effect, E : Event> {
    suspend fun handle(effect: Eff, emit: (E) -> Unit)
}