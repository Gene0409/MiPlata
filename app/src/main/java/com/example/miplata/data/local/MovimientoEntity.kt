
package com.example.miplata.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movimientos")
data class MovimientoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val monto: Double,
    val tipo: String,
    val categoria: String,
    val descripcion: String,
    val fecha: Long,
    val imagenUri: String? = null
)
