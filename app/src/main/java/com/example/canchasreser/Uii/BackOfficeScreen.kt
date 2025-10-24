package com.example.canchasreser.Uii


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.canchasreser.model.Cancha
@OptIn(ExperimentalMaterial3Api::class) // <-- Aquí indicamos que usamos API experimental
@Composable
fun BackOfficeScreen(navController: NavController, canchas: List<Cancha>) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Back Office - Gestión de Canchas") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "Pantalla de administración visual (NO FUNCIONAL)",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(canchas) { cancha ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("ID: ${cancha.id}", style = MaterialTheme.typography.bodySmall)
                            Text("Nombre: ${cancha.nombre}", style = MaterialTheme.typography.bodyMedium)
                            Text("Precio por hora: ${cancha.precioHora}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("catalogo") }) {
                Text("Volver al Catálogo")
            }
        }
    }
}
