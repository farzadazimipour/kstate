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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.farzadazimipour.sample.app.ui.counter.mvi.CounterEvent
import com.github.farzadazimipour.sample.app.ui.counter.mvi.CounterViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CounterViewModelScreen(
    viewModel: CounterViewModel,
    modifier: Modifier = Modifier
) {
    val currentState by viewModel.state.collectAsStateWithLifecycle()

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
                    text = "Using kstate-viewmodel",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "StateMachine wrapped in ViewModel for lifecycle management",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Text(
            text = "${currentState.count}",
            style = MaterialTheme.typography.displayLarge
        )
        
        Text(
            text = "Last Action: ${currentState.lastAction}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row {
            Button(onClick = { viewModel.dispatch(CounterEvent.Decrement) }) {
                Text("-")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { viewModel.dispatch(CounterEvent.Reset) }) {
                Text("Reset")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = { viewModel.dispatch(CounterEvent.Increment) }) {
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
                    text = "• State: count=${currentState.count}, lastAction='${currentState.lastAction}'",
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
                Text(
                    text = "• ViewModel: Survives configuration changes, proper lifecycle",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}