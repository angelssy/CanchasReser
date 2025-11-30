package com.example.canchasreser.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.canchasreser.viewmodel.CarritoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(navController: NavController, carritoViewModel: CarritoViewModel) {

    // Consumimos la lista de jugadores desde el ViewModel
    val jugadores by carritoViewModel.jugadores.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Carrito / Jugadores") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text("Jugadores seleccionados:", style = MaterialTheme.typography.titleLarge)

            if (jugadores.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aún no hay jugadores. Agrega con el botón.")
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    jugadores.forEach { id ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Jugador $id", style = MaterialTheme.typography.bodyLarge)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = { carritoViewModel.agregarJugador() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Agregar jugador")
                }

                Button(
                    onClick = { carritoViewModel.eliminarJugador() },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Eliminar jugador")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.navigate("reservaForm") },
                enabled = jugadores.isNotEmpty(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Continuar (Completar nombres)")
            }
        }
    }
}
