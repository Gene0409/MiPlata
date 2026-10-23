
package com.example.miplata.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MetaAhorroDao {

    @Query("SELECT * FROM metas_ahorro ORDER BY fechaCreacion DESC")
    fun obtenerMetas(): Flow<List<MetaAhorroEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarMeta(meta: MetaAhorroEntity)

    @Update
    suspend fun actualizarMeta(meta: MetaAhorroEntity)

    @Delete
    suspend fun eliminarMeta(meta: MetaAhorroEntity)
}
