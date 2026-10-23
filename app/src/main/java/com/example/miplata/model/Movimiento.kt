
package com.example.miplata.model

data class Movimiento(
    val id: Long = 0,
    val monto: Double,
    val tipo: TipoMovimiento,
    val categoria: String,
    val descripcion: String = "",
    val fecha: Long = System.currentTimeMillis(),
    val imagenUri: String? = null
)

enum class TipoMovimiento {
    INGRESO,
    GASTO
}
