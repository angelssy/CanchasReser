package com.example.canchasreser.Uii

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.canchasreser.viewmodel.CarritoViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservaFormScreen(navController: NavController, carritoViewModel: CarritoViewModel) {
    var nombre by remember { mutableStateOf("") }
    var invitados by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }

    // Estados de error
    var nombreError by remember { mutableStateOf(false) }
    var invitadosError by remember { mutableStateOf(false) }
    var fechaError by remember { mutableStateOf(false) }
    var horaError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            fecha = "${dayOfMonth}/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val minCalendar = Calendar.getInstance()
    minCalendar.set(2025, Calendar.OCTOBER, 27)
    datePickerDialog.datePicker.minDate = minCalendar.timeInMillis

    Scaffold(
        topBar = { TopAppBar(title = { Text("Formulario de Reserva") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    nombreError = false
                },
                label = { Text("Nombre del responsable") },
                modifier = Modifier.fillMaxWidth(),
                isError = nombreError
            )
            if (nombreError) {
                Text(
                    text = if (nombre.isBlank()) "Este campo es obligatorio" else "Solo se permiten letras",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Invitados
            OutlinedTextField(
                value = invitados,
                onValueChange = {
                    invitados = it
                    invitadosError = false
                },
                label = { Text("Lista de invitados") },
                modifier = Modifier.fillMaxWidth(),
                isError = invitadosError
            )
            if (invitadosError) {
                Text(
                    text = "Este campo es obligatorio",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Fecha
            OutlinedTextField(
                value = fecha,
                onValueChange = { },
                label = { Text("Fecha de reserva") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() },
                enabled = false,
                readOnly = true,
                isError = fechaError
            )
            if (fechaError) {
                Text(
                    text = "Debe seleccionar una fecha",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Hora
            HoraSelector(hora = hora, onHoraSelected = {
                hora = it
                horaError = false
            })
            if (horaError) {
                Text(
                    text = "Debe seleccionar una hora",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Número de tarjeta
            OutlinedTextField(
                value = cardNumber,
                onValueChange = { input ->
                    val digitsOnly = input.filter { it.isDigit() }
                    cardNumber = digitsOnly

                    // Redirigir a compra rechazada si excede 13 dígitos
                    if (digitsOnly.length > 13) {
                        navController.navigate("compraRechazada/${"Número de tarjeta excede 13 dígitos"}")
                    }
                },
                label = { Text("Número de tarjeta") },
                placeholder = { Text("e.g. 1234567890123") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón Confirmar
            Button(
                onClick = {
                    nombreError = nombre.isBlank() || !nombre.matches(Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$"))
                    invitadosError = invitados.isBlank()
                    fechaError = fecha.isBlank()
                    horaError = hora.isBlank()
                    val cardError = cardNumber.isBlank() || cardNumber.length > 13

                    if (cardError) {
                        navController.navigate("compraRechazada/${"Número de tarjeta inválido"}")
                    } else if (!(nombreError || invitadosError || fechaError || horaError)) {
                        carritoViewModel.vaciarCarrito()
                        navController.navigate("compraExitosa")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Reserva")
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
            onValueChange = { },
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
