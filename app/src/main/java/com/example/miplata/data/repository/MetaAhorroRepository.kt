
package com.example.miplata.data.repository

import com.example.miplata.data.local.MetaAhorroDao
import com.example.miplata.data.local.MetaAhorroEntity
import kotlinx.coroutines.flow.Flow

class MetaAhorroRepository(
    private val metaAhorroDao: MetaAhorroDao
) {
    fun obtenerMetas(): Flow<List<MetaAhorroEntity>> {
        return metaAhorroDao.obtenerMetas()
    }

    suspend fun insertarMeta(meta: MetaAhorroEntity) {
        metaAhorroDao.insertarMeta(meta)
    }

    suspend fun actualizarMeta(meta: MetaAhorroEntity) {
        metaAhorroDao.actualizarMeta(meta)
    }

    suspend fun eliminarMeta(meta: MetaAhorroEntity) {
        metaAhorroDao.eliminarMeta(meta)
    }
}
