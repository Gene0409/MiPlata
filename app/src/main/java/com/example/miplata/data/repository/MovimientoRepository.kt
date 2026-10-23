
package com.example.miplata.data.repository

import com.example.miplata.data.local.MovimientoDao
import com.example.miplata.data.local.MovimientoEntity
import kotlinx.coroutines.flow.Flow

class MovimientoRepository(
    private val movimientoDao: MovimientoDao
) {
    fun obtenerMovimientos(): Flow<List<MovimientoEntity>> =
        movimientoDao.obtenerMovimientos()

    suspend fun insertarMovimiento(movimiento: MovimientoEntity) {
        movimientoDao.insertarMovimiento(movimiento)
    }

    suspend fun eliminarMovimiento(id: Long) {
        movimientoDao.eliminarMovimiento(id)
    }
}
