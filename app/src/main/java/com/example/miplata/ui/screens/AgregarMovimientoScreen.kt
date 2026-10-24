package com.example.miplata.ui.screens

import android.net.Uri
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.core.content.FileProvider
import java.io.File
import java.util.UUID
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.Icon
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.example.miplata.model.TipoMovimiento
import com.example.miplata.viewmodel.MovimientoViewModel

@Composable
fun AgregarMovimientoScreen(
    viewModel: MovimientoViewModel = viewModel(),
    onGuardado: () -> Unit = {}
) {
    val estado by viewModel.formulario.collectAsState()
    val guardadoExitoso by viewModel.guardadoExitoso.collectAsState()
    val guardando by viewModel.guardando.collectAsState()
    val context = LocalContext.current

    var imagenSeleccionadaTexto by rememberSaveable { mutableStateOf<String?>(null) }
    val imagenSeleccionada = imagenSeleccionadaTexto?.let(Uri::parse)
    var uriCamaraPendiente by rememberSaveable { mutableStateOf<String?>(null) }

    var errorImagen by remember {
        mutableStateOf<String?>(null)
    }

    // Navegar solamente después de confirmar el guardado en Room.
    LaunchedEffect(guardadoExitoso) {
        if (guardadoExitoso) {
            viewModel.consumirGuardadoExitoso()
            onGuardado()
        }
    }

    val selectorImagen = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            try {
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                imagenSeleccionadaTexto = uri.toString()
                errorImagen = null
            } catch (_: SecurityException) {
                // Si no se conserva el permiso, no guardamos una URI que fallará al reiniciar.
                imagenSeleccionadaTexto = null
                errorImagen = "No se pudo conservar el permiso. Prueba con otra imagen."
            }
        }
    }

    val camara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { guardada ->
        if (guardada) {
            imagenSeleccionadaTexto = uriCamaraPendiente
            errorImagen = null
        } else {
            errorImagen = "Se canceló la fotografía. Puedes volver a intentarlo."
        }
        uriCamaraPendiente = null
    }

    fun abrirCamara() {
        try {
            val directorio = File(context.filesDir, "imagenes").apply { mkdirs() }
            val archivo = File(directorio, "movimiento_${UUID.randomUUID()}.jpg")
            val uri = FileProvider.getUriForFile(
                context, "${context.packageName}.fileprovider", archivo
            )
            uriCamaraPendiente = uri.toString()
            camara.launch(uri)
        } catch (_: Exception) {
            errorImagen = "No se pudo abrir la cámara en este dispositivo."
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Nuevo movimiento ✨",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Registra el dinero que recibes o gastas para mantener tus cuentas al día.",
            style = MaterialTheme.typography.bodyLarge
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "1. Elige el tipo",
                    style = MaterialTheme.typography.titleMedium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    FilterChip(
                        selected = estado.tipo == TipoMovimiento.GASTO,
                        onClick = {
                            viewModel.cambiarTipo(TipoMovimiento.GASTO)
                        },
                        label = { Text("📉 Gasto") }
                    )

                    FilterChip(
                        selected = estado.tipo == TipoMovimiento.INGRESO,
                        onClick = {
                            viewModel.cambiarTipo(TipoMovimiento.INGRESO)
                        },
                        label = { Text("📈 Ingreso") }
                    )
                }
            }
        }

        Text(
            text = "2. Completa los datos",
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = estado.monto,
            onValueChange = viewModel::cambiarMonto,
            label = { Text("Monto en pesos chilenos") },
            leadingIcon = { Icon(Icons.Outlined.AttachMoney, contentDescription = null) },
            placeholder = { Text("Ej.: 15000") },
            modifier = Modifier.fillMaxWidth(),
            isError = estado.errorMonto != null,
            supportingText = {
                Text(
                    estado.errorMonto ?: "Ingresa un monto mayor que cero."
                )
            },
            singleLine = true
        )

        OutlinedTextField(
            value = estado.categoria,
            onValueChange = viewModel::cambiarCategoria,
            label = { Text("Categoría") },
            leadingIcon = { Icon(Icons.Outlined.Category, contentDescription = null) },
            placeholder = {
                Text("Ej.: Alimentación, sueldo, transporte")
            },
            modifier = Modifier.fillMaxWidth(),
            isError = estado.errorCategoria != null,
            supportingText = {
                estado.errorCategoria?.let { Text(it) }
            },
            singleLine = true
        )

        OutlinedTextField(
            value = estado.descripcion,
            onValueChange = viewModel::cambiarDescripcion,
            label = { Text("Descripción (opcional)") },
            leadingIcon = { Icon(Icons.Outlined.Description, contentDescription = null) },
            placeholder = {
                Text("¿En qué consiste este movimiento?")
            },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2,
            maxLines = 4
        )

        Text(
            text = "3. Añade una imagen (opcional)",
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            OutlinedButton(
                onClick = { selectorImagen.launch(arrayOf("image/*")) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Outlined.PhotoLibrary, contentDescription = null)
                Text(" Galería")
            }
            OutlinedButton(
                onClick = ::abrirCamara,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Outlined.PhotoCamera, contentDescription = null)
                Text(" Cámara")
            }
        }

        errorImagen?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        AnimatedVisibility(
            visible = imagenSeleccionada != null,
            enter = fadeIn() + slideInVertically()
        ) {
            imagenSeleccionada?.let { uri ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Vista previa",
                            style = MaterialTheme.typography.titleSmall
                        )

                        AsyncImage(
                            model = uri,
                            contentDescription = "Vista previa de la imagen",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            contentScale = ContentScale.Crop
                        )

                        OutlinedButton(
                            onClick = {
                                imagenSeleccionadaTexto = null
                                errorImagen = null
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Quitar imagen")
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = estado.mensaje != null,
            enter = fadeIn() + slideInVertically()
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = estado.mensaje.orEmpty(),
                    modifier = Modifier.padding(14.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Button(
            onClick = {
                viewModel.guardarMovimiento(imagenSeleccionada?.toString())
            },
            enabled = !guardando,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                if (guardando) {
                    "Guardando..."
                } else {
                    "Guardar movimiento"
                }
            )
        }

        Text(
            text = "Consejo: registra tus movimientos con regularidad para obtener un resumen financiero más útil.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}