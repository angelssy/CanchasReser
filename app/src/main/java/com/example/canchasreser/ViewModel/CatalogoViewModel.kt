package com.example.canchasreser.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.canchasreser.model.Cancha
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.canchasreser.viewmodel.CatalogoViewModel


class CatalogoViewModel : ViewModel() {

    private val _productos = MutableStateFlow<List<Cancha>>(emptyList())
    val productos: StateFlow<List<Cancha>> = _productos

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun cargarProductos(context: Context) {
        viewModelScope.launch {
            _loading.value = true
            delay(800) // Simulación de carga

            try {
                val json = context.assets.open("Canchas.json")
                    .bufferedReader().use { it.readText() }

                val itemType = object : TypeToken<List<Cancha>>() {}.type
                _productos.value = Gson().fromJson(json, itemType)
            } catch (e: Exception) {
                e.printStackTrace()
                _productos.value = emptyList()
            }

            _loading.value = false
        }
    }

    fun buscarCanchaPorId(id: Int): Cancha? {
        return _productos.value.find { it.id == id }
    }
}
