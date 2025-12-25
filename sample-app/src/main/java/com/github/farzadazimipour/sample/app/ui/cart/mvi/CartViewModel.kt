package com.github.farzadazimipour.sample.app.ui.cart.mvi

import com.github.farzadazimipour.kstate.core.DefaultLogger
import com.github.farzadazimipour.kstate.core.DefaultStateMachine
import com.github.farzadazimipour.kstate.core.Next
import com.github.farzadazimipour.kstate.viewmodel.StateMachineViewModel
import kotlinx.coroutines.delay

class CartViewModel : StateMachineViewModel<CartState, CartEvent>(
    machine = DefaultStateMachine(
        logger = DefaultLogger,
        initialState = CartState(),
        reducer = { state, event ->
            when (event) {
                is CartEvent.AddItem -> {
                    val existingItem = state.items.find { it.id == event.item.id }
                    val updatedItems = if (existingItem != null) {
                        state.items.map { 
                            if (it.id == event.item.id) it.copy(quantity = it.quantity + event.item.quantity)
                            else it
                        }
                    } else {
                        state.items + event.item
                    }
                    Next(
                        state.copy(items = updatedItems),
                        listOf(CartEffect.RecalculateTotal)
                    )
                }

                is CartEvent.RemoveItem -> {
                    val updatedItems = state.items.filter { it.id != event.itemId }
                    Next(
                        state.copy(items = updatedItems),
                        listOf(CartEffect.RecalculateTotal)
                    )
                }

                is CartEvent.UpdateQuantity -> {
                    val updatedItems = if (event.quantity <= 0) {
                        state.items.filter { it.id != event.itemId }
                    } else {
                        state.items.map { 
                            if (it.id == event.itemId) it.copy(quantity = event.quantity)
                            else it
                        }
                    }
                    Next(
                        state.copy(items = updatedItems),
                        listOf(CartEffect.RecalculateTotal)
                    )
                }

                CartEvent.ClearCart -> Next(
                    CartState()
                )

                CartEvent.CalculateTotal -> {
                    val total = state.items.sumOf { it.price * it.quantity }
                    Next(state.copy(total = total, isLoading = false))
                }
            }
        },
        effectHandler = { effect, emit ->
            when (effect) {
                CartEffect.RecalculateTotal -> {
                    delay(100) // Simulate calculation
                    emit(CartEvent.CalculateTotal)
                }
            }
        }
    )
)