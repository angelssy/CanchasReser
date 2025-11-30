package com.example.canchasreser.Screen
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import com.example.canchasreser.Utils.isValidRut

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, viewModel: AuthViewModel) {

    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var rut by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var nombreError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var rutError by remember { mutableStateOf(false) }
    var passwordError by remember { mutableStateOf(false) }
    var mensajeErrorGeneral by remember { mutableStateOf<String?>(null) }

    val fieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedIndicatorColor = Color(0xFF0A6E2F),
        unfocusedIndicatorColor = Color(0xFF388E3C),
        cursorColor = Color.Black,
        focusedLabelColor = Color(0xFF0A6E2F)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF66BB6A)),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {

            Image(
                painter = painterResource(id = R.drawable.fff),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = "Canchas Reser",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 20.dp, top = 10.dp)
            )

            // NOMBRE
            OutlinedTextField(
                value = nombre,
                onValueChange = {
                    nombre = it
                    nombreError = false
                },
                label = { Text("Nombre completo") },
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                isError = nombreError
            )
            if (nombreError) {
                Text("Debes ingresar tu nombre", color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // EMAIL
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = false
                },
                label = { Text("Correo electrónico") },
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                isError = emailError
            )
            if (emailError) {
                Text("Correo inválido", color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // RUT
            OutlinedTextField(
                value = rut,
                onValueChange = {
                    rut = it
                    rutError = false
                },
                label = { Text("RUT") },
                placeholder = { Text("Ej: 12345678-9") },
                colors = fieldColors,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                isError = rutError
            )
            if (rutError) {
                Text("RUT inválido", color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(10.dp))

            // CONTRASEÑA
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = false
                },
                label = { Text("Contraseña") },
                colors = fieldColors,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                isError = passwordError
            )
            if (passwordError) {
                Text("La contraseña debe tener al menos 6 caracteres", color = Color.Red, fontSize = 12.sp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // BOTÓN REGISTRARSE
            Button(
                onClick = {
                    // Reset mensaje general
                    mensajeErrorGeneral = null

                    nombreError = nombre.isBlank()
                    emailError = !email.isValidEmail()
                    rutError = !rut.isValidRut()
                    passwordError = password.length < 6

                    if (nombreError || emailError || rutError || passwordError) {
                        mensajeErrorGeneral = "Por favor corrige los campos marcados"
                    } else {
                        viewModel.registrar(nombre.trim(), email.trim(), rut.trim(), password)

                        if (viewModel.mensaje.value == "Registro exitoso") {
                            mensajeErrorGeneral = null
                            navController.navigate("login") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            mensajeErrorGeneral = viewModel.mensaje.value
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                colors = ButtonDefaults.buttonColors(Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Registrarse", color = Color(0xFF0A6E2F), fontSize = 18.sp)
            }

            if (mensajeErrorGeneral != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = mensajeErrorGeneral ?: "",
                    color = Color.Red,
                    fontSize = 14.sp
                )
            }
        }
    }
}
