package com.example.canchasreser.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import com.example.canchasreser.model.JugadorPos

class CarritoViewModel : ViewModel() {
    private fun posicionesFutbol(offsetId: Int) = listOf(
        JugadorPos(offsetId + 1, 0.43f, 0.78f),
        JugadorPos(offsetId + 2, 0.32f, 0.53f),
        JugadorPos(offsetId + 3, 0.57f, 0.53f),
        JugadorPos(offsetId + 4, 0.7f, 0.32f),
        JugadorPos(offsetId + 5, 0.25f, 0.84f),
        JugadorPos(offsetId + 6, 0.82f, 0.53f),
        JugadorPos(offsetId + 7, 0.45f, 0.85f),
        JugadorPos(offsetId + 8, 0.9f, 0.84f),
        JugadorPos(offsetId + 9, 0.56f, 0.71f),
        JugadorPos(offsetId + 10, 0.67f, 0.85f),
        JugadorPos(offsetId + 11, 0.23f, 0.50f)
    )

    // POSICIONES voley
    private fun posicionesVoley(offsetId: Int) = listOf(
        JugadorPos(offsetId + 1, 0.36f, 0.74f),
        JugadorPos(offsetId + 2, 0.5f, 0.80f),
        JugadorPos(offsetId + 3, 0.33f, 0.18f),

        JugadorPos(offsetId + 4, 0.33f, 0.97f),
        JugadorPos(offsetId + 5, 0.78f, 0.74f),
        JugadorPos(offsetId + 6, 0.53f, 0.33f),

        JugadorPos(offsetId + 7, 0.55f, 0.97f),
        JugadorPos(offsetId + 8, 0.53f, 0.18f),
        JugadorPos(offsetId + 9, 0.30f, 0.39f),

        JugadorPos(offsetId + 10, 0.78f, 0.97f),
        JugadorPos(offsetId + 11, 0.74f, 0.18f),
        JugadorPos(offsetId + 12, 0.76f, 0.39f)
    )
    // POSICIONES basquet
    private fun posicionesBasquet(offsetId: Int) = listOf(
        JugadorPos(offsetId + 1, 50f, 85f),
        JugadorPos(offsetId + 2, 30f, 60f),
        JugadorPos(offsetId + 3, 70f, 60f),
        JugadorPos(offsetId + 4, 40f, 35f),
        JugadorPos(offsetId + 5, 60f, 35f),
        JugadorPos(offsetId + 1, 80f, 85f),
        JugadorPos(offsetId + 2, 30f, 60f),
        JugadorPos(offsetId + 3, 70f, 60f),
        JugadorPos(offsetId + 4, 40f, 35f),
        JugadorPos(offsetId + 5, 60f, 35f)
    )

    // POSICIONES tenis
    private fun posicionesTenis(offsetId: Int) = listOf(
        JugadorPos(offsetId + 1, 50f, 75f),
        JugadorPos(offsetId + 2, 50f, 30f),
        JugadorPos(offsetId + 1, 50f, 75f),
        JugadorPos(offsetId + 2, 50f, 30f)
    )

    private val _equipoA = MutableStateFlow(posicionesFutbol(0))
    private val _equipoB = MutableStateFlow(posicionesFutbol(100))

    private val _equipoActual = MutableStateFlow("A")
    val equipoActual = _equipoActual.asStateFlow()

    private val _modo = MutableStateFlow("futbol")
    val modo = _modo.asStateFlow()

    fun setDeporte(tipo: String) {
        _modo.value = tipo.lowercase()
        _equipoActual.value = "A"

        when (_modo.value) {
            "voley" -> _equipoA.value = posicionesVoley(0)
            "basquet" -> _equipoA.value = posicionesBasquet(0)
            "tenis" -> _equipoA.value = posicionesTenis(0)
            else -> {
                _equipoA.value = posicionesFutbol(0)
                _equipoB.value = posicionesFutbol(200)
            }
        }
    }

    val jugadores = combine(
        _equipoA,
        _equipoB,
        _equipoActual,
        _modo
    ) { equipoA, equipoB, actual, modo ->

        if (modo != "futbol") equipoA
        else if (actual == "A") equipoA
        else equipoB
    }

    fun cambiarEquipo() {
        if (_modo.value == "futbol") {
            _equipoActual.value = if (_equipoActual.value == "A") "B" else "A"
        }
    }

    fun actualizarJugador(index: Int, nombre: String, rut: String) {
        val lista = _equipoA.value.toMutableList()
        lista[index] = lista[index].copy(nombre = nombre, rut = rut)
        _equipoA.value = lista
    }

    fun hayJugadoresCompletosEquipoActual(): Boolean {
        return _equipoA.value.any { it.nombre.isNotBlank() && it.rut.isNotBlank() }
    }
}
