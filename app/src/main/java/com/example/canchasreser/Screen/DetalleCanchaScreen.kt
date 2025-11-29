package com.example.canchasreser.Screen

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
import com.example.canchasreser.viewmodel.CanchasViewModel
import com.example.canchasreser.viewmodel.CarritoViewModel
import com.example.canchasreser.Utils.formatPrecio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCanchaScreen(
    canchaId: Int,
    viewModel: CanchasViewModel = viewModel(),
    carritoViewModel: CarritoViewModel = viewModel(),
    navController: androidx.navigation.NavController
) {

    val cancha = viewModel.canchas.value.find { it.id == canchaId }


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

                val painter = rememberAsyncImagePainter(
                    model = "https://via.placeholder.com/600"
                )

                Image(
                    painter = painter,
                    contentDescription = c.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )

                Text(c.nombre, style = MaterialTheme.typography.titleLarge)
                Text("Precio por hora: ${formatPrecio(c.precioHora)}",
                    style = MaterialTheme.typography.titleMedium)

                c.descripcion?.let { Text(it, style = MaterialTheme.typography.bodyMedium) }
                c.tipoSuperficie?.let { Text("Superficie: $it") }
                c.dimensiones?.let { Text("Dimensiones: $it") }
                c.medidas?.let { Text("Medidas: $it") }
                c.jugadores?.let { Text("Jugadores: $it") }
                c.ubicacion?.let { Text("Ubicación: $it") }

                Button(
                    onClick = {
                        carritoViewModel.agregarAlCarrito(c)
                        navController.navigate("carrito")
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