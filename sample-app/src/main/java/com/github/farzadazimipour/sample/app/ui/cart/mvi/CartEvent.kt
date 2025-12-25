package com.github.farzadazimipour.sample.app.ui.cart.mvi

import com.github.farzadazimipour.kstate.core.Event

sealed interface CartEvent : Event {
    data class AddItem(val item: CartItem) : CartEvent
    data class RemoveItem(val itemId: String) : CartEvent
    data class UpdateQuantity(val itemId: String, val quantity: Int) : CartEvent
    data object ClearCart : CartEvent
    data object CalculateTotal : CartEvent
}