package com.example.canchasreser.repository


import com.example.canchasreser.model.Cancha
import com.example.canchasreser.Network.RetrofitClient

class CanchaRepository {

    suspend fun cargarCanchas(): List<Cancha> {
        return RetrofitClient.api.obtenerCanchas()
    }

    suspend fun obtenerCanchaPorId(id: Int): Cancha {
        return RetrofitClient.api.obtenerCanchaPorId(id)
    }
}