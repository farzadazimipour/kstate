package com.github.farzadazimipour.sample.app

import com.github.farzadazimipour.kstate.core.Reducer
import com.github.farzadazimipour.sample.app.ui.login.LoginEffect
import com.github.farzadazimipour.sample.app.ui.login.LoginEvent
import com.github.farzadazimipour.sample.app.ui.login.LoginState
import org.junit.Assert.*
import org.junit.Test

class LoginReducerTest {

    private val reducer = Reducer<LoginState, LoginEvent, LoginEffect> { state, event ->
        when (event) {
            is LoginEvent.EmailChanged -> com.github.farzadazimipour.kstate.core.Next(state.copy(email = event.value))
            LoginEvent.Submit -> com.github.farzadazimipour.kstate.core.Next(
                state.copy(loading = true, error = null),
                listOf(LoginEffect.Submit(state.email))
            )
            is LoginEvent.Result -> com.github.farzadazimipour.kstate.core.Next(
                state.copy(
                    loading = false,
                    error = if (event.success) null else "Login failed"
                )
            )
        }
    }

    @Test
    fun `should update email when EmailChanged event is dispatched`() {
        val initialState = LoginState()
        val event = LoginEvent.EmailChanged("test@example.com")
        
        val result = reducer.reduce(initialState, event)
        
        assertEquals("test@example.com", result.state.email)
        assertTrue(result.effects.isEmpty())
    }

    @Test
    fun `should set loading and create submit effect when Submit event is dispatched`() {
        val initialState = LoginState(email = "test@example.com")
        val event = LoginEvent.Submit
        
        val result = reducer.reduce(initialState, event)
        
        assertTrue(result.state.loading)
        assertNull(result.state.error)
        assertEquals(1, result.effects.size)
        assertEquals(LoginEffect.Submit("test@example.com"), result.effects.first())
    }

    @Test
    fun `should clear loading and set error on failed result`() {
        val initialState = LoginState(loading = true)
        val event = LoginEvent.Result(success = false)
        
        val result = reducer.reduce(initialState, event)
        
        assertFalse(result.state.loading)
        assertEquals("Login failed", result.state.error)
        assertTrue(result.effects.isEmpty())
    }

    @Test
    fun `should clear loading and error on successful result`() {
        val initialState = LoginState(loading = true, error = "Previous error")
        val event = LoginEvent.Result(success = true)
        
        val result = reducer.reduce(initialState, event)
        
        assertFalse(result.state.loading)
        assertNull(result.state.error)
        assertTrue(result.effects.isEmpty())
    }
}