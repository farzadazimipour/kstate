package com.github.farzadazimipour.sample.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.farzadazimipour.sample.app.ui.counter.mvi.CounterViewModel
import com.github.farzadazimipour.sample.app.ui.counter.screens.CounterComposeScreen
import com.github.farzadazimipour.sample.app.ui.counter.screens.CounterViewModelScreen
import com.github.farzadazimipour.sample.app.ui.login.mvi.LoginViewModel
import com.github.farzadazimipour.sample.app.ui.login.screens.LoginScreen
import com.github.farzadazimipour.sample.app.ui.theme.KstateTheme

class MainActivity : ComponentActivity() {
    private val counterViewModel: CounterViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KstateTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppTabs(
                        counterViewModel = counterViewModel,
                        loginViewModel = loginViewModel,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AppTabs(
    counterViewModel: CounterViewModel,
    loginViewModel: LoginViewModel,
    modifier: Modifier = Modifier,
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Counter VM", "Counter Compose", "Login")

    Column(modifier = modifier) {
        SecondaryTabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                )
            }
        }

        when (selectedTab) {
            0 -> CounterViewModelScreen(viewModel = counterViewModel)
            1 -> CounterComposeScreen()
            2 -> LoginScreen(viewModel = loginViewModel)
        }
    }
}