package com.example.canchasreser.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import com.example.canchasreser.model.JugadorPos

class CarritoViewModel : ViewModel() {

    // 🔹 POSICIONES BASE (las que ya usabas)
    private fun posicionesIniciales(offsetId: Int) = listOf(
        JugadorPos(offsetId + 1, x = 50f, y = 90f),  // Arquero
        JugadorPos(offsetId + 2, x = 20f, y = 70f),
        JugadorPos(offsetId + 3, x = 40f, y = 70f),
        JugadorPos(offsetId + 4, x = 60f, y = 70f),
        JugadorPos(offsetId + 5, x = 80f, y = 70f),

        JugadorPos(offsetId + 6, x = 30f, y = 45f),
        JugadorPos(offsetId + 7, x = 50f, y = 45f),
        JugadorPos(offsetId + 8, x = 70f, y = 45f),

        JugadorPos(offsetId + 9, x = 35f, y = 20f),
        JugadorPos(offsetId + 10, x = 50f, y = 20f),
        JugadorPos(offsetId + 11, x = 65f, y = 20f),
    )

    // 🟢 EQUIPO A
    private val _equipoA = MutableStateFlow(posicionesIniciales(0))

    // 🔵 EQUIPO B
    private val _equipoB = MutableStateFlow(posicionesIniciales(100))

    // ⚪ EQUIPO ACTUAL
    private val _equipoActual = MutableStateFlow("A")
    val equipoActual = _equipoActual.asStateFlow()

    // 👥 JUGADORES A MOSTRAR SEGÚN EQUIPO
    val jugadores = combine(
        _equipoA,
        _equipoB,
        _equipoActual
    ) { equipoA, equipoB, actual ->
        if (actual == "A") equipoA else equipoB
    }

    // 🔁 CAMBIAR ENTRE EQUIPO A / B
    fun cambiarEquipo() {
        _equipoActual.value = if (_equipoActual.value == "A") "B" else "A"
    }

    // ✏️ ACTUALIZAR JUGADOR SEGÚN EQUIPO
    fun actualizarJugador(index: Int, nombre: String, rut: String) {
        if (_equipoActual.value == "A") {
            val lista = _equipoA.value.toMutableList()
            lista[index] = lista[index].copy(nombre = nombre, rut = rut)
            _equipoA.value = lista
        } else {
            val lista = _equipoB.value.toMutableList()
            lista[index] = lista[index].copy(nombre = nombre, rut = rut)
            _equipoB.value = lista
        }
    }

    // ✅ VALIDAR SI EL EQUIPO ACTUAL TIENE JUGADORES
    fun hayJugadoresCompletosEquipoActual(): Boolean {
        return if (_equipoActual.value == "A") {
            _equipoA.value.any { it.nombre.isNotBlank() && it.rut.isNotBlank() }
        } else {
            _equipoB.value.any { it.nombre.isNotBlank() && it.rut.isNotBlank() }
        }
    }

    // ✅ VALIDAR SI AMBOS EQUIPOS TIENEN JUGADORES
    fun ambosEquiposCompletos(): Boolean {
        return _equipoA.value.any { it.nombre.isNotBlank() && it.rut.isNotBlank() } &&
                _equipoB.value.any { it.nombre.isNotBlank() && it.rut.isNotBlank() }
    }
}
