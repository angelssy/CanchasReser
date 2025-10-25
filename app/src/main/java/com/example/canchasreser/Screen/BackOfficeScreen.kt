package com.example.canchasreser.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.canchasreser.model.Cancha
import com.example.canchasreser.viewmodel.CatalogoViewModel
import com.example.canchasreser.Utils.formatPrecio
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackOfficeScreen(navController: NavController, viewModel: CatalogoViewModel) {
    val canchas by viewModel.productos.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Back Office - Gestión de Canchas") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(canchas) { cancha ->
                BackOfficeItem(cancha)
            }
        }
    }
}

@Composable
fun BackOfficeItem(cancha: Cancha) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(text = "Nombre: ${cancha.nombre}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Precio/Hora: ${formatPrecio(cancha.precioHora)}")
            Text(text = "Ubicación: ${cancha.ubicacion ?: "No especificada"}")
            Text(text = "Tipo: ${cancha.tipoSuperficie ?: "No listado"}")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { }) {
                Text("Editar Cancha")
            }
        }
    }
}
