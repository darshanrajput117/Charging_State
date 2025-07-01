package com.example.myapplication

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class ChargerMonitorService : Service (){

    companion object
    {
        const val NOTIFICATION_CHANNEL_ID = "ChargerMonitor"
        const val NOTIFICATION_ID = 101
    }

    private var chargingReceiver : ChargerBroadcastReceiver? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(NOTIFICATION_ID, createNotification())

        // Register the BroadcastReceiver dynamically
        chargingReceiver = ChargerBroadcastReceiver()
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        registerReceiver(chargingReceiver, filter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY // Service will be restarted if killed by the system
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the receiver to prevent memory leaks
        chargingReceiver?.let { unregisterReceiver(it) }
    }


    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Charger Monitor Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun createNotification() =
        NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Charger Monitor Service")
            .setContentText("Monitoring charger status in the background.")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your app's icon
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

}