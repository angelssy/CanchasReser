package com.example.canchasreser.Screen


import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
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
import java.util.*
import androidx.compose.foundation.shape.RoundedCornerShape

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaFormScreen(navController: NavController, carritoViewModel: CarritoViewModel) {

    var nombre by remember { mutableStateOf("") }
    var invitados by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }

    var nombreError by remember { mutableStateOf(false) }
    var invitadosError by remember { mutableStateOf(false) }
    var fechaError by remember { mutableStateOf(false) }
    var horaError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            fecha = "$day/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Fecha mínima Octubre 2025
    val minCalendar = Calendar.getInstance()
    minCalendar.set(2025, Calendar.OCTOBER, 27)
    datePickerDialog.datePicker.minDate = minCalendar.timeInMillis

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Reserva de Cancha", color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0A6E2F)
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .background(Color(0xFFE8F5E9)),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            // Nombre responsable
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del responsable") },
                modifier = Modifier.fillMaxWidth(),
                isError = nombreError
            )
            if (nombreError) ErrorText("Debe ingresar un nombre válido")

            // Invitados
            OutlinedTextField(
                value = invitados,
                onValueChange = { invitados = it },
                label = { Text("Lista de invitados") },
                modifier = Modifier.fillMaxWidth(),
                isError = invitadosError
            )
            if (invitadosError) ErrorText("Debe ingresar invitados")

            // Fecha
            OutlinedTextField(
                value = fecha,
                onValueChange = {},
                label = { Text("Fecha de reserva") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() },
                enabled = false,
                readOnly = true,
                isError = fechaError
            )
            if (fechaError) ErrorText("Debe seleccionar una fecha")

            // Hora selector
            HoraSelector(
                hora = hora,
                onHoraSelected = { hora = it }
            )
            if (horaError) ErrorText("Debe seleccionar una hora")

            // Número de tarjeta
            OutlinedTextField(
                value = cardNumber,
                onValueChange = {
                    cardNumber = it.filter { c -> c.isDigit() }
                },
                label = { Text("Número de tarjeta") },
                placeholder = { Text("Ej: 1234567890123") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Botón confirmar
            Button(
                onClick = {

                    nombreError = nombre.isBlank()
                    invitadosError = invitados.isBlank()
                    fechaError = fecha.isBlank()
                    horaError = hora.isBlank()

                    val tarjetaInvalida = cardNumber.isBlank() || cardNumber.length > 13

                    // Resultado
                    if (tarjetaInvalida) {
                        navController.navigate("compraRechazada/${"Número de tarjeta inválido"}")
                        return@Button
                    }

                    if (!(nombreError || invitadosError || fechaError || horaError)) {
                        carritoViewModel.vaciarCarrito()
                        navController.navigate("compraExitosa")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0A6E2F)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Confirmar Reserva", color = Color.White)
            }
        }
    }
}

@Composable
fun HoraSelector(hora: String, onHoraSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val horas = (10..22).map { String.format("%02d:00", it) }

    Box {
        OutlinedTextField(
            value = hora,
            onValueChange = {},
            label = { Text("Hora de reserva") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            readOnly = true,
            enabled = false
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            horas.forEach { h ->
                DropdownMenuItem(
                    text = { Text(h) },
                    onClick = {
                        onHoraSelected(h)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun ErrorText(msg: String) {
    Text(
        text = msg,
        color = MaterialTheme.colorScheme.error,
    )
}
