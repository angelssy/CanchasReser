package com.example.canchasreser.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.canchasreser.R
import com.example.canchasreser.viewmodel.AuthViewModel
import com.example.canchasreser.Utils.isValidEmail
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController, viewModel: AuthViewModel) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFF66BB6A)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(0.85f)) {

            Image(painter = painterResource(id = R.drawable.fff),
                contentDescription = null,
                modifier = Modifier.size(120.dp))

            Text("Canchas Reser", fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White)

            Spacer(modifier = Modifier.height(20.dp))

            OutlinedTextField(value = email,
                onValueChange = { email = it; mensajeError = null },
                label = { Text("Correo") },
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(value = password,
                onValueChange = { password = it; mensajeError = null },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier.fillMaxWidth().height(55.dp),
                onClick = {
                    when {
                        email.isBlank() || password.isBlank() -> {
                            mensajeError = "Completa los campos"
                        }
                        else -> {
                            viewModel.login(email.trim(), password.trim()) { success ->
                                if (success) {
                                    if (viewModel.esAdmin()) {
                                        navController.navigate("backoffice")
                                    } else {
                                        navController.navigate("inicio") {
                                            popUpTo("login") { inclusive = true }
                                        }
                                    }
                                } else {
                                    mensajeError = viewModel.mensaje.value
                                }
                            }
                        }
                    }
                }
            ) {
                Text("Iniciar Sesión")
            }

            if (mensajeError != null) {
                Text(mensajeError ?: "", color = Color.Red)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text("¿No tienes cuenta? Regístrate",
                color = Color.White,
                modifier = Modifier.clickable {
                    navController.navigate("register")
                })
        }
    }
}
