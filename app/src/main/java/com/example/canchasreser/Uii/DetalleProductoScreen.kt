package com.example.canchasreser.Uii

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.canchasreser.ViewModel.CatalogoViewModel

@OptIn(ExperimentalMaterial3Api::class) // ✅ Para usar CenterAlignedTopAppBar
@Composable
fun DetalleProductoScreen(productoId: Int, viewModel: CatalogoViewModel) {
    val producto = remember { viewModel.buscarProductoPorId(productoId) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(producto?.nombre ?: "Detalle") }
            )
        }
    ) { padding ->
        producto?.let { p ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val painter = rememberAsyncImagePainter(p.imagen)
                Image(
                    painter = painter,
                    contentDescription = p.nombre,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )
                Text(text = p.nombre, style = MaterialTheme.typography.titleLarge)
                Text(text = "$${p.precio}", style = MaterialTheme.typography.titleMedium)
                Text(text = p.descripcion ?: "", style = MaterialTheme.typography.bodyMedium)
                Button(
                    onClick = { /* TODO: agregar al carrito */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar al carrito")
                }
            }
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Producto no encontrado")
        }
    }
}