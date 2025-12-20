package com.github.farzadazimipour.kstate.core

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow

class DefaultStateMachine<S : State, E : Event, Eff : Effect>(
    initialState: S,
    private val reducer: Reducer<S, E, Eff>,
    private val effectHandler: EffectHandler<Eff, E>,
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    private val logger: Logger = NoOpLogger
) : StateMachine<S, E> {

    private val scope = CoroutineScope(SupervisorJob() + dispatcher)
    private val events = Channel<E>(Channel.UNLIMITED)
    private val _state = MutableStateFlow(initialState)
    override val state = _state

    init {
        scope.launch {
            for (event in events) {
                try {
                    val next = reducer.reduce(_state.value, event)
                    _state.value = next.state
                    next.effects.forEach { effect ->
                        scope.launch {
                            try {
                                effectHandler.handle(effect) { dispatch(it) }
                            } catch (e: Exception) {
                                if (e is CancellationException) throw e
                                // Log effect handler errors but don't crash the state machine
                                logger.error("Effect handler error", e)
                            }
                        }
                    }
                } catch (e: Exception) {
                    if (e is CancellationException) throw e
                    // Log reducer errors but don't crash the event processing loop
                    logger.error("Reducer error", e)
                }
            }
        }
    }

    override fun dispatch(event: E) {
        val result = events.trySend(event)
        if (result.isFailure) {
            logger.error("Failed to dispatch event", result.exceptionOrNull())
        }
    }

    override fun cancel() {
        scope.cancel()
    }
}
