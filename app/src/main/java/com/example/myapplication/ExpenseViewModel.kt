package com.example.myapplication

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.myapplication.Data.ExpenseDatabase
import com.example.myapplication.Data.ExpenseEntity
import kotlinx.coroutines.launch

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {

    private val expenseDao = ExpenseDatabase.getDatabase(application).expenseDao()
    val allExpenses: LiveData<List<ExpenseEntity>> = expenseDao.getAllExpenses()
    val totalAmount: LiveData<Double> = expenseDao.getTotalAmount()

    fun addExpense(name: String, amount: Double, date: String) {
        viewModelScope.launch {
            expenseDao.insertExpense(ExpenseEntity(name = name, amount = amount, date = date))
        }
    }

    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch {
            expenseDao.deleteExpense(expense)
        }
    }
}
