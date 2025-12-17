package com.example.canchasreser.Screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.canchasreser.model.Cancha
import com.example.canchasreser.model.Reserva
import com.example.canchasreser.viewmodel.AuthViewModel
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackOfficeScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {

    val db = FirebaseFirestore.getInstance()

    var reservas by remember { mutableStateOf<List<Reserva>>(emptyList()) }
    var canchas by remember { mutableStateOf<List<Cancha>>(emptyList()) }

    // 🔄 ESCUCHAR RESERVAS
    LaunchedEffect(Unit) {
        db.collection("reservas")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    reservas = snapshot.toObjects(Reserva::class.java)
                }
            }
    }

    // 🔄 ESCUCHAR CANCHAS ADMIN
    LaunchedEffect(Unit) {
        db.collection("canchas")
            .addSnapshotListener { snapshot, _ ->
                if (snapshot != null) {
                    canchas = snapshot.documents.mapNotNull { doc ->
                        Cancha(
                            id = doc.id,
                            nombre = doc.getString("nombre") ?: "",
                            tipoSuperficie = "No especificado",
                            dimensiones = "-",
                            medidas = "-",
                            jugadores = "-",
                            descripcion = doc.getString("descripcion"),
                            ubicacion = "Chile",
                            precioHora = doc.getLong("precioHora")?.toDouble() ?: 0.0,
                            imagen = doc.getString("imagen") ?: ""
                        )
                    }
                }
            }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Panel de Administración") },
                actions = {
                    TextButton(
                        onClick = {
                            authViewModel.logout()
                            navController.navigate("login") {
                                popUpTo("backoffice") { inclusive = true }
                            }
                        }
                    ) {
                        Text("Cerrar sesión", color = Color.White)
                    }
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // =========================
            // 📋 RESERVAS
            // =========================
            item {
                Text(
                    "Reservas registradas",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            if (reservas.isEmpty()) {
                item {
                    Text("No hay reservas registradas")
                }
            } else {
                items(reservas) { r ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Responsable: ${r.responsable}")
                            Text("Jugadores: ${r.jugadores.joinToString()}")
                            Text("Fecha: ${r.fecha}")
                            Text("Horario: ${r.horaInicio} - ${r.horaTermino}")
                            Text("Cancha: ${r.canchaNombre}")
                            Text("Total: $${r.total}")
                        }
                    }
                }
            }

            // =========================
            // 🏟️ CANCHAS ADMIN
            // =========================
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    "Canchas creadas por el admin",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            item {
                Button(
                    onClick = { navController.navigate("agregarProducto") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar Nueva Cancha")
                }
            }

            if (canchas.isEmpty()) {
                item {
                    Text("No hay canchas creadas por el admin")
                }
            } else {
                items(canchas) { cancha ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(cancha.nombre, style = MaterialTheme.typography.titleMedium)
                                Text("Precio: $${cancha.precioHora}")
                            }

                            IconButton(
                                onClick = {
                                    db.collection("canchas")
                                        .document(cancha.id)
                                        .delete()
                                }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = Color.Red
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
