package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class ChargerBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        Log.d("--jk--", "Action received: $action")

        when (action) {
            Intent.ACTION_POWER_CONNECTED -> {
                Log.d("--jk--", "Charger Connected!")
                // Start your activity
                val chargerIntent = Intent(context, ChargingActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                }
                context?.startActivity(chargerIntent)
            }
            Intent.ACTION_POWER_DISCONNECTED -> {
                Log.d("--jk--", "Charger Disconnected!")
                // Close your activity if it's open. This is tricky.
                // The best way is to send a broadcast to your activity to finish itself.
                // Or, if your activity is always launched in singleTop mode,
                // you might just rely on the user closing it or the system.
                // For a more robust solution, you'll need to use local broadcasts or events.

                // Option 1: Send a local broadcast to the activity to finish
                val finishIntent = Intent("ACTION_FINISH_CHARGER_ACTIVITY")
                context?.sendBroadcast(finishIntent)

                // Option 2 (Less ideal for automatic closing): If the activity is designed
                // to be very transient, you might not need to explicitly close it from here
                // as the user might close it manually or the system handles it.
            }
        }
    }
}