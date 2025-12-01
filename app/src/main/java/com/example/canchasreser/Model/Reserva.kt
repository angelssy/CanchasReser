package com.example.canchasreser.model

import kotlinx.serialization.Serializable

@Serializable
data class Reserva(
    val responsable: String = "",
    val jugadores: List<String> = emptyList(),
    val fecha: String = "",
    val hora: String = "",
    val canchaId: Int = 0,
    val canchaNombre: String = "",
    val total: Double = 0.0
)