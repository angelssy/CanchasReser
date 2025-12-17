package com.example.canchasreser.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.canchasreser.model.Cancha
import com.example.canchasreser.Network.RetrofitClient
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class CanchasViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    val canchas = mutableStateOf<List<Cancha>>(emptyList())
    val loading = mutableStateOf(true)
    val error = mutableStateOf<String?>(null)

    init {
        escucharCanchas()
    }

    private fun escucharCanchas() {
        loading.value = true

        viewModelScope.launch {
            try {
                // 1️⃣ Cargar canchas BASE desde API (CORRECTO)
                val canchasApi = RetrofitClient.api.obtenerCanchas()

                // 2️⃣ Escuchar canchas ADMIN desde Firestore
                db.collection("canchas")
                    .addSnapshotListener { snapshot, e ->

                        if (e != null) {
                            canchas.value = canchasApi
                            loading.value = false
                            return@addSnapshotListener
                        }

                        val canchasAdmin = snapshot?.documents?.mapNotNull { doc ->
                            Cancha(
                                id = doc.id, // String
                                nombre = doc.getString("nombre") ?: "",
                                tipoSuperficie = "No especificado",
                                dimensiones = "-",
                                medidas = "-",
                                jugadores = "-",
                                descripcion = doc.getString("descripcion"),
                                ubicacion = "Chile",
                                precioHora = doc.getLong("precioHora")?.toDouble() ?: 0.0,
                                imagen = doc.getString("imagen") ?: ""
                            )
                        } ?: emptyList()

                        // 3️⃣ UNIR BASE + ADMIN
                        canchas.value = canchasApi + canchasAdmin
                        loading.value = false
                    }

            } catch (e: Exception) {
                error.value = e.message
                loading.value = false
            }
        }
    }
}
