package com.example.miplata.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.miplata.nativos.Notificaciones

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.miplata.model.TipoMovimiento
import com.example.miplata.viewmodel.MovimientoViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun InicioScreen(
    viewModel: MovimientoViewModel,
    onAgregar: () -> Unit,
    onVerMovimientos: () -> Unit
) {
    val movimientos by viewModel.movimientos.collectAsState()
    val contexto = LocalContext.current
    var estadoAviso by remember { mutableStateOf<String?>(null) }
    val solicitarPermiso = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        estadoAviso = if (concedido && Notificaciones.mostrarRecordatorio(contexto)) {
            "Recordatorio enviado. Revisa las notificaciones del dispositivo."
        } else {
            "Permiso de notificaciones no concedido. Puedes habilitarlo en Ajustes."
        }
    }
    fun enviarAviso() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            !Notificaciones.tienePermiso(contexto)
        ) {
            solicitarPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
        } else {
            estadoAviso = if (Notificaciones.mostrarRecordatorio(contexto)) {
                "Recordatorio enviado. Revisa las notificaciones del dispositivo."
            } else {
                "No se pudo enviar el aviso. Comprueba los permisos del dispositivo."
            }
        }
    }

    val ingresos = movimientos
        .filter { it.tipo == TipoMovimiento.INGRESO }
        .sumOf { it.monto }

    val gastos = movimientos
        .filter { it.tipo == TipoMovimiento.GASTO }
        .sumOf { it.monto }

    val saldo = ingresos - gastos

    val moneda = NumberFormat.getCurrencyInstance(
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
            text = "¡Hola! 👋",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "Bienvenido a MiPlata",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "Entiende tu dinero, organiza tus gastos y avanza hacia tus metas.",
            style = MaterialTheme.typography.bodyMedium
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
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "💚 SALDO REGISTRADO",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = moneda.format(saldo),
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )

                Text(
                    text = "Ingresos menos gastos registrados",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("📈 Ingresos")
                    Text(
                        text = moneda.format(ingresos),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("📉 Gastos")
                    Text(
                        text = moneda.format(gastos),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }

        Button(
            onClick = onAgregar,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("＋ Registrar movimiento")
        }

        OutlinedButton(
            onClick = onVerMovimientos,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Ver mis movimientos")
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
                    text = "💡 Consejo financiero",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "Registra tus gastos pequeños. Sumados al final del mes, pueden representar una parte importante de tu presupuesto."
                )
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("🔔 Recordatorio financiero", style = MaterialTheme.typography.titleMedium)
                Text("Prueba un aviso del dispositivo para recordar revisar tus finanzas.")
                OutlinedButton(onClick = ::enviarAviso) {
                    Text("Enviar recordatorio")
                }
                estadoAviso?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}