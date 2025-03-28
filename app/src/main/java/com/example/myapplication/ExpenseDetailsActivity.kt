package com.example.myapplication

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class ExpenseDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_details)

        val name = intent.getStringExtra("EXPENSE_NAME") ?: "Please add your expense name"
        val amount = intent.getDoubleExtra("EXPENSE_AMOUNT", 0.0)

        findViewById<TextView>(R.id.tvExpenseName).text = "Expense Name: $name"
        findViewById<TextView>(R.id.tvExpenseAmount).text = "Expense Amount: $%.2f".format(amount)
    }
}
