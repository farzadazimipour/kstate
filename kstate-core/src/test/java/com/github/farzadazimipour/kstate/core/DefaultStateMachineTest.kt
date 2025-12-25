package com.github.farzadazimipour.kstate.core

import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class DefaultStateMachineTest {

    // Test state and events
    data class TestState(val value: Int) : State
    sealed class TestEvent : Event {
        object Increment : TestEvent()
        object Decrement : TestEvent()
        data class SetValue(val value: Int) : TestEvent()
    }
    sealed class TestEffect : Effect {
        data class LogValue(val value: Int) : TestEffect()
        object ThrowError : TestEffect()
    }

    @Test
    fun `initial state is set correctly`() = runTest {
        val initialState = TestState(0)
        val reducer = mockk<Reducer<TestState, TestEvent, TestEffect>>()
        val effectHandler = mockk<EffectHandler<TestEffect, TestEvent>>()
        
        val stateMachine = DefaultStateMachine(
            initialState = initialState,
            reducer = reducer,
            effectHandler = effectHandler,
            dispatcher = UnconfinedTestDispatcher()
        )
        
        assertEquals(initialState, stateMachine.state.value)
        stateMachine.cancel()
    }

    @Test
    fun `dispatch event updates state through reducer`() = runTest {
        val initialState = TestState(0)
        val newState = TestState(1)
        val event = TestEvent.Increment
        
        val reducer = mockk<Reducer<TestState, TestEvent, TestEffect>>()
        val effectHandler = mockk<EffectHandler<TestEffect, TestEvent>>()
        
        every { reducer.reduce(initialState, event) } returns Next(newState)
        
        val stateMachine = DefaultStateMachine(
            initialState = initialState,
            reducer = reducer,
            effectHandler = effectHandler,
            dispatcher = UnconfinedTestDispatcher()
        )
        
        stateMachine.dispatch(event)
        advanceUntilIdle()
        
        assertEquals(newState, stateMachine.state.value)
        verify { reducer.reduce(initialState, event) }
        stateMachine.cancel()
    }

    @Test
    fun `effects are handled when returned from reducer`() = runTest {
        val initialState = TestState(0)
        val newState = TestState(1)
        val event = TestEvent.Increment
        val effect = TestEffect.LogValue(1)
        
        val reducer = mockk<Reducer<TestState, TestEvent, TestEffect>>()
        val effectHandler = mockk<EffectHandler<TestEffect, TestEvent>>()
        
        every { reducer.reduce(initialState, event) } returns Next(newState, listOf(effect))
        coEvery { effectHandler.handle(effect, any()) } just Runs
        
        val stateMachine = DefaultStateMachine(
            initialState = initialState,
            reducer = reducer,
            effectHandler = effectHandler,
            dispatcher = UnconfinedTestDispatcher()
        )
        
        stateMachine.dispatch(event)
        advanceUntilIdle()
        
        assertEquals(newState, stateMachine.state.value)
        coVerify { effectHandler.handle(effect, any()) }
        stateMachine.cancel()
    }

    @Test
    fun `effect handler can emit new events`() = runTest {
        val initialState = TestState(0)
        val intermediateState = TestState(1)
        val finalState = TestState(2)
        val firstEvent = TestEvent.Increment
        val secondEvent = TestEvent.Increment
        val effect = TestEffect.LogValue(1)
        
        val reducer = mockk<Reducer<TestState, TestEvent, TestEffect>>()
        val effectHandler = mockk<EffectHandler<TestEffect, TestEvent>>()
        
        every { reducer.reduce(initialState, firstEvent) } returns Next(intermediateState, listOf(effect))
        every { reducer.reduce(intermediateState, secondEvent) } returns Next(finalState)
        
        coEvery { effectHandler.handle(effect, any()) } answers {
            val emit = secondArg<(TestEvent) -> Unit>()
            emit(secondEvent)
        }
        
        val stateMachine = DefaultStateMachine(
            initialState = initialState,
            reducer = reducer,
            effectHandler = effectHandler,
            dispatcher = UnconfinedTestDispatcher()
        )
        
        stateMachine.dispatch(firstEvent)
        advanceUntilIdle()
        
        assertEquals(finalState, stateMachine.state.value)
        stateMachine.cancel()
    }

    @Test
    fun `reducer errors are logged but don't crash state machine`() = runTest {
        val initialState = TestState(0)
        val event = TestEvent.Increment
        val logger = mockk<Logger>()
        
        val reducer = mockk<Reducer<TestState, TestEvent, TestEffect>>()
        val effectHandler = mockk<EffectHandler<TestEffect, TestEvent>>()
        
        val exception = RuntimeException("Reducer error")
        every { reducer.reduce(initialState, event) } throws exception
        every { logger.error(any(), any()) } just Runs
        
        val stateMachine = DefaultStateMachine(
            initialState = initialState,
            reducer = reducer,
            effectHandler = effectHandler,
            dispatcher = UnconfinedTestDispatcher(),
            logger = logger
        )
        
        stateMachine.dispatch(event)
        advanceUntilIdle()
        
        // State should remain unchanged
        assertEquals(initialState, stateMachine.state.value)
        verify { logger.error("Reducer error", exception) }
        stateMachine.cancel()
    }

    @Test
    fun `effect handler errors are logged but don't crash state machine`() = runTest {
        val initialState = TestState(0)
        val newState = TestState(1)
        val event = TestEvent.Increment
        val effect = TestEffect.ThrowError
        val logger = mockk<Logger>()
        
        val reducer = mockk<Reducer<TestState, TestEvent, TestEffect>>()
        val effectHandler = mockk<EffectHandler<TestEffect, TestEvent>>()
        
        val exception = RuntimeException("Effect handler error")
        every { reducer.reduce(initialState, event) } returns Next(newState, listOf(effect))
        coEvery { effectHandler.handle(effect, any()) } throws exception
        every { logger.error(any(), any()) } just Runs
        
        val stateMachine = DefaultStateMachine(
            initialState = initialState,
            reducer = reducer,
            effectHandler = effectHandler,
            dispatcher = UnconfinedTestDispatcher(),
            logger = logger
        )
        
        stateMachine.dispatch(event)
        advanceUntilIdle()
        
        // State should be updated despite effect handler error
        assertEquals(newState, stateMachine.state.value)
        verify { logger.error("Effect handler error", exception) }
        stateMachine.cancel()
    }

    @Test
    fun `multiple events are processed in order`() = runTest {
        val initialState = TestState(0)
        val state1 = TestState(1)
        val state2 = TestState(0)
        val state3 = TestState(5)
        
        val reducer = mockk<Reducer<TestState, TestEvent, TestEffect>>()
        val effectHandler = mockk<EffectHandler<TestEffect, TestEvent>>()
        
        every { reducer.reduce(initialState, TestEvent.Increment) } returns Next(state1)
        every { reducer.reduce(state1, TestEvent.Decrement) } returns Next(state2)
        every { reducer.reduce(state2, TestEvent.SetValue(5)) } returns Next(state3)
        
        val stateMachine = DefaultStateMachine(
            initialState = initialState,
            reducer = reducer,
            effectHandler = effectHandler,
            dispatcher = UnconfinedTestDispatcher()
        )
        
        stateMachine.dispatch(TestEvent.Increment)
        stateMachine.dispatch(TestEvent.Decrement)
        stateMachine.dispatch(TestEvent.SetValue(5))
        advanceUntilIdle()
        
        assertEquals(state3, stateMachine.state.value)
        verifyOrder {
            reducer.reduce(initialState, TestEvent.Increment)
            reducer.reduce(state1, TestEvent.Decrement)
            reducer.reduce(state2, TestEvent.SetValue(5))
        }
        stateMachine.cancel()
    }

    @Test
    fun `cancel stops the state machine`() = runTest {
        val initialState = TestState(0)
        val reducer = mockk<Reducer<TestState, TestEvent, TestEffect>>()
        val effectHandler = mockk<EffectHandler<TestEffect, TestEvent>>()
        
        val stateMachine = DefaultStateMachine(
            initialState = initialState,
            reducer = reducer,
            effectHandler = effectHandler,
            dispatcher = UnconfinedTestDispatcher()
        )
        
        stateMachine.cancel()
        
        // After cancellation, dispatching events should not process them
        stateMachine.dispatch(TestEvent.Increment)
        advanceUntilIdle()
        
        verify(exactly = 0) { reducer.reduce(any(), any()) }
    }
}