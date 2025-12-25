package com.github.farzadazimipour.kstate.core

import org.junit.Test
import org.junit.Assert.*

class NextTest {

    data class TestState(val value: Int) : State
    data class TestEffect(val message: String) : Effect

    @Test
    fun `Next with state only has empty effects list`() {
        val state = TestState(1)
        val next = Next<TestState, TestEffect>(state)
        
        assertEquals(state, next.state)
        assertTrue(next.effects.isEmpty())
    }

    @Test
    fun `Next with state and effects`() {
        val state = TestState(1)
        val effects = listOf(TestEffect("effect1"), TestEffect("effect2"))
        val next = Next(state, effects)
        
        assertEquals(state, next.state)
        assertEquals(effects, next.effects)
        assertEquals(2, next.effects.size)
    }

    @Test
    fun `Next data class equality`() {
        val state = TestState(1)
        val effects = listOf(TestEffect("effect1"))
        
        val next1 = Next(state, effects)
        val next2 = Next(state, effects)
        val next3 = Next(TestState(2), effects)
        
        assertEquals(next1, next2)
        assertNotEquals(next1, next3)
    }
}