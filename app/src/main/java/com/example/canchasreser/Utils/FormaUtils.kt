package com.example.canchasreser.Utils


import java.text.NumberFormat
import java.util.Locale

fun formatPrecio(precio: Double): String {
    val formato = NumberFormat.getNumberInstance(Locale("es", "CL"))
    return "$" + formato.format(precio.toInt())


}
fun detectarTipoCancha(nombre: String, imagen: String): String {
    val texto = (nombre + imagen).lowercase()

    return when {
        "voley" in texto || "vóley" in texto -> "voley"
        "basquet" in texto || "básquet" in texto -> "basquet"
        "tenis" in texto -> "tenis"
        else -> "futbol"
    }
}

