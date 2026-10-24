
package com.example.miplata.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.miplata.Validaciones
import com.example.miplata.data.local.DatabaseProvider
import com.example.miplata.data.local.MovimientoEntity
import com.example.miplata.data.repository.MovimientoRepository
import com.example.miplata.model.Movimiento
import com.example.miplata.model.TipoMovimiento
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MovimientoFormularioState(
    val monto: String = "",
    val tipo: TipoMovimiento = TipoMovimiento.GASTO,
    val categoria: String = "",
    val descripcion: String = "",
    val errorMonto: String? = null,
    val errorCategoria: String? = null,
    val mensaje: String? = null
)

class MovimientoViewModel(application: Application) : AndroidViewModel(application) {

    private val dao =
        DatabaseProvider.obtenerBaseDatos(application).movimientoDao()

    private val repository = MovimientoRepository(dao)

    private val _movimientos = MutableStateFlow<List<Movimiento>>(emptyList())
    val movimientos: StateFlow<List<Movimiento>> = _movimientos.asStateFlow()

    private val _formulario = MutableStateFlow(MovimientoFormularioState())
    val formulario: StateFlow<MovimientoFormularioState> = _formulario.asStateFlow()

    private val _guardadoExitoso = MutableStateFlow(false)
    val guardadoExitoso: StateFlow<Boolean> = _guardadoExitoso.asStateFlow()

    private val _guardando = MutableStateFlow(false)
    val guardando: StateFlow<Boolean> = _guardando.asStateFlow()

    val ingresos: StateFlow<Double> = movimientos
        .map { lista ->
            lista.filter { it.tipo == TipoMovimiento.INGRESO }
                .sumOf { it.monto }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    val gastos: StateFlow<Double> = movimientos
        .map { lista ->
            lista.filter { it.tipo == TipoMovimiento.GASTO }
                .sumOf { it.monto }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    val saldo: StateFlow<Double> = movimientos
        .map { lista ->
            lista.sumOf { movimiento ->
                if (movimiento.tipo == TipoMovimiento.INGRESO) {
                    movimiento.monto
                } else {
                    -movimiento.monto
                }
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0.0)

    init {
        viewModelScope.launch {
            repository.obtenerMovimientos()
                .map { lista ->
                    lista.map { entidad ->
                        Movimiento(
                            id = entidad.id,
                            monto = entidad.monto,
                            tipo = TipoMovimiento.valueOf(entidad.tipo),
                            categoria = entidad.categoria,
                            descripcion = entidad.descripcion,
                            fecha = entidad.fecha,
                            imagenUri = entidad.imagenUri
                        )
                    }
                }
                .collect { lista ->
                    _movimientos.value = lista
                }
        }
    }

    fun cambiarMonto(valor: String) {
        _formulario.value = _formulario.value.copy(
            monto = valor,
            errorMonto = null,
            mensaje = null
        )
    }

    fun cambiarTipo(tipo: TipoMovimiento) {
        _formulario.value = _formulario.value.copy(
            tipo = tipo,
            mensaje = null
        )
    }

    fun cambiarCategoria(valor: String) {
        _formulario.value = _formulario.value.copy(
            categoria = valor,
            errorCategoria = null,
            mensaje = null
        )
    }

    fun cambiarDescripcion(valor: String) {
        _formulario.value = _formulario.value.copy(
            descripcion = valor,
            mensaje = null
        )
    }

    fun guardarMovimiento(imagenUri: String? = null) {
        if (_guardando.value) return

        val actual = _formulario.value

        val errorMonto = Validaciones.validarMonto(actual.monto)
        val errorCategoria = Validaciones.validarCategoria(actual.categoria)

        if (errorMonto != null || errorCategoria != null) {
            _formulario.value = actual.copy(
                errorMonto = errorMonto,
                errorCategoria = errorCategoria,
                mensaje = null
            )
            return
        }

        val monto = Validaciones.leerMontoCLP(actual.monto)

        // Protección adicional antes de crear el registro.
        if (monto == null || !monto.isFinite() || monto <= 0) {
            _formulario.value = actual.copy(
                errorMonto = "Ingresa un monto válido mayor que cero.",
                mensaje = null
            )
            return
        }

        val entidad = MovimientoEntity(
            monto = monto,
            tipo = actual.tipo.name,
            categoria = actual.categoria.trim(),
            descripcion = actual.descripcion.trim(),
            fecha = System.currentTimeMillis(),
            imagenUri = imagenUri
        )

        viewModelScope.launch {
            _guardando.value = true

            try {
                repository.insertarMovimiento(entidad)

                _formulario.value = MovimientoFormularioState(
                    mensaje = "¡Movimiento registrado correctamente!"
                )

                _guardadoExitoso.value = true
            } catch (e: Exception) {
                _formulario.value = actual.copy(
                    mensaje = "No se pudo guardar el movimiento. Inténtalo nuevamente."
                )
            } finally {
                _guardando.value = false
            }
        }
    }

    fun consumirGuardadoExitoso() {
        _guardadoExitoso.value = false
    }

    fun eliminarMovimiento(id: Long) {
        viewModelScope.launch {
            try {
                repository.eliminarMovimiento(id)
            } catch (e: Exception) {
                _formulario.value = _formulario.value.copy(
                    mensaje = "No se pudo eliminar el movimiento."
                )
            }
        }
    }
}
