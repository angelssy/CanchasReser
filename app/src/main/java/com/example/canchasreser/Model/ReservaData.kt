package com.example.canchasreser.model

import kotlinx.serialization.Serializable
@Serializable
data class ReservaData(
    val nombreResponsable: String,
    val cantidadJugadores: Int,
    val fecha: String,
    val hora: String,
    val canchaNombre: String
)