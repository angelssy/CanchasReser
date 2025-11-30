package com.example.canchasreser.Screen


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackOfficeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Panel de Administración") })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text("Bienvenido, Administrador", style = MaterialTheme.typography.titleLarge)

            Button(
                onClick = { navController.navigate("agregarProducto") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar Nueva Cancha")
            }

            Button(
                onClick = { navController.navigate("catalogo") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Ver Catálogo Completo")
            }

        }
    }
}
