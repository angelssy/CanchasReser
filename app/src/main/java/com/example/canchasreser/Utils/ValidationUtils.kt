package com.example.canchasreser.Utils



fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
    return this.isNotBlank() && emailRegex.matches(this)
}

fun validarRut(rut: String): Boolean {
    val cleanRut = rut.replace(".", "").replace("-", "").uppercase()

    if (cleanRut.length < 2) return false

    val cuerpo = cleanRut.dropLast(1)
    val dv = cleanRut.last()

    var suma = 0
    var multiplo = 2

    for (i in cuerpo.reversed()) {
        suma += Character.getNumericValue(i) * multiplo
        multiplo = if (multiplo < 7) multiplo + 1 else 2
    }

    val resto = 11 - (suma % 11)

    val dvEsperado = when (resto) {
        11 -> '0'
        10 -> 'K'
        else -> resto.toString()[0]
    }

    return dv == dvEsperado
}

fun String.isValidRut(): Boolean {
    val rutClean = this
        .replace(".", "")
        .replace("-", "")
        .uppercase()

    if (rutClean.length < 2) return false

    val body = rutClean.dropLast(1)
    val dv = rutClean.last()

    return try {
        val reversedDigits = body.reversed().map { it.toString().toInt() }
        var multiplier = 2
        val sum = reversedDigits.fold(0) { acc, digit ->
            val res = acc + digit * multiplier
            multiplier = if (multiplier < 7) multiplier + 1 else 2
            res
        }
        val mod = 11 - (sum % 11)
        val dvExpected = when (mod) {
            11 -> '0'
            10 -> 'K'
            else -> mod.toString().first()
        }

        dv == dvExpected
    } catch (e: NumberFormatException) {
        false
    }
}
