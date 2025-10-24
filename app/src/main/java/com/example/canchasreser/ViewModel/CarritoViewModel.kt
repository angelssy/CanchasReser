package com.example.canchasreser.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.canchasreser.model.Carrito
import com.example.canchasreser.model.Cancha

class CarritoViewModel: ViewModel() {
    val items = mutableStateListOf<Carrito>()

    fun agregarAlCarrito(cancha: Cancha) {
        val existente = items.find { it.cancha.id == cancha.id }
        if (existente != null) {
            existente.cantidad += 1
        } else {
            items.add(Carrito(cancha))
        }
    }

    fun eliminarDelCarrito(item: Carrito) {
        items.remove(item)
    }

    fun actualizarCantidad(item: Carrito, cantidad: Int) {
        if (cantidad <= 0) {
            eliminarDelCarrito(item)
        } else {
            val index = items.indexOf(item)
            if (index != -1) items[index].cantidad = cantidad
        }
    }

    fun total(): Double {
        return items.sumOf { it.cancha.precioHora * it.cantidad }
    }

    fun vaciarCarrito() {
        items.clear()
    }
}
