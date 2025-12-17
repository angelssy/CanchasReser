package com.example.canchasreser.Screen


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.canchasreser.Utils.formatPrecio
import com.example.canchasreser.viewmodel.CanchasViewModel
import com.example.canchasreser.viewmodel.CarritoViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleCanchaScreen(
    canchaId: Int,
    viewModel: CanchasViewModel = viewModel(),
    carritoViewModel: CarritoViewModel = viewModel(),
    navController: NavController
) {

    val cancha = viewModel.canchas.value.find { it.id == canchaId }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(cancha?.nombre ?: "Detalle de Cancha") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        },

        bottomBar = {
            cancha?.let {
                Button(
                    onClick = {
                        // ✅ AHORA SOLO NAVEGAMOS A LA FORMACIÓN
                        navController.navigate("carrito")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(12.dp)
                ) {
                    Text("Reservar cancha")
                }
            }
        }
    ) { padding ->

        cancha?.let { c ->

            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                item {
                    Image(
                        painter = rememberAsyncImagePainter(c.imagen),
                        contentDescription = c.nombre,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                item { Text(c.nombre, style = MaterialTheme.typography.titleLarge) }
                item { Text("Precio por hora: ${formatPrecio(c.precioHora)}", style = MaterialTheme.typography.titleMedium) }

                item { c.descripcion?.let { Text(it) } }
                item { Text("Superficie: ${c.tipoSuperficie}") }
                item { Text("Dimensiones: ${c.dimensiones}") }
                item { Text("Medidas: ${c.medidas}") }
                item { Text("Jugadores: ${c.jugadores}") }
                item { Text("Ubicación: ${c.ubicacion}") }

                item { Spacer(modifier = Modifier.height(100.dp)) }
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
