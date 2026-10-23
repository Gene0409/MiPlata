
package com.example.miplata.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

private val MIGRACION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS `metas_ahorro` (
                `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `nombre` TEXT NOT NULL,
                `montoObjetivo` REAL NOT NULL,
                `montoAhorrado` REAL NOT NULL,
                `fechaCreacion` INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }
}

object DatabaseProvider {

    @Volatile
    private var INSTANCE: MiPlataDatabase? = null

    fun obtenerBaseDatos(context: Context): MiPlataDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                MiPlataDatabase::class.java,
                "miplata_database"
            )
                .addMigrations(MIGRACION_1_2)
                .build()
                .also { INSTANCE = it }
        }
    }
}
