
package com.example.miplata.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovimientoDao {

    @Query("SELECT * FROM movimientos ORDER BY fecha DESC")
    fun obtenerMovimientos(): Flow<List<MovimientoEntity>>

    @Insert
    suspend fun insertarMovimiento(movimiento: MovimientoEntity)

    @Query("DELETE FROM movimientos WHERE id = :id")
    suspend fun eliminarMovimiento(id: Long)
}
