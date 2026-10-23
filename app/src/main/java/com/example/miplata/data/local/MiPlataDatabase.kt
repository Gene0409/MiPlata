
package com.example.miplata.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        MovimientoEntity::class,
        MetaAhorroEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class MiPlataDatabase : RoomDatabase() {

    abstract fun movimientoDao(): MovimientoDao

    abstract fun metaAhorroDao(): MetaAhorroDao
}
