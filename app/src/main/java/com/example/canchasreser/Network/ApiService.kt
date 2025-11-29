package com.example.canchasreser.Network
import com.example.canchasreser.model.Cancha
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("api/canchas")
    suspend fun obtenerCanchas(): List<Cancha>

    @GET("api/canchas/{id}")
    suspend fun obtenerCanchaPorId(@Path("id") id: Int): Cancha
}