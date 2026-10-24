
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
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Savings
import androidx.compose.material.icons.outlined.Edit
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

    val formulario by viewModel.formulario.collectAsState()
    val guardando by viewModel.guardando.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val errorActualizacion by viewModel.errorActualizacion.collectAsState()

    var metaParaEliminar by remember {
        mutableStateOf<MetaAhorroEntity?>(null)
    }

    var metaParaActualizar by remember {
        mutableStateOf<MetaAhorroEntity?>(null)
    }

    var nuevoAhorro by remember { mutableStateOf("") }

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
            value = formulario.nombre,
            onValueChange = viewModel::cambiarNombre,
            label = { Text("¿Para qué estás ahorrando?") },
            placeholder = { Text("Ej.: Viaje, notebook, emergencia") },
            leadingIcon = { Icon(Icons.Outlined.Flag, contentDescription = null) },
            isError = formulario.errorNombre != null,
            supportingText = { formulario.errorNombre?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = formulario.objetivo,
            onValueChange = viewModel::cambiarObjetivo,
            label = { Text("Monto objetivo (CLP)") },
            placeholder = { Text("Ej.: 300000") },
            leadingIcon = { Icon(Icons.Outlined.Savings, contentDescription = null) },
            isError = formulario.errorObjetivo != null,
            supportingText = { formulario.errorObjetivo?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = formulario.ahorro,
            onValueChange = viewModel::cambiarAhorro,
            label = { Text("¿Cuánto tienes ahorrado? (CLP)") },
            placeholder = { Text("Ej.: 50000") },
            leadingIcon = { Icon(Icons.Outlined.Savings, contentDescription = null) },
            isError = formulario.errorAhorro != null,
            supportingText = { formulario.errorAhorro?.let { Text(it) } },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        mensaje?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Button(
            onClick = viewModel::crearMeta,
            enabled = !guardando,
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(if (guardando) "Guardando..." else "Guardar meta")
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
                                    viewModel.limpiarErrorActualizacion()
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
                            viewModel.limpiarErrorActualizacion()
                        },
                        label = { Text("Ahorro actual (CLP)") },
                        placeholder = { Text("Ej.: 70000") },
                        singleLine = true,
                        leadingIcon = { Icon(Icons.Outlined.Edit, contentDescription = null) },
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
                        if (viewModel.actualizarMeta(meta, nuevoAhorro)) {
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
