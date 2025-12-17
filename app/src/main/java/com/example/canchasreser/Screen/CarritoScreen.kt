package com.example.canchasreser.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.canchasreser.R
import com.example.canchasreser.viewmodel.CarritoViewModel
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(navController: NavController, carritoViewModel: CarritoViewModel) {

    val jugadores by carritoViewModel.jugadores.collectAsState()
    var jugadorSeleccionado by remember { mutableStateOf<Int?>(null) }
    var zoom by remember { mutableStateOf(1f) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Formación del Equipo") },
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
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            // ✅ CANCHA CON JUGADORES DENTRO
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .graphicsLayer(scaleX = zoom, scaleY = zoom)
                    .clickable { zoom = if (zoom == 1f) 1.4f else 1f }
            ) {

                Image(
                    painter = painterResource(id = R.drawable.canchita),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )

                // ✅ POSICIONES REALES SOBRE LA CANCHA
                val posiciones = listOf(
                    Pair(0.43f, 0.34f), // Arquero
                    Pair(0.32f, 0.53f), // Arquero
                    Pair(0.57f, 0.53f), Pair(0.7f, 0.32f), Pair(0.25f, 0.84f),
                    Pair(0.82f, 0.53f), Pair(0.45f, 0.85f), Pair(0.9f, 0.84f),
                    Pair(0.56f, 0.71f), Pair(0.67f, 0.85f), Pair(0.55f, 0.99f),
                    Pair(0.7f, 0.67f)
                )

                jugadores.take(11).forEachIndexed { index, jugador ->

                    val x = posiciones[index].first
                    val y = posiciones[index].second

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = (x * 300).dp, top = (y * 400).dp)
                            .clickable { jugadorSeleccionado = index }
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(6.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(6.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (jugador.nombre.isBlank()) "Jugador ${index + 1}" else jugador.nombre,
                                    style = MaterialTheme.typography.bodySmall
                                )
                                if (jugador.rut.isNotBlank()) {
                                    Text(jugador.rut, style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ✅ BOTÓN CONTINUAR DIRECTO A RESERVA
            Button(
                onClick = { navController.navigate("reservaForm") },
                enabled = carritoViewModel.hayJugadoresCompletos(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(55.dp)
            ) {
                Text("Continuar a la Reserva")
            }
        }

        // ✅ DIÁLOGO PARA EDITAR JUGADOR
        if (jugadorSeleccionado != null) {
            var nombre by remember { mutableStateOf(jugadores[jugadorSeleccionado!!].nombre) }
            var rut by remember { mutableStateOf(jugadores[jugadorSeleccionado!!].rut) }

            AlertDialog(
                onDismissRequest = { jugadorSeleccionado = null },
                confirmButton = {
                    Button(onClick = {
                        carritoViewModel.actualizarJugador(jugadorSeleccionado!!, nombre, rut)
                        jugadorSeleccionado = null
                    }) {
                        Text("Guardar")
                    }
                },
                dismissButton = {
                    Button(onClick = { jugadorSeleccionado = null }) {
                        Text("Cancelar")
                    }
                },
                title = { Text("Jugador ${jugadorSeleccionado!! + 1}") },
                text = {
                    Column {
                        OutlinedTextField(
                            value = nombre,
                            onValueChange = { nombre = it },
                            label = { Text("Nombre") }
                        )
                        OutlinedTextField(
                            value = rut,
                            onValueChange = { rut = it },
                            label = { Text("RUT") }
                        )
                    }
                }
            )
        }
    }
}

