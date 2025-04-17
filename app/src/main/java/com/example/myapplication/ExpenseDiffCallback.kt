package com.example.myapplication

import androidx.recyclerview.widget.DiffUtil

class ExpenseDiffCallback : DiffUtil.ItemCallback<Expense>() {
    override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
        return oldItem.name == newItem.name && oldItem.date == newItem.date
    }

    override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {

        return oldItem == newItem
    }
}
