package com.example.miplata.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.miplata.Validaciones
import com.example.miplata.data.local.DatabaseProvider
import com.example.miplata.data.local.MetaAhorroEntity
import com.example.miplata.data.repository.MetaAhorroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class MetaFormularioState(
    val nombre: String = "",
    val objetivo: String = "",
    val ahorro: String = "0",
    val errorNombre: String? = null,
    val errorObjetivo: String? = null,
    val errorAhorro: String? = null
)

class MetaAhorroViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = MetaAhorroRepository(
        DatabaseProvider.obtenerBaseDatos(application).metaAhorroDao()
    )

    val metas: StateFlow<List<MetaAhorroEntity>> =
        repository.obtenerMetas().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _formulario = MutableStateFlow(MetaFormularioState())
    val formulario: StateFlow<MetaFormularioState> = _formulario.asStateFlow()

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _guardando = MutableStateFlow(false)
    val guardando: StateFlow<Boolean> = _guardando.asStateFlow()

    private val _errorActualizacion = MutableStateFlow<String?>(null)
    val errorActualizacion: StateFlow<String?> = _errorActualizacion.asStateFlow()

    fun cambiarNombre(valor: String) {
        _formulario.value = _formulario.value.copy(nombre = valor, errorNombre = null)
        _mensaje.value = null
    }

    fun cambiarObjetivo(valor: String) {
        _formulario.value = _formulario.value.copy(
            objetivo = valor, errorObjetivo = null, errorAhorro = null
        )
        _mensaje.value = null
    }

    fun cambiarAhorro(valor: String) {
        _formulario.value = _formulario.value.copy(ahorro = valor, errorAhorro = null)
        _mensaje.value = null
    }

    fun crearMeta() {
        if (_guardando.value) return
        val actual = _formulario.value
        val errorNombre = Validaciones.validarNombreMeta(actual.nombre)
        val errorObjetivo = Validaciones.validarObjetivoMeta(actual.objetivo)
        val errorAhorro = Validaciones.validarAhorroMeta(actual.ahorro, actual.objetivo)
        if (errorNombre != null || errorObjetivo != null || errorAhorro != null) {
            _formulario.value = actual.copy(
                errorNombre = errorNombre,
                errorObjetivo = errorObjetivo,
                errorAhorro = errorAhorro
            )
            return
        }

        val objetivo = Validaciones.leerMontoCLP(actual.objetivo) ?: return
        val ahorro = Validaciones.leerMontoCLP(actual.ahorro) ?: return
        viewModelScope.launch {
            _guardando.value = true
            try {
                repository.insertarMeta(
                    MetaAhorroEntity(
                        nombre = actual.nombre.trim(),
                        montoObjetivo = objetivo,
                        montoAhorrado = ahorro
                    )
                )
                _formulario.value = MetaFormularioState()
                _mensaje.value = "¡Meta de ahorro creada correctamente!"
            } catch (_: Exception) {
                _mensaje.value = "No se pudo guardar la meta. Inténtalo nuevamente."
            } finally {
                _guardando.value = false
            }
        }
    }

    /** Retorna false y conserva el diálogo cuando la entrada es inválida. */
    fun actualizarMeta(meta: MetaAhorroEntity, textoAhorro: String): Boolean {
        val error = Validaciones.validarActualizacionAhorro(textoAhorro, meta.montoObjetivo)
        _errorActualizacion.value = error
        if (error != null) return false
        val monto = Validaciones.leerMontoCLP(textoAhorro) ?: return false
        viewModelScope.launch {
            try {
                repository.actualizarMeta(meta.copy(montoAhorrado = monto))
                _mensaje.value = "Meta actualizada correctamente."
            } catch (_: Exception) {
                _mensaje.value = "No se pudo actualizar la meta."
            }
        }
        return true
    }

    fun limpiarErrorActualizacion() { _errorActualizacion.value = null }

    fun eliminarMeta(meta: MetaAhorroEntity) {
        viewModelScope.launch {
            try {
                repository.eliminarMeta(meta)
                _mensaje.value = "Meta eliminada correctamente."
            } catch (_: Exception) {
                _mensaje.value = "No se pudo eliminar la meta."
            }
        }
    }

    fun limpiarMensaje() { _mensaje.value = null }
}
