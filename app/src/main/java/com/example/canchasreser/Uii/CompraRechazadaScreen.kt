package com.example.canchasreser.Uii

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class) // <-- Aquí indicamos que usamos API experimental
@Composable
fun CompraRechazadaScreen(navController: NavController, mensajeError: String) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Compra Rechazada") }) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "❌ Compra Rechazada",
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = mensajeError,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Botón para volver al carrito
                Button(onClick = { navController.navigate("carrito") }) {
                    Text("Volver al Carrito")
                }

                // Botón para reintentar pago (lleva al formulario de reserva)
                Button(onClick = { navController.navigate("reservaForm") }) {
                    Text("Reintentar Pago")
                }
            }
        }
    }
}
