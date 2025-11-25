package com.example.canchasreser.Network

import com.example.canchasreser.model.ClimaResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ClimaApi {

    @GET("data/2.5/weather")
    suspend fun obtenerClima(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "es",
        @Query("appid") apiKey: String
    ): ClimaResponse
}