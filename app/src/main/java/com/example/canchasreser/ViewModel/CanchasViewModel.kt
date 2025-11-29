package com.example.canchasreser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import com.example.canchasreser.model.Cancha
import com.example.canchasreser.Network.RetrofitClient
import kotlinx.coroutines.launch

class CanchasViewModel : ViewModel()  {

    val canchas = mutableStateOf<List<Cancha>>(emptyList())
    val loading = mutableStateOf(true)
    val error = mutableStateOf<String?>(null)

    init {
        cargarCanchas()
    }

    fun cargarCanchas() {
        viewModelScope.launch {
            try {
                val data = RetrofitClient.api.obtenerCanchas()
                canchas.value = data
                loading.value = false
            } catch (e: Exception) {
                error.value = e.message
                loading.value = false
            }
        }
    }
    fun despertarApi() {
        viewModelScope.launch {
            try {
                RetrofitClient.api.obtenerCanchas()
            } catch (_: Exception) {}
        }
    }
}