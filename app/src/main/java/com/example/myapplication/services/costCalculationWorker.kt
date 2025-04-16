package com.example.myapplication.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.myapplication.Expense
import com.example.myapplication.R
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class costCalculationWorker (
    private val context: Context,
    workerParams : WorkerParameters
    ) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val expenses = loadExpensesFromFile()
        val totalCost = expenses.sumOf { it.amount }

        showNotification("Weekly Summary", "Total Expenses: $$totalCost")
        Log.d("ExpenseCostWorker", "Weekly Total: $totalCost")

        return Result.success()

    }

    private fun loadExpensesFromFile(): List<Expense> {
        val file = File(context.filesDir, "expenses.txt")
        if (!file.exists()) return emptyList()
        val json = file.readText()
        val type = object : TypeToken<List<Expense>>() {}.type
        return Gson().fromJson(json, type)
    }

    private fun showNotification(title: String, message: String) {
        val channelId = "weekly_expense_channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Weekly Expense Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_notification)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, notification)
    }
}