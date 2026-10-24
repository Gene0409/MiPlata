package com.example.miplata

/** Reglas independientes de Jetpack Compose y de Android (testeables con JUnit). */
object Validaciones {

    /** Acepta CLP sin separador o con puntos de miles; coma decimal opcional. */
    fun leerMontoCLP(texto: String): Double? {
        val entrada = texto.trim().replace("$", "").replace(" ", "")
        val normalizado = when {
            ',' in entrada -> entrada.replace(".", "").replace(",", ".")
            entrada.matches(Regex("""\d{1,3}(\.\d{3})+""")) ->
                entrada.replace(".", "")
            else -> entrada
        }
        return normalizado.toDoubleOrNull()?.takeIf { it.isFinite() }
    }

    fun validarMonto(valor: String): String? = when {
        valor.isBlank() -> "Ingresa un monto."
        leerMontoCLP(valor) == null -> "Ingresa un número válido."
        leerMontoCLP(valor)!! <= 0 -> "El monto debe ser mayor que cero."
        else -> null
    }

    fun validarCategoria(categoria: String): String? =
        if (categoria.isBlank()) "Ingresa una categoría." else null

    fun validarNombreMeta(nombre: String): String? =
        if (nombre.isBlank()) "Escribe un nombre para tu meta." else null

    fun validarObjetivoMeta(objetivo: String): String? {
        val monto = leerMontoCLP(objetivo)
        return when {
            objetivo.isBlank() -> "Ingresa el monto objetivo."
            monto == null -> "Ingresa un monto objetivo válido."
            monto <= 0 -> "El objetivo debe ser mayor que cero."
            else -> null
        }
    }

    fun validarAhorroMeta(ahorro: String, objetivo: String): String? {
        val monto = leerMontoCLP(ahorro)
        val maximo = leerMontoCLP(objetivo)
        return when {
            ahorro.isBlank() -> "Ingresa el ahorro inicial (puede ser cero)."
            monto == null -> "Ingresa un ahorro válido."
            monto < 0 -> "El ahorro no puede ser negativo."
            maximo != null && maximo > 0 && monto > maximo ->
                "El ahorro no puede superar el objetivo."
            else -> null
        }
    }

    fun validarActualizacionAhorro(texto: String, objetivo: Double): String? {
        val monto = leerMontoCLP(texto)
        return when {
            texto.isBlank() || monto == null -> "Ingresa un monto válido."
            monto < 0 -> "El ahorro no puede ser negativo."
            monto > objetivo -> "No puede superar el objetivo."
            else -> null
        }
    }

    // Se conserva para compatibilidad con otras llamadas o pruebas previas.
    fun validarMeta(nombre: String, objetivo: String): String? =
        validarNombreMeta(nombre) ?: validarObjetivoMeta(objetivo)
}
