package com.example.canchasreser.model


object FakeDatabase {

    private val usuarios = mutableListOf<Usuario>()

    fun registrar(usuario: Usuario): Boolean {
        if (usuarios.any { it.email == usuario.email }) return false
        usuarios.add(usuario)
        return true
    }

    fun login(email: String, password: String): Boolean {
        return usuarios.any { it.email == email && it.password == password }
    }

    // <- Agregar este método para limpiar la base antes de cada test
    fun clear() {
        usuarios.clear()
    }
}
