package com.example.canchasreser.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.canchasreser.R
import com.example.canchasreser.viewmodel.CarritoViewModel
import androidx.compose.ui.graphics.Color

import com.example.canchasreser.Utils.validarRut
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarritoScreen(
    navController: NavController,
    carritoViewModel: CarritoViewModel,
    tipoCancha: String
) {

    // actualizar modo
    LaunchedEffect(tipoCancha) {
        carritoViewModel.setDeporte(tipoCancha)
    }

    val jugadores by carritoViewModel.jugadores.collectAsState(initial = emptyList())
    val equipoActual by carritoViewModel.equipoActual.collectAsState()
    val modo by carritoViewModel.modo.collectAsState()

    var jugadorSeleccionado by remember { mutableStateOf<Int?>(null) }
    var zoom by remember { mutableStateOf(1f) }

    val imagenCancha = when (modo) {
        "voley" -> R.drawable.formvoley
        "basquet" -> R.drawable.formbas
        "tenis" -> R.drawable.formtenis
        else -> R.drawable.canchita
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Formación Equipo $tipoCancha") },
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

            var boxWidth by remember { mutableStateOf(0f) }
            var boxHeight by remember { mutableStateOf(0f) }

            val density = LocalDensity.current

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp)
                    .graphicsLayer(scaleX = zoom, scaleY = zoom)
                    .clickable { zoom = if (zoom == 1f) 1.4f else 1f }
            ) {

                Image(
                    painter = painterResource(id = imagenCancha),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .onGloballyPositioned { coordinates ->
                            boxWidth = coordinates.size.width.toFloat()
                            boxHeight = coordinates.size.height.toFloat()
                        }
                )

                // posiciones correctas por deporte 👇
                val posiciones = when (modo) {

                    "voley" -> listOf(
                        Pair(0.60f, 0.65f), Pair(0.18f, 0.15f), Pair(0.65f, 0.15f),
                        Pair(0.23f, 0.65f), Pair(0.40f, 0.15f), Pair(0.40f, 0.28f),

                        Pair(0.18f, 0.86f), Pair(0.64f, 0.86f), Pair(0.23f, 0.35f),
                        Pair(0.40f, 0.86f), Pair(0.57f, 0.35f), Pair(0.40f, 0.70f)
                    )

                    "basquet" -> listOf(
                        Pair(0.50f, 0.80f),
                        Pair(0.30f, 0.55f),
                        Pair(0.70f, 0.55f),
                        Pair(0.40f, 0.30f),
                        Pair(0.60f, 0.30f)
                    )

                    "tenis" -> listOf(
                        Pair(0.50f, 0.75f),
                        Pair(0.50f, 0.25f)
                    )

                    else -> listOf(
                        Pair(0.64f, 0.47f),  // delantero centro
                        Pair(0.28f, 0.30f),  // extremo izq
                        Pair(0.53f, 0.30f),  // extremo der

                        Pair(0.43f, 0.47f),  // mediocentro
                        Pair(0.22f, 0.47f),  // volante izq
                        Pair(0.41f, 0.63f),  // volante der

                        Pair(0.16f, 0.74f),  // defensa izq
                        Pair(0.33f, 0.74f),  // defensa der
                        Pair(0.48f, 0.74f),  // libero

                        Pair(0.47f, 0.90f),  // arquero
                        Pair(0.65f, 0.74f)
                    )
                }

                jugadores.take(posiciones.size).forEachIndexed { index, jugador ->

                    val (x, y) = posiciones[index]

                    Box(
                        modifier = Modifier
                            .offset(
                                x = with(density) { (x * boxWidth).toDp() },
                                y = with(density) { (y * boxHeight).toDp() }
                            )
                            .size(width = 95.dp, height = 32.dp)
                            .clickable { jugadorSeleccionado = index }
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(3.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(if (jugador.nombre.isBlank()) "Jugador ${index + 1}" else jugador.nombre)
                                if (jugador.rut.isNotBlank()) Text(jugador.rut)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (modo != "futbol") {
                Button(
                    onClick = { navController.navigate("reservaForm") },
                    enabled = jugadores.any { it.nombre.isNotBlank() && it.rut.isNotBlank() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(55.dp)
                ) {
                    Text("Continuar a la Reserva")
                }
            } else {
                Button(
                    onClick = {
                        if (equipoActual == "A") carritoViewModel.cambiarEquipo()
                        else navController.navigate("reservaForm")
                    },
                    enabled = carritoViewModel.hayJugadoresCompletosEquipoActual(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(55.dp)
                ) {
                    Text(if (equipoActual == "A") "Ir a Equipo B" else "Continuar a la Reserva")
                }
            }
        }

        if (jugadorSeleccionado != null) {

            var nombre by remember { mutableStateOf(jugadores[jugadorSeleccionado!!].nombre) }
            var rut by remember { mutableStateOf(jugadores[jugadorSeleccionado!!].rut) }
            var rutError by remember { mutableStateOf(false) }

            AlertDialog(
                onDismissRequest = { jugadorSeleccionado = null },
                confirmButton = {
                    Button(onClick = {

                        if (!validarRut(rut)) {
                            rutError = true
                            return@Button
                        }

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
                            label = { Text("Nombre del jugador") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = rut,
                            onValueChange = {
                                rut = it
                                rutError = false
                            },
                            label = { Text("RUT") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = rutError
                        )

                        if (rutError)
                            Text(
                                text = "RUT inválido",
                                color = Color.Red
                            )
                    }
                }
            )
        }

    }

}
