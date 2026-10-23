
package com.example.miplata.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "metas_ahorro")
data class MetaAhorroEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nombre: String,
    val montoObjetivo: Double,
    val montoAhorrado: Double,
    val fechaCreacion: Long = System.currentTimeMillis()
)
