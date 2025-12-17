package com.example.canchasreser.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.canchasreser.model.JugadorPos
import com.example.canchasreser.model.Cancha

class CarritoViewModel : ViewModel() {

    private val _jugadores = MutableStateFlow(
        listOf(
            JugadorPos(1, x = 50f, y = 90f),  // Arquero
            JugadorPos(2, x = 20f, y = 70f),
            JugadorPos(3, x = 40f, y = 70f),
            JugadorPos(4, x = 60f, y = 70f),
            JugadorPos(5, x = 80f, y = 70f),

            JugadorPos(6, x = 30f, y = 45f),
            JugadorPos(7, x = 50f, y = 45f),
            JugadorPos(8, x = 70f, y = 45f),

            JugadorPos(9, x = 35f, y = 20f),
            JugadorPos(10, x = 50f, y = 20f),
            JugadorPos(11, x = 65f, y = 20f),
        )
    )

    val jugadores = _jugadores.asStateFlow()

    fun actualizarJugador(index: Int, nombre: String, rut: String) {
        val lista = _jugadores.value.toMutableList()
        lista[index] = lista[index].copy(nombre = nombre, rut = rut)
        _jugadores.value = lista
    }
    // ✅ ESTA FUNCIÓN ES LA QUE TE FALTABA
    fun hayJugadoresCompletos(): Boolean {
        return _jugadores.value.any {
            it.nombre.isNotBlank() && it.rut.isNotBlank()
        }
}
}