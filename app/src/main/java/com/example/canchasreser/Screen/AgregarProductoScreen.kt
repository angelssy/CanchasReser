package com.example.canchasreser.Screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarProductoScreen(navController: NavController) {

    val db = FirebaseFirestore.getInstance()

    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf<Uri?>(null) }

    // 👉 estado para el mensaje
    var mostrarDialogo by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri -> imagenUri = uri }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Agregar Nueva Cancha") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre de la cancha") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it.filter { c -> c.isDigit() } },
                label = { Text("Precio por hora (CLP)") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seleccionar imagen desde galería")
            }

            imagenUri?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 💾 GUARDAR
            Button(
                onClick = {
                    if (nombre.isBlank() || precio.isBlank() ||
                        descripcion.isBlank() || imagenUri == null
                    ) return@Button

                    val nuevaCancha = hashMapOf(
                        "nombre" to nombre,
                        "precioHora" to precio.toInt(),
                        "descripcion" to descripcion,
                        "imagen" to imagenUri.toString(),
                        "fechaCreacion" to Date()
                    )

                    db.collection("canchas")
                        .add(nuevaCancha)
                        .addOnSuccessListener {
                            mostrarDialogo = true // 👈 MOSTRAR MENSAJE
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Cancha")
            }

            OutlinedButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Regresar")
            }
        }
    }

    // ✅ DIÁLOGO DE CONFIRMACIÓN
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Éxito") },
            text = { Text("Cancha agregada correctamente") },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarDialogo = false
                        navController.popBackStack() // vuelve al BackOffice
                    }
                ) {
                    Text("Aceptar")
                }
            }
        )
    }
}
