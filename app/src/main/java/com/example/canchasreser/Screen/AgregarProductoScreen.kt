package com.example.canchasreser.Screen


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
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

    // ✅ Lanzador de galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imagenUri = uri
    }

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

            // ✅ BOTÓN PARA SUBIR IMAGEN
            Button(
                onClick = { galleryLauncher.launch("image/*") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Seleccionar imagen desde galería")
            }

            // ✅ PREVISUALIZACIÓN DE IMAGEN
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

            Button(
                onClick = {

                    if (nombre.isBlank() || precio.isBlank() || descripcion.isBlank() || imagenUri == null) {
                        return@Button
                    }

                    val nuevaCancha = hashMapOf(
                        "id" to UUID.randomUUID().toString(),
                        "nombre" to nombre,
                        "precioHora" to precio.toInt(),
                        "descripcion" to descripcion,
                        "imagen" to imagenUri.toString()
                    )

                    db.collection("canchas")
                        .add(nuevaCancha)
                        .addOnSuccessListener {
                            navController.popBackStack()
                        }

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar Cancha")
            }
        }
    }
}
