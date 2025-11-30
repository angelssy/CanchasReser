package com.example.canchasreser.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarProductoScreen(navController: NavController) {

    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagen by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Agregar Nueva Cancha") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre de la cancha") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(value = precio, onValueChange = { precio = it }, label = { Text("Precio por hora") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth())

            OutlinedTextField(value = imagen, onValueChange = { imagen = it }, label = { Text("URL de imagen") }, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Cancha")
            }
        }
    }
}