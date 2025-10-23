package com.example.canchasreser.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.canchasreser.Model.FakeDatabase
import com.example.canchasreser.Model.Usuario

class AuthViewModel : ViewModel() {

    var mensaje = mutableStateOf("")
    var usuarioActual = mutableStateOf<String?>(null)

    // Ahora toma los nuevos parámetros
    fun registrar(nombre: String, email: String, rut: String, password: String) {
        val nuevoUsuario = Usuario(nombre, email, rut, password)

        // Llamada a FakeDatabase para registrar el usuario
        if (FakeDatabase.registrar(nuevoUsuario)) {
            mensaje.value = "Registro exitoso ✅"
        } else {
            mensaje.value = "El usuario ya existe ❌"
        }
    }

    fun login(email: String, password: String): Boolean {
        return if (FakeDatabase.login(email, password)) {
            usuarioActual.value = email
            mensaje.value = "Inicio de sesión exitoso 🎉"
            true
        } else {
            mensaje.value = "Credenciales inválidas ❌"
            false
        }
    }
}
