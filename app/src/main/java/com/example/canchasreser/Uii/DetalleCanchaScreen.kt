package com.example.canchasreser.Uii

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.canchasreser.ViewModel.CatalogoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCanchaScreen(canchaId: Int, viewModel: CatalogoViewModel) {
    val cancha = viewModel.buscarCanchaPorId(canchaId)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(cancha?.nombre ?: "Detalle de Cancha") }
            )
        }
    ) { padding ->
        cancha?.let { c ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val painter = rememberAsyncImagePainter(c.imagen)
                Image(
                    painter = painter,
                    contentDescription = c.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

                Text(text = c.nombre, style = MaterialTheme.typography.titleLarge)
                Text(text = "Precio por hora: $${c.precioHora}", style = MaterialTheme.typography.titleMedium)

                c.descripcion?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }
                c.tipoSuperficie?.let { Text(text = "Superficie: $it", style = MaterialTheme.typography.bodyMedium) }
                c.dimensiones?.let { Text(text = "Dimensiones: $it", style = MaterialTheme.typography.bodyMedium) }
                c.medidas?.let { Text(text = "Medidas: $it", style = MaterialTheme.typography.bodyMedium) }
                c.jugadores?.let { Text(text = "Jugadores: $it", style = MaterialTheme.typography.bodyMedium) }
                c.ubicacion?.let { Text(text = "Ubicación: $it", style = MaterialTheme.typography.bodyMedium) }

                Button(
                    onClick = { /* Lógica futura */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Reservar cancha")
                }
            }
        } ?: Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Cancha no encontrada")
        }
    }
}
