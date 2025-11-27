package com.example.canchasreser.viewmodel

import com.example.canchasreser.model.Cancha
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class CarritoViewModelTest {

    private lateinit var carrito: CarritoViewModel
    private val cancha1 = Cancha(1, "Cancha Futbol", "Césped", "90x45", "7.32x2.44", "22 jugadores", "Descripción", "Ubicación", 15000.0, "futbol")
    private val cancha2 = Cancha(2, "Cancha Vóley", "Madera", "18x9", "Altura ajustable", "12 jugadores", "Descripción", "Ubicación", 10000.0, "voley")

    @Before
    fun setup() {
        carrito = CarritoViewModel()
    }

    @Test
    fun testAgregarAlCarrito() {
        carrito.agregarAlCarrito(cancha1)
        assertEquals(1, carrito.items.size)
        assertEquals(15000.0, carrito.total())
    }

    @Test
    fun testAgregarMismaCanchaIncrementaCantidad() {
        carrito.agregarAlCarrito(cancha1)
        carrito.agregarAlCarrito(cancha1)
        assertEquals(1, carrito.items.size)
        assertEquals(2, carrito.items[0].cantidad)
        assertEquals(15000.0 * 2, carrito.total())
    }

    @Test
    fun testEliminarDelCarrito() {
        carrito.agregarAlCarrito(cancha1)
        val item = carrito.items[0]
        carrito.eliminarDelCarrito(item)
        assertEquals(0, carrito.items.size)
        assertEquals(0.0, carrito.total())
    }

    @Test
    fun testActualizarCantidad() {
        carrito.agregarAlCarrito(cancha1)
        val item = carrito.items[0]
        carrito.actualizarCantidad(item, 5)
        assertEquals(5, carrito.items[0].cantidad)
        assertEquals(15000.0 * 5, carrito.total())
    }

    @Test
    fun testVaciarCarrito() {
        carrito.agregarAlCarrito(cancha1)
        carrito.agregarAlCarrito(cancha2)
        carrito.vaciarCarrito()
        assertEquals(0, carrito.items.size)
        assertEquals(0.0, carrito.total())
    }

    @Test
    fun testAgregarJugador() {
        carrito.agregarJugador()
        carrito.agregarJugador()
        assertEquals(listOf(1, 2), carrito.jugadores.value)
    }

    @Test
    fun testEliminarJugador() {
        carrito.agregarJugador()
        carrito.agregarJugador()
        carrito.eliminarJugador()
        assertEquals(listOf(1), carrito.jugadores.value)
    }

    @Test
    fun testVaciarJugadores() {
        carrito.agregarJugador()
        carrito.agregarJugador()
        carrito.vaciarJugadores()
        assertEquals(emptyList<Int>(), carrito.jugadores.value)
    }
}