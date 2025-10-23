package com.example.canchasreser.Model

object FakeDatabase {
    private val usuarios = mutableListOf<Usuario>()

    // Función para registrar un usuario
    fun registrar(usuario: Usuario): Boolean {
        if (usuarios.any { it.email == usuario.email }) return false
        usuarios.add(usuario)
        return true
    }

    // Función para realizar login de un usuario
    fun login(email: String, password: String): Boolean {
        return usuarios.any { it.email == email && it.password == password }
    }

    // Función para obtener todos los usuarios (opcional, en caso de que se necesite)
    fun obtenerUsuarios(): List<Usuario> {
        return usuarios
    }
}