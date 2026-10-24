package com.example.miplata

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.miplata.data.local.MetaAhorroEntity
import com.example.miplata.data.local.MiPlataDatabase
import com.example.miplata.data.local.MovimientoEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

/** Requiere emulador/dispositivo Android: prueba Room después de cerrar y abrir la BD. */
@RunWith(AndroidJUnit4::class)
class PersistenciaLocalTest {
    @Test fun mantieneMovimientosYMetasAlReabrir() = runBlocking {
        val contexto = InstrumentationRegistry.getInstrumentation().targetContext
        val nombre = "prueba_persistencia_miplata"
        contexto.deleteDatabase(nombre)
        try {
            var bd = Room.databaseBuilder(
                contexto, MiPlataDatabase::class.java, nombre
            ).build()
            bd.movimientoDao().insertarMovimiento(
                MovimientoEntity(
                    monto = 9000.0, tipo = "GASTO", categoria = "Comida",
                    descripcion = "Prueba", fecha = System.currentTimeMillis()
                )
            )
            bd.metaAhorroDao().insertarMeta(
                MetaAhorroEntity(nombre = "Viaje", montoObjetivo = 200000.0,
                    montoAhorrado = 5000.0)
            )
            bd.close()

            bd = Room.databaseBuilder(
                contexto, MiPlataDatabase::class.java, nombre
            ).build()
            assertEquals(1, bd.movimientoDao().obtenerMovimientos().first().size)
            assertEquals(1, bd.metaAhorroDao().obtenerMetas().first().size)
            bd.close()
        } finally {
            contexto.deleteDatabase(nombre)
        }
    }
}
