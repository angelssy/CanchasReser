package com.example.canchasreser.ViewModel



import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.canchasreser.Model.Producto

class CatalogoViewModel : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        // Datos de prueba
        _productos.value = listOf(
            Producto(1, "Cancha 1", "Descripción cancha 1", 2000.0, "https://via.placeholder.com/150"),
            Producto(2, "Cancha 2", "Descripción cancha 2", 2500.0, "https://via.placeholder.com/150"),
            Producto(3, "Cancha 3", "Descripción cancha 3", 3000.0, "https://via.placeholder.com/150")
        )
    }

    // Función que podrías mantener para futuras cargas de datos reales
    fun cargarProductos() {
        // Aquí puedes conectar a tu API o base de datos
    }

    fun buscarProductoPorId(id: Int): Producto? {
        return _productos.value.find { it.id == id }
    }
}
