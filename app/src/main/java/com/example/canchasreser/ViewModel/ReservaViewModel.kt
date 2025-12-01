package com.example.canchasreser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.canchasreser.model.Reserva
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReservaViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _resultado = MutableStateFlow<Boolean?>(null)
    val resultado: StateFlow<Boolean?> = _resultado

    fun guardarReserva(reserva: Reserva) {
        viewModelScope.launch {
            db.collection("reservas")
                .add(reserva)
                .addOnSuccessListener { _resultado.value = true }
                .addOnFailureListener { _resultado.value = false }
        }
    }

    fun resetResultado() {
        _resultado.value = null
    }
}