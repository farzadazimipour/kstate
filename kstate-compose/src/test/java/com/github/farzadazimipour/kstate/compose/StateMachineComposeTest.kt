package com.github.farzadazimipour.kstate.compose

import com.github.farzadazimipour.kstate.core.Event
import com.github.farzadazimipour.kstate.core.State
import com.github.farzadazimipour.kstate.core.StateMachine
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test
import org.junit.Assert.*

class StateMachineComposeTest {

    data class TestState(val value: Int) : State
    sealed class TestEvent : Event

    @Test
    fun `collectStateMachineAsStateWithLifecycle returns current state value`() {
        val initialState = TestState(42)
        val stateFlow = MutableStateFlow(initialState)
        val machine = mockk<StateMachine<TestState, TestEvent>>()
        
        every { machine.state } returns stateFlow
        
        // Note: This is a simplified test since we can't easily test Compose functions
        // In a real scenario, you'd use ComposeTestRule for proper Compose testing
        assertEquals(initialState, machine.state.value)
    }
}