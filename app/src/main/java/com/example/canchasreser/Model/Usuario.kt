package com.example.canchasreser.Model

data class Usuario(
    val nombre: String,
    val email: String,
    val direccion: String,
    val rut: String, // Incluir RUT
    val password: String
)
