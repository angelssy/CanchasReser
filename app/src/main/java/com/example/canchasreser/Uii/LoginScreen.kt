package com.example.canchasreser.Uii


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import com.example.canchasreser.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Inicio de Sesión", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        OutlinedTextField(value = password, onValueChange = { password = it }, label = { Text("Contraseña") })

        Spacer(modifier = Modifier.height(10.dp))

        Button(onClick = {
            if (viewModel.login(email, password)) {
                navController.navigate("catalogo") // Navegar al catálogo si el login es exitoso
            }
        }) {
            Text("Entrar")
        }

        Text(viewModel.mensaje.value, modifier = Modifier.padding(top = 10.dp))

        TextButton(onClick = { navController.navigate("register") }) {
            Text("¿No tienes cuenta? Regístrate")
        }
    }
}
