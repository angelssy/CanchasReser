package com.example.canchasreser.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.canchasreser.viewmodel.ClimaViewModel

@Composable
fun ClimaCard(
    lat: Double = -33.4489,        // Santiago por defecto
    lon: Double = -70.6693,
    apiKey: String = "c734635bd206fc577f2be5215aa64228",
    viewModel: ClimaViewModel = viewModel()
) {
    val climaState by viewModel.clima.collectAsState()

    // Cargar clima al iniciar
    LaunchedEffect(Unit) {
        viewModel.cargarClima(lat, lon, apiKey)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFB2DFDB) // verde suave
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (climaState == null) {
                Text("Cargando clima...", style = MaterialTheme.typography.titleMedium)
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Clima en ${climaState!!.name}",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "${climaState!!.main.temp}°C — ${climaState!!.weather[0].description}",
                        style = MaterialTheme.typography.bodyLarge
                    )

                    Text(
                        text = "Humedad: ${climaState!!.main.humidity}%",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
