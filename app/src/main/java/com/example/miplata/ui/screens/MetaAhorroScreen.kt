
package com.example.miplata.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.miplata.data.local.MetaAhorroEntity
import com.example.miplata.viewmodel.MetaAhorroViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MetaAhorroScreen(
    viewModel: MetaAhorroViewModel = viewModel()
) {
    val metas by viewModel.metas.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var objetivo by remember { mutableStateOf("") }
    var ahorro by remember { mutableStateOf("0") }
    var error by remember { mutableStateOf<String?>(null) }

    var metaParaEliminar by remember {
        mutableStateOf<MetaAhorroEntity?>(null)
    }

    var metaParaActualizar by remember {
        mutableStateOf<MetaAhorroEntity?>(null)
    }

    var nuevoAhorro by remember { mutableStateOf("") }
    var errorActualizacion by remember { mutableStateOf<String?>(null) }

    val moneda = remember {
        NumberFormat.getCurrencyInstance(Locale("es", "CL"))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Mis metas 🎯",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Dale un propósito a tu ahorro y avanza paso a paso.",
            style = MaterialTheme.typography.bodyLarge
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "💡 Consejo para tu meta",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Elige un objetivo concreto y un monto que puedas ahorrar de manera realista."
                )
            }
        }

        Text(
            text = "Crear una meta",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                error = null
            },
            label = { Text("¿Para qué estás ahorrando?") },
            placeholder = { Text("Ej.: Viaje, notebook, emergencia") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = objetivo,
            onValueChange = {
                objetivo = it
                error = null
            },
            label = { Text("Monto objetivo (CLP)") },
            placeholder = { Text("Ej.: 300000") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = ahorro,
            onValueChange = {
                ahorro = it
                error = null
            },
            label = { Text("¿Cuánto tienes ahorrado? (CLP)") },
            placeholder = { Text("Ej.: 50000") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        if (error != null) {
            Text(
                text = error.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = {
                val montoObjetivo = objetivo.trim()
                    .replace(" ", "")
                    .replace(".", "")
                    .replace(",", ".")
                    .toDoubleOrNull()

                val montoAhorrado = ahorro.trim()
                    .replace(" ", "")
                    .replace(".", "")
                    .replace(",", ".")
                    .toDoubleOrNull()

                error = when {
                    nombre.isBlank() ->
                        "Escribe un nombre para tu meta."

                    montoObjetivo == null ||
                            !montoObjetivo.isFinite() ||
                            montoObjetivo <= 0 ->
                        "El objetivo debe ser mayor que cero."

                    montoAhorrado == null ||
                            !montoAhorrado.isFinite() ||
                            montoAhorrado < 0 ->
                        "El ahorro debe ser cero o mayor."

                    montoAhorrado > montoObjetivo ->
                        "El ahorro no puede superar el objetivo."

                    else -> null
                }

                if (error == null) {
                    viewModel.crearMeta(
                        nombre = nombre.trim(),
                        montoObjetivo = montoObjetivo!!,
                        montoAhorrado = montoAhorrado!!
                    )

                    nombre = ""
                    objetivo = ""
                    ahorro = "0"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Guardar meta")
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Tus metas guardadas",
            style = MaterialTheme.typography.titleLarge
        )

        if (metas.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("🌱 Aún no tienes metas")
                    Text(
                        "Crea tu primera meta y aparecerá aquí. Se guardará en tu dispositivo."
                    )
                }
            }
        } else {
            metas.forEach { meta ->
                val progreso = (
                        meta.montoAhorrado / meta.montoObjetivo
                        ).toFloat().coerceIn(0f, 1f)

                val pendiente = (
                        meta.montoObjetivo - meta.montoAhorrado
                        ).coerceAtLeast(0.0)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "🌱 Tu meta",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = meta.nombre,
                            style = MaterialTheme.typography.headlineSmall
                        )

                        Text(
                            text = moneda.format(meta.montoAhorrado),
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "de ${moneda.format(meta.montoObjetivo)}"
                        )

                        LinearProgressIndicator(
                            progress = { progreso },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp),
                            color = MaterialTheme.colorScheme.primary,
                            trackColor = MaterialTheme.colorScheme.primaryContainer
                        )

                        Text(
                            text = "${(progreso * 100).toInt()}% completado",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = if (pendiente == 0.0) {
                                "🎉 ¡Meta alcanzada! ¡Buen trabajo!"
                            } else {
                                "Te faltan ${moneda.format(pendiente)} para alcanzar tu meta."
                            }
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = {
                                    metaParaActualizar = meta
                                    nuevoAhorro = meta.montoAhorrado.toLong().toString()
                                    errorActualizacion = null
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Actualizar ahorro")
                            }

                            OutlinedButton(
                                onClick = {
                                    metaParaEliminar = meta
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }

    metaParaActualizar?.let { meta ->
        AlertDialog(
            onDismissRequest = {
                metaParaActualizar = null
            },
            title = {
                Text("Actualizar ahorro")
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Meta: ${meta.nombre}")

                    OutlinedTextField(
                        value = nuevoAhorro,
                        onValueChange = {
                            nuevoAhorro = it
                            errorActualizacion = null
                        },
                        label = { Text("Ahorro actual (CLP)") },
                        placeholder = { Text("Ej.: 70000") },
                        singleLine = true,
                        isError = errorActualizacion != null
                    )

                    if (errorActualizacion != null) {
                        Text(
                            text = errorActualizacion.orEmpty(),
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val monto = nuevoAhorro.trim()
                            .replace(" ", "")
                            .replace(".", "")
                            .replace(",", ".")
                            .toDoubleOrNull()

                        errorActualizacion = when {
                            monto == null || !monto.isFinite() ->
                                "Ingresa un monto válido."

                            monto < 0 ->
                                "El ahorro no puede ser negativo."

                            monto > meta.montoObjetivo ->
                                "No puede superar el objetivo."

                            else -> null
                        }

                        if (errorActualizacion == null) {
                            viewModel.actualizarMeta(
                                meta.copy(montoAhorrado = monto!!)
                            )
                            metaParaActualizar = null
                        }
                    }
                ) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        metaParaActualizar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

    metaParaEliminar?.let { meta ->
        AlertDialog(
            onDismissRequest = {
                metaParaEliminar = null
            },
            title = {
                Text("¿Eliminar esta meta?")
            },
            text = {
                Text("Se eliminará la meta «${meta.nombre}» de forma permanente.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.eliminarMeta(meta)
                        metaParaEliminar = null
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        metaParaEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
