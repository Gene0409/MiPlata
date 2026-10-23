
package com.example.miplata

object Validaciones {

    fun validarMonto(valor: String): String? {
        if (valor.isBlank()) {
            return "Ingresa un monto."
        }

        val monto = valor.trim()
            .replace(",", ".")
            .toDoubleOrNull()

        if (monto == null || !monto.isFinite()) {
            return "Ingresa un número válido."
        }

        if (monto <= 0) {
            return "El monto debe ser mayor que cero."
        }

        return null
    }

    fun validarCategoria(categoria: String): String? {
        return if (categoria.isBlank()) {
            "Ingresa una categoría."
        } else {
            null
        }
    }

    fun validarMeta(nombre: String, objetivo: String): String? {
        if (nombre.isBlank()) {
            return "Ingresa el nombre de la meta."
        }

        if (objetivo.isBlank()) {
            return "Ingresa el monto objetivo."
        }

        val monto = objetivo.trim()
            .replace(",", ".")
            .toDoubleOrNull()

        if (monto == null || !monto.isFinite()) {
            return "Ingresa un monto objetivo válido."
        }

        if (monto <= 0) {
            return "El monto objetivo debe ser mayor que cero."
        }

        return null
    }
}
