
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

class MetaAhorroViewModel(application: Application) : AndroidViewModel(application) {

    private val dao =
        DatabaseProvider.obtenerBaseDatos(application).metaAhorroDao()

    private val repository = MetaAhorroRepository(dao)

    val metas: StateFlow<List<MetaAhorroEntity>> =
        repository.obtenerMetas().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    private val _guardando = MutableStateFlow(false)
    val guardando: StateFlow<Boolean> = _guardando.asStateFlow()

    fun crearMeta(
        nombre: String,
        montoObjetivo: Double,
        montoAhorrado: Double
    ) {
        if (_guardando.value) return

        val error = Validaciones.validarMeta(
            nombre = nombre,
            objetivo = montoObjetivo.toString()
        )

        if (error != null) {
            _mensaje.value = error
            return
        }

        if (!montoAhorrado.isFinite() || montoAhorrado < 0) {
            _mensaje.value = "El ahorro inicial no puede ser negativo."
            return
        }

        if (montoAhorrado > montoObjetivo) {
            _mensaje.value =
                "El ahorro inicial no puede superar el monto objetivo."
            return
        }

        viewModelScope.launch {
            _guardando.value = true

            try {
                repository.insertarMeta(
                    MetaAhorroEntity(
                        nombre = nombre.trim(),
                        montoObjetivo = montoObjetivo,
                        montoAhorrado = montoAhorrado
                    )
                )
                _mensaje.value = "¡Meta de ahorro creada correctamente!"
            } catch (e: Exception) {
                _mensaje.value = "No se pudo guardar la meta. Inténtalo nuevamente."
            } finally {
                _guardando.value = false
            }
        }
    }

    fun actualizarMeta(meta: MetaAhorroEntity) {
        if (meta.nombre.isBlank()) {
            _mensaje.value = "El nombre de la meta es obligatorio."
            return
        }

        if (!meta.montoObjetivo.isFinite() || meta.montoObjetivo <= 0) {
            _mensaje.value = "El monto objetivo debe ser mayor que cero."
            return
        }

        if (!meta.montoAhorrado.isFinite() ||
            meta.montoAhorrado < 0 ||
            meta.montoAhorrado > meta.montoObjetivo
        ) {
            _mensaje.value =
                "El ahorro debe estar entre cero y el monto objetivo."
            return
        }

        viewModelScope.launch {
            try {
                repository.actualizarMeta(meta.copy(nombre = meta.nombre.trim()))
                _mensaje.value = "Meta actualizada correctamente."
            } catch (e: Exception) {
                _mensaje.value = "No se pudo actualizar la meta."
            }
        }
    }

    fun eliminarMeta(meta: MetaAhorroEntity) {
        viewModelScope.launch {
            try {
                repository.eliminarMeta(meta)
                _mensaje.value = "Meta eliminada correctamente."
            } catch (e: Exception) {
                _mensaje.value = "No se pudo eliminar la meta."
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}
