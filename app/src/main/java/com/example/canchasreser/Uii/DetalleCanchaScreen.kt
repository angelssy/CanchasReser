package com.example.canchasreser.Uii

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.canchasreser.ViewModel.CatalogoViewModel

@OptIn(ExperimentalMaterial3Api::class)  // Aceptamos la API experimental
@Composable
fun DetalleCanchaScreen(canchaId: Int, viewModel: CatalogoViewModel = viewModel()) {
    // Obtener los detalles de la cancha por su ID desde el ViewModel
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
                // Cargar la imagen usando rememberAsyncImagePainter
                val painter = rememberAsyncImagePainter(c.imagen)
                Image(
                    painter = painter,
                    contentDescription = c.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )

                Text(text = c.nombre, style = MaterialTheme.typography.titleLarge)
                Text(text = "Precio por hora: $${c.precioHora}", style = MaterialTheme.typography.titleMedium)

                c.descripcion?.let { desc ->
                    Text(text = desc, style = MaterialTheme.typography.bodyMedium)
                }

                c.tipoSuperficie?.let { superficie ->
                    Text(text = "Superficie: $superficie", style = MaterialTheme.typography.bodyMedium)
                }

                c.dimensiones?.let { dim ->
                    Text(text = "Dimensiones: $dim", style = MaterialTheme.typography.bodyMedium)
                }

                c.medidas?.let { med ->
                    Text(text = "Medidas: $med", style = MaterialTheme.typography.bodyMedium)
                }

                c.jugadores?.let { jug ->
                    Text(text = "Jugadores: $jug", style = MaterialTheme.typography.bodyMedium)
                }

                c.ubicacion?.let { ubi ->
                    Text(text = "Ubicación: $ubi", style = MaterialTheme.typography.bodyMedium)
                }

                Button(
                    onClick = {
                        // Aquí puedes agregar lógica para reserva o carrito en el futuro
                    },
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
