package com.github.farzadazimipour.kstate.viewmodel

import com.github.farzadazimipour.kstate.core.Event
import com.github.farzadazimipour.kstate.core.State
import com.github.farzadazimipour.kstate.core.StateMachine
import io.mockk.*
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Test
import org.junit.Assert.*

class StateMachineViewModelTest {

    data class TestState(val value: Int) : State
    sealed class TestEvent : Event {
        object Increment : TestEvent()
    }

    class TestViewModel(machine: StateMachine<TestState, TestEvent>) : 
        StateMachineViewModel<TestState, TestEvent>(machine) {
        
        // Expose onCleared for testing
        public override fun onCleared() {
            super.onCleared()
        }
    }

    @Test
    fun `state flow is exposed from state machine`() {
        val initialState = TestState(0)
        val stateFlow = MutableStateFlow(initialState)
        val machine = mockk<StateMachine<TestState, TestEvent>>()
        
        every { machine.state } returns stateFlow
        
        val viewModel = TestViewModel(machine)
        
        assertEquals(stateFlow, viewModel.state)
        assertEquals(initialState, viewModel.state.value)
    }

    @Test
    fun `dispatch forwards events to state machine`() {
        val machine = mockk<StateMachine<TestState, TestEvent>>()
        val event = TestEvent.Increment
        
        every { machine.state } returns MutableStateFlow(TestState(0))
        every { machine.dispatch(event) } just Runs
        
        val viewModel = TestViewModel(machine)
        viewModel.dispatch(event)
        
        verify { machine.dispatch(event) }
    }

    @Test
    fun `onCleared cancels state machine`() {
        val machine = mockk<StateMachine<TestState, TestEvent>>()
        
        every { machine.state } returns MutableStateFlow(TestState(0))
        every { machine.cancel() } just Runs
        
        val viewModel = TestViewModel(machine)
        
        // Simulate ViewModel being cleared
        viewModel.onCleared()
        
        verify { machine.cancel() }
    }
}