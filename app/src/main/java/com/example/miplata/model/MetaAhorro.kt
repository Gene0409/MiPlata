
package com.example.miplata.model

data class MetaAhorro(
    val nombre: String,
    val montoObjetivo: Double,
    val montoAhorrado: Double = 0.0
) {
    val progreso: Float
        get() = if (montoObjetivo > 0) {
            (montoAhorrado / montoObjetivo).toFloat().coerceIn(0f, 1f)
        } else {
            0f
        }
}
