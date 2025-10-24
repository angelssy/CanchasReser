@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.canchasreser.Uii

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.canchasreser.model.Carrito
import com.example.canchasreser.viewmodel.CarritoViewModel
import com.example.canchasreser.Utils.formatPrecio

@Composable
fun CarritoScreen(navController: NavController, viewModel: CarritoViewModel) {
    val items = remember { viewModel.items }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Carrito de Compras") }) },
        bottomBar = {
            if (items.isNotEmpty()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    // Botón para ir al formulario de reserva
                    Button(
                        onClick = { navController.navigate("reservaForm") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Text("Reservar Cancha")
                    }

                    // Botón para completar compra directa
                    Button(
                        onClick = {
                            navController.navigate("compraExitosa")
                            viewModel.vaciarCarrito()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    ) {
                        Text("Completar Compra (${formatPrecio(viewModel.total())})")
                    }
                }
            }
        }
    ) { padding ->
        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("No hay productos en el carrito")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    CarritoItemRow(item = item, viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun CarritoItemRow(item: Carrito, viewModel: CarritoViewModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(item.cancha.nombre, style = MaterialTheme.typography.titleMedium)
                Text("Precio: ${formatPrecio(item.cancha.precioHora)} x ${item.cantidad}")
            }
            Row {
                Button(onClick = { viewModel.actualizarCantidad(item, item.cantidad - 1) }) {
                    Text("-")
                }
                Spacer(modifier = Modifier.width(4.dp))
                Button(onClick = { viewModel.actualizarCantidad(item, item.cantidad + 1) }) {
                    Text("+")
                }
            }
        }
    }
}
