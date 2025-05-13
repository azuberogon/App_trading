package com.example.app_trading.kotlin.InicioSesion

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.app_trading.R

/**
 * Worker responsable de crear y mostrar una notificación.
 */
class NotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        // Lógica para mostrar la notificación
        showNotification()
        return Result.success()
    }

    /**
     * Crea y muestra una notificación simple.
     */
    private fun showNotification() {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "my_notification_channel_id"
        val notificationId = 1

        // Crear un canal de notificación para Android O y superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Trading App Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for regular trading app notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Construir la notificación
        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.mipmap.ic_logo_foreground) // Reemplaza con el ícono de tu notificación
            .setContentTitle("Notificación de Trading App")
            .setContentText("¡No olvides revisar tus inversiones!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // La notificación se cierra al tocarla

        // Mostrar la notificación
        notificationManager.notify(notificationId, builder.build())
    }
}