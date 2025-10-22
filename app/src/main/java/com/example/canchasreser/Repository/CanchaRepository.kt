package com.example.canchasreser.repository

import android.content.Context
import com.example.canchasreser.model.Cancha
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CanchaRepository {

    fun obtenerCanchasDesdeAssets(context: Context, filename: String = "canchas.json"): List<Cancha> {
        return try {
            val json = context.assets.open(filename).bufferedReader().use { it.readText() }
            val listType = object : TypeToken<List<Cancha>>() {}.type
            Gson().fromJson<List<Cancha>>(json, listType) ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}
