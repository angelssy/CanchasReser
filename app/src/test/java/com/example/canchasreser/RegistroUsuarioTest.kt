package com.example.canchasreser
import com.example.canchasreser.model.FakeDatabase
import com.example.canchasreser.model.Usuario
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import com.example.canchasreser.model.Cancha
import com.example.canchasreser.viewmodel.CarritoViewModel
class RegistroUsuarioTest {

    @Before
    fun limpiarBase() {
        FakeDatabase.clear()
    }

    @Test
    fun `registrar usuario exitoso`() {
        val usuario = Usuario("Juan", "juan@test.com", "12345678-9", "1234")
        val resultado = FakeDatabase.registrar(usuario)
        assertTrue(resultado)
    }

    @Test
    fun `registrar usuario duplicado falla`() {
        val usuario = Usuario("Juan", "juan@test.com", "12345678-9", "1234")
        FakeDatabase.registrar(usuario)

        val resultado = FakeDatabase.registrar(usuario)
        assertFalse(resultado)
    }
    @Test
    fun `no debe permitir registrar usuario duplicado`() {
        FakeDatabase.clear()

        val user1 = Usuario("Juan", "juan@test.com", "123", "pass")
        val user2 = Usuario("Juan", "juan@test.com", "123", "pass")

        FakeDatabase.registrar(user1)
        val resultado = FakeDatabase.registrar(user2)

        assertFalse(resultado)
    }
    @Test
    fun `login debe ser exitoso con credenciales correctas`() {
        FakeDatabase.clear()

        val user = Usuario("Mario", "mario@test.com", "444", "1234")
        FakeDatabase.registrar(user)

        val resultado = FakeDatabase.login("mario@test.com", "1234")

        assertTrue(resultado)
    }
    @Test
    fun `login debe fallar por contrasena incorrecta`() {
        FakeDatabase.clear()

        val user = Usuario("Ana", "ana@test.com", "555", "abcd")
        FakeDatabase.registrar(user)

        val resultado = FakeDatabase.login("ana@test.com", "1234")

        assertFalse(resultado)
    }


}