package com.example.myapplication.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class SystemEventReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BATTERY_LOW -> {
                Toast.makeText(context, "Sync paused due to low battery", Toast.LENGTH_SHORT).show()
            }
            Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                val isAirplaneOn = intent.getBooleanExtra("state", false)
                val message = if (isAirplaneOn)
                    "Airplane Mode Enabled - Sync paused"
                else
                    "Airplane Mode Disabled - Sync resumed"

                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
