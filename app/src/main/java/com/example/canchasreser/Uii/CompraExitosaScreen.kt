package com.example.canchasreser.Uii



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CompraExitosaScreen(navController: NavController) {
    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("¡Compra Exitosa!", style = MaterialTheme.typography.headlineMedium)
                Text("Gracias por tu reserva. Tu pedido ha sido confirmado.")

                Button(onClick = { navController.navigate("catalogo") }) {
                    Text("Seguir Comprando")
                }

                Button(onClick = { navController.navigate("inicio") }) {
                    Text("Volver al Inicio")
                }
            }
        }
    }
}
