package com.example.canchasreser.Screen


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.canchasreser.model.Reserva
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BackOfficeScreen(navController: NavController) {

    val db = FirebaseFirestore.getInstance()
    var reservas by remember { mutableStateOf(listOf<Reserva>()) }

    LaunchedEffect(Unit) {
        db.collection("reservas").addSnapshotListener { snapshot, _ ->
            if (snapshot != null) {
                reservas = snapshot.toObjects(Reserva::class.java)
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Panel de Administración") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text("Reservas registradas", style = MaterialTheme.typography.titleLarge)

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {

                items(reservas) { r ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Responsable: ${r.responsable}")
                            Text("Jugadores: ${r.jugadores.joinToString()}")
                            Text("Fecha: ${r.fecha}")
                            Text("Hora: ${r.hora}")
                            Text("Cancha: ${r.canchaNombre}")
                            Text("Total: $${r.total}")
                        }
                    }
                }
            }

            Button(
                onClick = { navController.navigate("agregarProducto") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar Nueva Cancha")
            }
        }
    }
}