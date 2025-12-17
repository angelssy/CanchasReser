package com.example.canchasreser.model

data class Cancha(
    val id: String,
    val nombre: String,
    val tipoSuperficie: String,
    val dimensiones: String,
    val medidas: String,
    val jugadores: String,
    val descripcion: String?,
    val ubicacion: String,
    val precioHora: Double,
    val imagen: String
)