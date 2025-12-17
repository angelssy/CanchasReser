package com.example.canchasreser.Screen

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import java.net.URLEncoder
import java.util.Calendar
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
    var horaInicio by remember { mutableStateOf("") }
    var horaTermino by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }

    var nombreError by remember { mutableStateOf(false) }
    var fechaError by remember { mutableStateOf(false) }
    var horaInicioError by remember { mutableStateOf(false) }
    var horaTerminoError by remember { mutableStateOf(false) }

    var horaRangoError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val jugadores by carritoViewModel.jugadores.collectAsState(initial = emptyList())

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            fecha = "$day/${month + 1}/$year"
        },
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

            item {
                ClimaCard(apiKey = "c734635bd206fc577f2be5215aa64228")
                Spacer(modifier = Modifier.height(12.dp))
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
            }

            // 📅 FECHA (CALENDARIO FUNCIONAL)
            item {
                OutlinedTextField(
                    value = fecha,
                    onValueChange = {},
                    label = { Text("Fecha de reserva") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { datePickerDialog.show() },
                    readOnly = true,
                    isError = fechaError,
                    shape = RoundedCornerShape(10.dp)
                )
            }

            item {
                Text("Hora Inicio")
                HoraSelector(hora = horaInicio) {
                    horaInicio = it
                    horaRangoError = null
                }
                if (horaInicioError) {
                    Text("Selecciona hora de inicio", color = Color.Red)
                }
            }

            item {
                Text("Hora Término")
                HoraSelector(hora = horaTermino) {
                    horaTermino = it
                    horaRangoError = null
                }
                if (horaTerminoError) {
                    Text("Selecciona hora de término", color = Color.Red)
                }
            }

            item {
                horaRangoError?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            item {
                OutlinedTextField(
                    value = cardNumber,
                    onValueChange = { cardNumber = it.filter { c -> c.isDigit() } },
                    label = { Text("Número de tarjeta") },
                    keyboardOptions = KeyboardOptions(
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
                        horaInicioError = horaInicio.isBlank()
                        horaTerminoError = horaTermino.isBlank()
                        horaRangoError = null

                        if (nombreError || fechaError || horaInicioError || horaTerminoError) return@Button

                        val inicio = horaToInt(horaInicio)
                        val termino = horaToInt(horaTermino)

                        if (termino <= inicio) {
                            horaRangoError =
                                "La hora de término debe ser mayor a la de inicio"
                            return@Button
                        }

                        val reserva = Reserva(
                            responsable = nombreResponsable,
                            jugadores = jugadores.map { it.nombre }, // ✅
                            fecha = fecha,
                            horaInicio = horaInicio,
                            horaTermino = horaTermino,
                            canchaId = 1,
                            canchaNombre = "Cancha Fútbol",
                            total = 15000.0
                        )

                        reservaViewModel.guardarReserva(reserva)

                        val json =
                            URLEncoder.encode(Json.encodeToString(reserva), "UTF-8")

                        navController.navigate("compraExitosa/$json")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0A6E2F)
                    )
                ) {
                    Text("Confirmar Reserva", color = Color.White)
                }
            }
        }
    }
}

// ✅ FUNCIÓN AUXILIAR
fun horaToInt(hora: String): Int {
    return hora.substringBefore(":").toIntOrNull() ?: -1
}
