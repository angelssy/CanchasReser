package com.example.canchasreser.viewmodel
import com.example.canchasreser.model.FakeDatabase
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class AuthViewModelTest {

    private lateinit var authViewModel: AuthViewModel

    @Before
    fun setup() {
        // Limpiamos la base de datos falsa antes de cada test
        FakeDatabase.clear()
        authViewModel = AuthViewModel()
    }

    @Test
    fun testRegistroExitoso() {
        authViewModel.registrar("Angel", "angel@test.com", "12345678-9", "1234")
        assertEquals("Registro exitoso ", authViewModel.mensaje.value)
    }

    @Test
    fun testRegistroUsuarioExistente() {
        authViewModel.registrar("Angel", "angel@test.com", "12345678-9", "1234")
        authViewModel.registrar("Angel", "angel@test.com", "12345678-9", "1234")
        assertEquals("El usuario ya existe ", authViewModel.mensaje.value)
    }

    @Test
    fun testLoginExitoso() {
        authViewModel.registrar("Angel", "angel@test.com", "12345678-9", "1234")
        val result = authViewModel.login("angel@test.com", "1234")
        assertTrue(result)
        assertEquals("Inicio de sesión exitoso ", authViewModel.mensaje.value)
        assertEquals("angel@test.com", authViewModel.usuarioActual.value)
    }

    @Test
    fun testLoginFallido() {
        val result = authViewModel.login("noexiste@test.com", "0000")
        assertFalse(result)
        assertEquals("Credenciales inválidas ", authViewModel.mensaje.value)
        assertNull(authViewModel.usuarioActual.value)
    }
}