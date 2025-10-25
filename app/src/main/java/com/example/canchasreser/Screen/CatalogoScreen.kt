package com.example.canchasreser.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.canchasreser.viewmodel.CatalogoViewModel
import com.example.canchasreser.model.Cancha
import com.example.canchasreser.Utils.formatPrecio

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(navController: NavController, viewModel: CatalogoViewModel) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.cargarProductos(context)
    }

    val canchas by viewModel.productos.collectAsState()
    val loading by viewModel.loading.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Catálogo de Canchas") }
            )
        },
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón para agregar producto
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate("agregarProducto") }
                ) {
                    Text("Agregar Producto")
                }

                // Botón para Back Office
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate("backOffice") }
                ) {
                    Text("Back Office")
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            if (loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = canchas, key = { it.id }) { cancha ->
                        CanchaCard(cancha = cancha) {
                            navController.navigate("detalle/${cancha.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CanchaCard(cancha: Cancha, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(4.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val painter = rememberAsyncImagePainter(
                model = cancha.imagen?.let {
                    if (it.startsWith("http")) it else "android.resource://com.example.canchasreser/drawable/$it"
                }
            )
            Image(
                painter = painter,
                contentDescription = cancha.nombre,
                modifier = Modifier.size(80.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(text = cancha.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = formatPrecio(cancha.precioHora), style = MaterialTheme.typography.bodyMedium)
                cancha.descripcion?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = it, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                }
            }
        }
    }
}
