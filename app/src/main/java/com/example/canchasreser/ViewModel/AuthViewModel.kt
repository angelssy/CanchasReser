package com.example.canchasreser.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.canchasreser.model.Usuario
import com.google.firebase.firestore.FirebaseFirestore

class AuthViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var mensaje = mutableStateOf("")
    var usuarioActual = mutableStateOf<String?>(null)

    fun registrar(nombre: String, email: String, rut: String, password: String) {
        val usuario = Usuario(nombre, email, rut, password)

        db.collection("usuarios")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    mensaje.value = "El usuario ya existe"
                } else {
                    db.collection("usuarios")
                        .add(usuario)
                        .addOnSuccessListener {
                            mensaje.value = "Registro exitoso"
                        }
                        .addOnFailureListener {
                            mensaje.value = "Error al registrar"
                        }
                }
            }
    }

    fun login(email: String, password: String, onResult: (Boolean) -> Unit) {

        // ✅ ACCESO ADMIN SIN REGISTRARSE
        if (email == "admin@admin.com" && password == "admin123") {
            usuarioActual.value = "ADMIN"
            mensaje.value = "Administrador"
            onResult(true)
            return
        }

        db.collection("usuarios")
            .whereEqualTo("email", email)
            .whereEqualTo("password", password)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    usuarioActual.value = email
                    mensaje.value = "Inicio de sesión exitoso"
                    onResult(true)
                } else {
                    mensaje.value = "Credenciales inválidas"
                    onResult(false)
                }
            }
            .addOnFailureListener {
                mensaje.value = "Error de conexión"
                onResult(false)
            }
    }

    fun logout() {
        usuarioActual.value = null
        mensaje.value = "Sesión cerrada"
    }

    fun esAdmin(): Boolean {
        return usuarioActual.value == "ADMIN"
    }
}