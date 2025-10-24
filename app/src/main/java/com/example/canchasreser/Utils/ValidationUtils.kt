package com.example.canchasreser.Utils


// ✅ Validar correo electrónico con Regex
fun String.isValidEmail(): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
    return this.isNotBlank() && emailRegex.matches(this)
}

// ✅ Validar RUT chileno (con dígito verificador y formato)
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
