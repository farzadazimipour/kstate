package com.github.farzadazimipour.sample.app.ui.cart.mvi

import com.github.farzadazimipour.kstate.core.State

data class CartItem(
    val id: String,
    val name: String,
    val price: Double,
    val quantity: Int
)

data class CartState(
    val items: List<CartItem> = emptyList(),
    val total: Double = 0.0,
    val isLoading: Boolean = false
) : State