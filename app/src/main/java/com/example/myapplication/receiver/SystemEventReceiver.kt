package com.example.myapplication.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import com.example.myapplication.SyncStatusManager

class SystemEventReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BATTERY_LOW -> {
                Toast.makeText(context, "Sync paused due to low battery", Toast.LENGTH_SHORT).show()
                SyncStatusManager.updateSyncStatus(false)
            }
            Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                val isAirplaneOn = isAirplaneModeOn(context)
                val message = if (isAirplaneOn)
                    "Airplane Mode Enabled - Sync paused"
                else
                    "Airplane Mode Disabled - Sync resumed"

                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                SyncStatusManager.updateSyncStatus(!isAirplaneOn)
            }
        }
    }

    private fun isAirplaneModeOn(context: Context): Boolean {
        return Settings.Global.getInt(
            context.contentResolver,
            Settings.Global.AIRPLANE_MODE_ON, 0
        ) != 0
    }
}
