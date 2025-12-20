package com.github.farzadazimipour.kstate.compose

import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.farzadazimipour.kstate.core.State
import com.github.farzadazimipour.kstate.core.StateMachine

@Composable
fun <S : State> StateMachine<S, *>.collectStateMachineAsStateWithLifecycle(): S {
    return state.collectAsStateWithLifecycle().value
}