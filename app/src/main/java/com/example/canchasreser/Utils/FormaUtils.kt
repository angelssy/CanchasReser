package com.example.canchasreser.Utils


import java.text.NumberFormat
import java.util.Locale

fun formatPrecio(precio: Double): String {
    val formato = NumberFormat.getNumberInstance(Locale("es", "CL"))
    return "$" + formato.format(precio.toInt())
}
