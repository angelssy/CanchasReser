package com.example.canchasreser.Screen

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.canchasreser.viewmodel.CarritoViewModel
import com.example.canchasreser.model.ReservaData
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaFormScreen(navController: NavController, carritoViewModel: CarritoViewModel) {

    var nombreResponsable by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }

    var nombreError by remember { mutableStateOf(false) }
    var fechaError by remember { mutableStateOf(false) }
    var horaError by remember { mutableStateOf(false) }
    var jugadoresError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val jugadores by carritoViewModel.jugadores.collectAsState()
    val canchaReservada = carritoViewModel.items.lastOrNull()?.cancha

    val playerNames = remember { mutableStateListOf<String>() }

    LaunchedEffect(jugadores.size) {
        val size = jugadores.size
        while (playerNames.size < size) playerNames.add("")
        while (playerNames.size > size) playerNames.removeLast()
    }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day -> fecha = "$day/${month + 1}/$year" },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Reserva de Cancha", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF0A6E2F))
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .background(Color(0xFFE8F5E9))
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            item {
                ClimaCard(apiKey = "c734635bd206fc577f2be5215aa64228")
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                OutlinedTextField(
                    value = nombreResponsable,
                    onValueChange = { nombreResponsable = it },
                    label = { Text("Nombre del responsable") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = nombreError,
                    shape = RoundedCornerShape(10.dp)
                )
                if (nombreError) Text("Debes ingresar un nombre válido", color = Color.Red)
            }

            item {
                Text("Nombres de los jugadores:", style = MaterialTheme.typography.titleMedium)
            }

            itemsIndexed(playerNames) { index, value ->
                OutlinedTextField(
                    value = value,
                    onValueChange = { playerNames[index] = it },
                    singleLine = true,
                    label = { Text("Jugador ${index + 1}") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            item {
                if (jugadoresError) Text("Debes completar todos los nombres", color = Color.Red)
            }

            item {
                OutlinedTextField(
                    value = fecha,
                    onValueChange = {},
                    label = { Text("Fecha de reserva") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { datePickerDialog.show() },
                    enabled = false,
                    readOnly = true,
                    isError = fechaError,
                    shape = RoundedCornerShape(10.dp)
                )
            }

            item {
                HoraSelector(
                    hora = hora,
                    onHoraSelected = { hora = it }
                )
                if (horaError) Text("Debes seleccionar una hora", color = Color.Red)
            }

            item {
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { cardNumber = it.filter { c -> c.isDigit() } },
                    label = { Text("Número de tarjeta") },
                    placeholder = { Text("Ej: 1234567890123") },
                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            item {
                Button(
                    onClick = {
                        nombreError = nombreResponsable.isBlank()
                        fechaError = fecha.isBlank()
                        horaError = hora.isBlank()
                        jugadoresError = playerNames.any { it.isBlank() }

                        val tarjetaInvalida = cardNumber.length !in 1..13
                        if (tarjetaInvalida) {
                            navController.navigate("compraRechazada/${"Número de tarjeta inválido"}")
                            return@Button
                        }

                        if (!(nombreError || fechaError || horaError || jugadoresError)) {

                            val resumen = ReservaData(
                                nombreResponsable = nombreResponsable,
                                cantidadJugadores = jugadores.size,
                                fecha = fecha,
                                hora = hora,
                                canchaNombre = canchaReservada?.nombre ?: "Sin cancha"
                            )

                            val json = java.net.URLEncoder.encode(
                                Json.encodeToString(resumen),
                                "UTF-8"
                            )

                            carritoViewModel.vaciarJugadores()
                            carritoViewModel.vaciarCarrito()

                            navController.navigate("compraExitosa/$json")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0A6E2F)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Confirmar Reserva", color = Color.White)
                }
            }
        }
    }
}