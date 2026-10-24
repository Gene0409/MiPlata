package com.example.miplata.nativos

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.miplata.MainActivity

/** Segundo recurso nativo: notificaciones del sistema con consentimiento (Android 13+). */
object Notificaciones {
    private const val CANAL = "recordatorios_financieros"

    fun tienePermiso(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

    fun mostrarRecordatorio(context: Context): Boolean {
        if (!tienePermiso(context)) return false

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(
                    CANAL,
                    "Recordatorios de MiPlata",
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply { description = "Avisos voluntarios para revisar tus finanzas" }
            )
        }

        val abrirApp = PendingIntent.getActivity(
            context,
            0,
            Intent(context, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificacion = NotificationCompat.Builder(context, CANAL)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("MiPlata · Recordatorio")
            .setContentText("Revisa tus ingresos, gastos y metas de ahorro 💚")
            .setContentIntent(abrirApp)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        return try {
            NotificationManagerCompat.from(context).notify(1001, notificacion)
            true
        } catch (_: SecurityException) {
            false
        }
    }
}
