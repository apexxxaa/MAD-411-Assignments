package com.example.myapplication

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExpenseViewModel : ViewModel() {

    private val prExpenses = MutableLiveData<MutableList<Expense>>(mutableListOf())
    val expenses: LiveData<MutableList<Expense>> = prExpenses

    private val prTotal = MutableLiveData<Double> (0.0)
    val total : LiveData<Double> = prTotal

    fun addExpense(expense: Expense){
        prExpenses.value?.add(expense)
        prExpenses.value = prExpenses.value
        calculateTotal()
    }

    fun removeExpense(index: Int) {
        prExpenses.value?.removeAt(index)
        prExpenses.value = prExpenses.value
        calculateTotal()
    }

    private fun calculateTotal() {
        prTotal.value = prExpenses.value?.sumOf { it.amount } ?: 0.0
    }

    fun setExpenses(list: MutableList<Expense>){
        prExpenses.value = list
        calculateTotal()
    }

}