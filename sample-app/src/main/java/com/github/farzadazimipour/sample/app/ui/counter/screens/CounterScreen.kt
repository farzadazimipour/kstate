package com.github.farzadazimipour.sample.app.ui.counter.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.farzadazimipour.kstate.compose.collectStateMachineAsStateWithLifecycle
import com.github.farzadazimipour.kstate.core.DefaultStateMachine
import com.github.farzadazimipour.kstate.core.Next
import com.github.farzadazimipour.sample.app.ui.counter.mvi.CounterEffect
import com.github.farzadazimipour.sample.app.ui.counter.mvi.CounterEvent
import com.github.farzadazimipour.sample.app.ui.counter.mvi.CounterState

@Composable
fun CounterComposeScreen(modifier: Modifier = Modifier) {

    val stateMachine = remember {
        DefaultStateMachine<CounterState, CounterEvent, CounterEffect>(
            initialState = CounterState(),
            reducer = { state, event ->
                when (event) {
                    CounterEvent.Increment -> {
                        val newCount = state.count + 1
                        val effects = if (newCount % 10 == 0) {
                            listOf(CounterEffect.ShowToast("Milestone: $newCount!"))
                        } else emptyList()
                        Next(
                            state.copy(count = newCount, lastAction = "Incremented"),
                            effects
                        )
                    }
                    CounterEvent.Decrement -> {
                        val newCount = state.count - 1
                        val effects = if (newCount < 0) {
                            listOf(CounterEffect.Vibrate)
                        } else emptyList()
                        Next(
                            state.copy(count = newCount, lastAction = "Decremented"),
                            effects
                        )
                    }
                    CounterEvent.Reset -> Next(
                        CounterState(lastAction = "Reset"),
                        listOf(CounterEffect.ShowToast("Counter reset!"))
                    )
                    is CounterEvent.ToastShown -> Next(
                        state.copy(lastAction = "Toast: ${event.message}")
                    )
                }
            },
            effectHandler = { effect, emit ->
                when (effect) {
                    is CounterEffect.ShowToast -> {
                        kotlinx.coroutines.delay(100)
                        emit(CounterEvent.ToastShown(effect.message))
                    }
                    CounterEffect.Vibrate -> {
                        kotlinx.coroutines.delay(50)
                        emit(CounterEvent.ToastShown("Vibrated (negative count)"))
                    }
                }
            }
        )
    }
    
    val state = stateMachine.collectStateMachineAsStateWithLifecycle()
    
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Using kstate-compose",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Direct StateMachine integration with Compose",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Text(
            text = "${state.count}",
            style = MaterialTheme.typography.displayLarge
        )
        
        Text(
            text = "Last Action: ${state.lastAction}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row {
            Button(onClick = { stateMachine.dispatch(CounterEvent.Decrement) }) {
                Text("-")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { stateMachine.dispatch(CounterEvent.Reset) }) {
                Text("Reset")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { stateMachine.dispatch(CounterEvent.Increment) }) {
                Text("+")
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "State Management:",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = "• State: count=${state.count}, lastAction='${state.lastAction}'",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "• Events: Increment, Decrement, Reset trigger state changes",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "• Effects: Toast on milestones (every 10), Vibrate on negative",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}