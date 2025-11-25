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
import com.example.canchasreser.viewmodel.ClimaViewModel
import java.util.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults

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

    // Clima (opcional)
    val climaViewModel = remember { ClimaViewModel() }
    val climaState by climaViewModel.clima.collectAsState()
    LaunchedEffect(Unit) {
        // default: Santiago (-33.45, -70.66) — reemplaza apiKey por la tuya
        climaViewModel.cargarClima(lat = -33.45, lon = -70.66, apiKey = "c734635bd206fc577f2be5215aa64228")
    }

    // Observamos la cantidad de jugadores desde el ViewModel
    val jugadores by carritoViewModel.jugadores.collectAsState()


    // Local: lista de nombres para cada jugador (no persiste en ViewModel)
    val playerNames = remember { mutableStateListOf<String>() }

    // Mantener el tamaño de playerNames igual al número de jugadores
    LaunchedEffect(jugadores.size) {
        val target = jugadores.size
        while (playerNames.size < target) playerNames.add("")
        while (playerNames.size > target) playerNames.removeLast()
    }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            fecha = "$day/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // Fecha mínima ejemplo
    val minCalendar = Calendar.getInstance()
    minCalendar.set(2025, Calendar.OCTOBER, 27)
    datePickerDialog.datePicker.minDate = minCalendar.timeInMillis

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Reserva de Cancha", color = Color.White) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFF0A6E2F))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .background(Color(0xFFE8F5E9)),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // --- Sección clima (opcional) ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFD8F3DC)),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text("Clima actual:", style = MaterialTheme.typography.titleMedium)
                    if (climaState == null) {
                        Text("Cargando clima...", color = Color.Gray)
                    } else {
                        Text("Ciudad: ${climaState!!.name}")
                        Text("Condición: ${climaState!!.weather.firstOrNull()?.description ?: "-"}")
                        Text("Temperatura: ${climaState!!.main.temp}°C")
                    }
                }
            }

            // Nombre responsable
            OutlinedTextField(
                value = nombreResponsable,
                onValueChange = { nombreResponsable = it },
                label = { Text("Nombre del responsable") },
                modifier = Modifier.fillMaxWidth(),
                isError = nombreError,
                shape = RoundedCornerShape(8.dp)
            )
            if (nombreError) Text("Debe ingresar un nombre válido", color = MaterialTheme.colorScheme.error)

            // Campos dinámicos: un TextField por cada jugador
            Text("Nombres de los jugadores:", style = MaterialTheme.typography.titleMedium)
            if (playerNames.isEmpty()) {
                Text("No hay jugadores seleccionados.", color = Color.Gray)
            } else {
                playerNames.forEachIndexed { index, _ ->
                    OutlinedTextField(
                        value = playerNames[index],
                        onValueChange = { newVal -> playerNames[index] = newVal },
                        label = { Text("Jugador ${index + 1}") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
            if (jugadoresError) Text("Debes completar los nombres de todos los jugadores", color = MaterialTheme.colorScheme.error)

            // Fecha (disabled clickable)
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

            // Hora selector simple (usa HoraSelector de tu proyecto si tienes)
            HoraSelector(hora = hora, onHoraSelected = { hora = it })
            if (horaError) Text("Debes seleccionar una hora", color = MaterialTheme.colorScheme.error)

            // Número de tarjeta
            OutlinedTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it.filter { c -> c.isDigit() } },
                label = { Text("Número de tarjeta") },
                placeholder = { Text("Ej: 1234567890123") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Botón confirmar
            Button(
                onClick = {
                    // validaciones
                    nombreError = nombreResponsable.isBlank()
                    fechaError = fecha.isBlank()
                    horaError = hora.isBlank()
                    jugadoresError = playerNames.any { it.isBlank() } || playerNames.isEmpty()

                    val tarjetaInvalida = cardNumber.isBlank() || cardNumber.length > 13

                    if (tarjetaInvalida) {
                        navController.navigate("compraRechazada/${"Número de tarjeta inválido"}")
                        return@Button
                    }

                    if (!(nombreError || fechaError || horaError || jugadoresError)) {
                        // No guardamos nombres en VM por tu pedido.
                        // Limpiamos jugadores del ViewModel (ya se procesó la reserva).
                        carritoViewModel.vaciarJugadores()
                        navController.navigate("compraExitosa")
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

/**
 * HoraSelector simple: reutiliza la implementación que ya tenías
 */
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

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            horas.forEach { h ->
                DropdownMenuItem(text = { Text(h) }, onClick = {
                    onHoraSelected(h)
                    expanded = false
                })
            }
        }
    }
}