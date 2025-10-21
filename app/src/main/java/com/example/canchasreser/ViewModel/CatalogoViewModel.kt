package com.example.canchasreser.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.canchasreser.Model.Producto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CatalogoViewModel : ViewModel() {

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun cargarProductos(context: Context) {
        viewModelScope.launch {
            _loading.value = true
            delay(800) // Simulación de carga

            try {
                val json = context.assets.open("productos.json")
                    .bufferedReader().use { it.readText() }

                val itemType = object : TypeToken<List<Producto>>() {}.type
                _productos.value = Gson().fromJson(json, itemType)
            } catch (e: Exception) {
                e.printStackTrace()
                _productos.value = emptyList()
            }

            _loading.value = false
        }
    }

    fun buscarProductoPorId(id: Int): Producto? {
        return _productos.value.find { it.id == id }
    }
}