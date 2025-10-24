package com.example.canchasreser.Uii

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
    var pago by remember { mutableStateOf("") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    // -------------------- DatePicker --------------------
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            fecha = "${dayOfMonth}/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Limitar fecha mínima a octubre 2025
    val minCalendar = Calendar.getInstance()
    minCalendar.set(2025, Calendar.OCTOBER, 1)
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
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del responsable") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = invitados,
                onValueChange = { invitados = it },
                label = { Text("Lista de invitados") },
                modifier = Modifier.fillMaxWidth()
            )

            // -------------------- Campo Fecha --------------------
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha de reserva") },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { datePickerDialog.show() },
                enabled = false,
                readOnly = true
            )

            // -------------------- Campo Hora --------------------
            HoraSelector(hora = hora, onHoraSelected = { hora = it })

            OutlinedTextField(
                value = pago,
                onValueChange = { pago = it },
                label = { Text("Pago") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Vaciar carrito y navegar a compra exitosa
                    carritoViewModel.vaciarCarrito()
                    navController.navigate("compraExitosa")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar Reserva")
            }
        }
    }
}

// -------------------- Selector de hora --------------------
@Composable
fun HoraSelector(hora: String, onHoraSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val horas = (10..22).map { String.format("%02d:00", it) } // horas de 10 a 22

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
