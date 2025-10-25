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
import com.example.canchasreser.viewmodel.CatalogoViewModel
import com.example.canchasreser.viewmodel.CarritoViewModel
import com.example.canchasreser.Utils.formatPrecio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCanchaScreen(
    canchaId: Int,
    viewModel: CatalogoViewModel = viewModel(),
    carritoViewModel: CarritoViewModel = viewModel(),
    navController: androidx.navigation.NavController
) {

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

                val painter = rememberAsyncImagePainter(
                    model = c.imagen?.let {
                        if (it.startsWith("http")) it
                        else "android.resource://com.example.canchasreser/drawable/$it"
                    }
                )
                Image(
                    painter = painter,
                    contentDescription = c.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp),
                    contentScale = ContentScale.Crop
                )

                Text(text = c.nombre, style = MaterialTheme.typography.titleLarge)


                Text(
                    text = "Precio por hora: ${formatPrecio(c.precioHora)}",
                    style = MaterialTheme.typography.titleMedium
                )

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
