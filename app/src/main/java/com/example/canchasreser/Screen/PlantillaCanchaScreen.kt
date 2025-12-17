package com.example.canchasreser.Screen




import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController   // ✅ ESTE ERA EL ERROR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlantillaCanchaScreen(
    navController: NavController
) {

    // ✅ 22 jugadores con nombre + rut
    val jugadores = remember {
        mutableStateListOf(*Array(22) { Pair("", "") })
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Formación de Jugadores") })
        },
        bottomBar = {
            Button(
                onClick = { navController.navigate("reservaForm") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Confirmar Jugadores")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .background(Color(0xFF1B5E20)) // Verde cancha
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {

            repeat(4) { fila ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(6) { columna ->
                        val index = fila * 6 + columna
                        if (index < 22) {
                            JugadorItem(
                                nombre = jugadores[index].first,
                                rut = jugadores[index].second
                            ) { nuevoNombre, nuevoRut ->
                                jugadores[index] = Pair(nuevoNombre, nuevoRut)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JugadorItem(
    nombre: String,
    rut: String,
    onChange: (String, String) -> Unit
) {
    Card(
        modifier = Modifier
            .size(70.dp)
            .padding(4.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = nombre,
                onValueChange = { onChange(it, rut) },
                placeholder = { Text("Nom") },
                singleLine = true,
                modifier = Modifier.height(30.dp),
                textStyle = MaterialTheme.typography.bodySmall
            )

            OutlinedTextField(
                value = rut,
                onValueChange = { onChange(nombre, it) },
                placeholder = { Text("RUT") },
                singleLine = true,
                modifier = Modifier.height(30.dp),
                textStyle = MaterialTheme.typography.bodySmall
            )
        }
    }
}
