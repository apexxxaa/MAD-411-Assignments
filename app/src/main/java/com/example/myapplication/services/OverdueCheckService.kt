package com.example.myapplication.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.myapplication.Expense
import com.example.myapplication.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OverdueCheckService : Service() {
    private val channelId = "OverdueExpenseChannel"

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        startForeground(1, buildInitialNotification())

        // Check for overdue expenses right away
        checkForOverdueExpenses()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    private fun checkForOverdueExpenses() {
        val expenses = loadExpensesFromFile()
        val today = Date()

        val overdueCount = expenses.count {
            parseDate(it.date)?.before(today) == true
        }

        if (overdueCount > 0) {
            val notification = NotificationCompat.Builder(this, channelId)
                .setContentTitle("Overdue Expenses")
                .setContentText("You have $overdueCount overdue expense(s).")
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build()

            val manager = getSystemService(NotificationManager::class.java)
            manager.notify(2, notification)
        }
    }

    private fun buildInitialNotification(): Notification {
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Expense Tracker")
            .setContentText("Monitoring for overdue expenses...")
            .setSmallIcon(R.drawable.ic_notification)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Overdue Expense Alerts",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }

    private fun loadExpensesFromFile(): List<Expense> {
        val file = File(filesDir, "expenses.txt")
        if (!file.exists()) return emptyList()

        val json = file.readText()
        val type = object : TypeToken<List<Expense>>() {}.type
        return Gson().fromJson(json, type)
    }

    private fun parseDate(dateStr: String?): Date? {
        return try {
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(dateStr ?: "")
        } catch (e: Exception) {
            null
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
