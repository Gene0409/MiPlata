
package com.example.miplata.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.miplata.model.TipoMovimiento
import com.example.miplata.viewmodel.MovimientoViewModel

@Composable
fun MovimientosScreen(viewModel: MovimientoViewModel) {
    val movimientos by viewModel.movimientos.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text("Mis movimientos", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        if (movimientos.isEmpty()) {
            Text("Aún no tienes movimientos registrados.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(movimientos, key = { it.id }) { movimiento ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(16.dp)) {
                            Text(movimiento.categoria, style = MaterialTheme.typography.titleMedium)
                            Text(movimiento.descripcion.ifBlank { movimiento.tipo.name })
                            Text(
                                (if (movimiento.tipo == TipoMovimiento.INGRESO) "+ " else "− ") +
                                        "$ " + movimiento.monto.toLong()
                            )
                        }
                    }
                }
            }
        }
    }
}
