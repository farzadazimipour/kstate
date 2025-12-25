package com.github.farzadazimipour.sample.app.ui.cart.mvi

import com.github.farzadazimipour.kstate.core.Effect

sealed interface CartEffect : Effect {
    data object RecalculateTotal : CartEffect
}