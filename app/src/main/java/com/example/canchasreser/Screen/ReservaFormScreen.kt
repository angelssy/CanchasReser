package com.example.canchasreser.Screen

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.canchasreser.viewmodel.CarritoViewModel
import com.example.canchasreser.viewmodel.ReservaViewModel
import com.example.canchasreser.model.Reserva
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.*
import java.net.URLEncoder
import androidx.compose.foundation.text.KeyboardOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaFormScreen(
    navController: NavController,
    carritoViewModel: CarritoViewModel,
    reservaViewModel: ReservaViewModel = viewModel()
) {

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

    // Jugadores desde el ViewModel
    val jugadores by carritoViewModel.jugadores.collectAsState()
    val playerNames = remember { mutableStateListOf<String>() }

    LaunchedEffect(jugadores.size) {
        val count = jugadores.size
        while (playerNames.size < count) playerNames.add("")
        while (playerNames.size > count) playerNames.removeLast()
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
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Atrás",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0A6E2F)
                )
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .background(Color(0xFFE8F5E9)),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // CLIMA
            item {
                ClimaCard(apiKey = "c734635bd206fc577f2be5215aa64228")
                Spacer(modifier = Modifier.height(12.dp))
            }

            // NOMBRE RESPONSABLE
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

            // JUGADORES
            item {
                Text("Nombres de los jugadores:", style = MaterialTheme.typography.titleMedium)
            }

            itemsIndexed(playerNames) { index, value ->
                OutlinedTextField(
                    value = value,
                    onValueChange = { playerNames[index] = it },
                    label = { Text("Jugador ${index + 1}") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp)
                )
            }

            item {
                if (jugadoresError) Text("Debes ingresar todos los nombres.", color = Color.Red)
            }

            // FECHA
            item {
                OutlinedTextField(
                    value = fecha,
                    onValueChange = {},
                    label = { Text("Fecha de reserva") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { datePickerDialog.show() },
                    readOnly = true,
                    enabled = false,
                    isError = fechaError,
                    shape = RoundedCornerShape(10.dp)
                )
            }

            // HORA
            item {
                HoraSelector(hora = hora, onHoraSelected = { hora = it })
                if (horaError) Text("Debes seleccionar una hora", color = Color.Red)
            }

            // TARJETA
            item {
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { cardNumber = it.filter { c -> c.isDigit() } },
                    label = { Text("Número de tarjeta") },
                    placeholder = { Text("Ej: 1234567890123") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            // BOTÓN CONFIRMAR
            item {
                Button(
                    onClick = {
                        // Validaciones
                        nombreError = nombreResponsable.isBlank()
                        fechaError = fecha.isBlank()
                        horaError = hora.isBlank()
                        jugadoresError = playerNames.any { it.isBlank() }

                        if (nombreError || fechaError || horaError || jugadoresError) {
                            return@Button
                        }

                        // Construir objeto Reserva
                        val reserva = Reserva(
                            responsable = nombreResponsable,
                            jugadores = playerNames.toList(),
                            fecha = fecha,
                            hora = hora,
                            canchaId = carritoViewModel.items.firstOrNull()?.cancha?.id ?: 0,
                            canchaNombre = carritoViewModel.items.firstOrNull()?.cancha?.nombre ?: "",
                            total = carritoViewModel.total()
                        )

                        // Guardar en Firebase (best effort, sin esperar)
                        reservaViewModel.guardarReserva(reserva)

                        // Pasar la reserva a la pantalla de éxito
                        val json = URLEncoder.encode(Json.encodeToString(reserva), "UTF-8")

                        carritoViewModel.vaciarJugadores()

                        navController.navigate("compraExitosa/$json")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(Color(0xFF0A6E2F)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Confirmar Reserva", color = Color.White)
                }
            }
        }
    }
}