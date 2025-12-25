package com.github.farzadazimipour.sample.app.ui.cart.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.farzadazimipour.sample.app.ui.cart.mvi.*

@Composable
fun CartScreen(
    viewModel: CartViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Shopping Cart",
                style = MaterialTheme.typography.headlineMedium
            )
            Button(
                onClick = { viewModel.dispatch(CartEvent.ClearCart) },
                enabled = state.items.isNotEmpty()
            ) {
                Text("Clear")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = { 
                    viewModel.dispatch(CartEvent.AddItem(
                        CartItem("1", "Apple", 1.50, 1)
                    ))
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Add Apple")
            }
            Button(
                onClick = { 
                    viewModel.dispatch(CartEvent.AddItem(
                        CartItem("2", "Banana", 0.75, 1)
                    ))
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Add Banana")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (state.items.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Cart is empty",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(state.items) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = "$${String.format("%.2f", item.price)}",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = { 
                                        viewModel.dispatch(
                                            CartEvent.UpdateQuantity(item.id, item.quantity - 1)
                                        )
                                    }
                                ) {
                                    Text("-")
                                }
                                Text(
                                    text = "${item.quantity}",
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                IconButton(
                                    onClick = { 
                                        viewModel.dispatch(
                                            CartEvent.UpdateQuantity(item.id, item.quantity + 1)
                                        )
                                    }
                                ) {
                                    Text("+")
                                }
                            }
                        }
                    }
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total:",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "$${String.format("%.2f", state.total)}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}