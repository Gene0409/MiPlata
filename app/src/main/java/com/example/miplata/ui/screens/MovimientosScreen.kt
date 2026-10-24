package com.example.miplata.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.miplata.model.Movimiento
import com.example.miplata.model.TipoMovimiento
import com.example.miplata.viewmodel.MovimientoViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun MovimientosScreen(viewModel: MovimientoViewModel) {
    val movimientos by viewModel.movimientos.collectAsState()
    var paraEliminar by remember { mutableStateOf<Movimiento?>(null) }
    val moneda = remember { NumberFormat.getCurrencyInstance(Locale("es", "CL")) }

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp)
    ) {
        Text("Mis movimientos", style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(12.dp))

        if (movimientos.isEmpty()) {
            Text("Aún no tienes movimientos registrados.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(movimientos, key = { it.id }) { movimiento ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(movimiento.categoria,
                                style = MaterialTheme.typography.titleMedium)
                            Text(movimiento.descripcion.ifBlank { movimiento.tipo.name })
                            Text(
                                text = (if (movimiento.tipo == TipoMovimiento.INGRESO) "+ " else "− ") +
                                    moneda.format(movimiento.monto),
                                style = MaterialTheme.typography.titleLarge,
                                color = if (movimiento.tipo == TipoMovimiento.INGRESO)
                                    MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                            )
                            movimiento.imagenUri?.let { uri ->
                                AsyncImage(
                                    model = Uri.parse(uri),
                                    contentDescription = "Comprobante de ${movimiento.categoria}",
                                    modifier = Modifier.fillMaxWidth().height(155.dp),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            OutlinedButton(onClick = { paraEliminar = movimiento }) {
                                Text("Eliminar movimiento")
                            }
                        }
                    }
                }
            }
        }
    }

    paraEliminar?.let { movimiento ->
        AlertDialog(
            onDismissRequest = { paraEliminar = null },
            title = { Text("¿Eliminar movimiento?") },
            text = { Text("Eliminarás el registro de «${movimiento.categoria}». Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminarMovimiento(movimiento.id)
                    paraEliminar = null
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { paraEliminar = null }) { Text("Cancelar") }
            }
        )
    }
}
