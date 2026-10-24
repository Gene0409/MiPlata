package com.example.miplata

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ValidacionesTest {
    @Test fun `monto valido en pesos chilenos`() {
        assertEquals(15000.0, Validaciones.leerMontoCLP("15.000")!!, 0.001)
        assertEquals(15000.50, Validaciones.leerMontoCLP("15.000,50")!!, 0.001)
        assertEquals(12.5, Validaciones.leerMontoCLP("12.5")!!, 0.001)
        assertEquals(300000.0, Validaciones.leerMontoCLP("300000")!!, 0.001)
    }

    @Test fun `rechaza montos negativos o no numericos`() {
        assertEquals("El monto debe ser mayor que cero.", Validaciones.validarMonto("0"))
        assertEquals("El monto debe ser mayor que cero.", Validaciones.validarMonto("-2"))
        assertEquals("Ingresa un número válido.", Validaciones.validarMonto("abc"))
        assertNull(Validaciones.validarMonto("1500"))
    }

    @Test fun `valida campos individuales de meta`() {
        assertEquals("Escribe un nombre para tu meta.", Validaciones.validarNombreMeta(" "))
        assertEquals("El objetivo debe ser mayor que cero.", Validaciones.validarObjetivoMeta("0"))
        assertEquals("El ahorro no puede superar el objetivo.",
            Validaciones.validarAhorroMeta("200000", "100000"))
        assertNull(Validaciones.validarAhorroMeta("50000", "100000"))
    }

    @Test fun `valida modificacion de ahorro`() {
        assertEquals("No puede superar el objetivo.",
            Validaciones.validarActualizacionAhorro("120000", 100000.0))
        assertNull(Validaciones.validarActualizacionAhorro("100000", 100000.0))
    }
}
