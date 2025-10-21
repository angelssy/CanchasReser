package com.example.canchasreser.Model


data class Producto(
    val id: Int,
    val nombre: String,
    val descripcion: String?,
    val precio: Double,
    val imagen: String? // URL o nombre de recurso
)