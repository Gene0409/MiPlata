package com.example.miplata.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.miplata.model.TipoMovimiento
import com.example.miplata.viewmodel.MovimientoViewModel
import java.text.NumberFormat
import java.text.NumberFormat.getCurrencyInstance
import java.util.Locale

@Composable
fun ResumenFinancieroScreen(
    viewModel: MovimientoViewModel
) {
    val movimientos by viewModel.movimientos.collectAsState()

    val ingresos = movimientos
        .filter { it.tipo == TipoMovimiento.INGRESO }
        .sumOf { it.monto }

    val gastos = movimientos
        .filter { it.tipo == TipoMovimiento.GASTO }
        .sumOf { it.monto }

    val saldo = ingresos - gastos

    val porcentajeGastos = if (ingresos > 0) {
        (gastos / ingresos).toFloat().coerceIn(0f, 1f)
    } else {
        0f
    }

    val moneda = getCurrencyInstance(
        Locale("es", "CL")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Tu resumen financiero 💰",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Revisa cómo se está moviendo tu dinero.",
            style = MaterialTheme.typography.bodyLarge
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "SALDO ACTUAL",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.labelLarge
                )

                Text(
                    text = moneda.format(saldo),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineLarge
                )

                Text(
                    text = "Ingresos − gastos",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        TarjetaResumen(
            emoji = "📈",
            titulo = "Total de ingresos",
            monto = moneda.format(ingresos),
            explicacion = "Dinero que has registrado como recibido.",
            color = MaterialTheme.colorScheme.primary
        )

        TarjetaResumen(
            emoji = "📉",
            titulo = "Total de gastos",
            monto = moneda.format(gastos),
            explicacion = "Dinero que has registrado como gastado.",
            color = MaterialTheme.colorScheme.error
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Análisis de gastos",
                    style = MaterialTheme.typography.titleLarge
                )

                if (ingresos > 0) {
                    Text(
                        text = "Has registrado gastos equivalentes al " +
                                "${(gastos / ingresos * 100).toInt()}% de tus ingresos."
                    )

                    LinearProgressIndicator(
                        progress = { porcentajeGastos },
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.error
                    )

                    Text(
                        text = if (gastos > ingresos) {
                            "Atención: tus gastos superan tus ingresos registrados. Revisa tu presupuesto."
                        } else {
                            "Puedes revisar tus gastos y decidir cuánto quieres reservar para ahorrar."
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    Text(
                        text = "Registra un ingreso para comenzar a comparar tus gastos con el dinero recibido."
                    )
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "🌱 Aprende a ahorrar",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Una buena práctica es registrar tus ingresos, identificar tus gastos necesarios y fijar una meta de ahorro realista."
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun TarjetaResumen(
    emoji: String,
    titulo: String,
    monto: String,
    explicacion: String,
    color: androidx.compose.ui.graphics.Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "$emoji $titulo",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = monto,
                style = MaterialTheme.typography.headlineSmall,
                color = color
            )

            Text(
                text = explicacion,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}