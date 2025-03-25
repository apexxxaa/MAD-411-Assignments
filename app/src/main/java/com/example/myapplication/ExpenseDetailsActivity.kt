package com.example.myapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ExpenseDetailsActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)

        val name = intent.getStringExtra("Expense_name")
        val amount = intent.getDoubleExtra("expense_amount", 0.0)

        val tvExpenseDetails = findViewById<TextView>(R.id.tvExpenseDetails)
        tvExpenseDetails.text = "Expense: $name \n Amount: $amount"


    }
}