package com.example.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ExpenseDetailsActivity

class ExpenseAdapter(
    private val onDeleteClicked: (Expense) -> Unit
) : ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(ExpenseDiffCallback()) {

    inner class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val expenseNameTextView: TextView = itemView.findViewById(R.id.expenseNameTextView)
        val expenseAmountTextView: TextView = itemView.findViewById(R.id.expenseAmountTextView)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        val btnShowDetails: Button = itemView.findViewById(R.id.btnShowDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.expense_item, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = getItem(position)

        holder.expenseNameTextView.text = expense.name
        holder.expenseAmountTextView.text = "$${expense.amount}"

        holder.deleteButton.setOnClickListener {
            holder.itemView.animate()
                .alpha(0f)
                .translationX(holder.itemView.width.toFloat())
                .setDuration(300)
                .withEndAction {
                    onDeleteClicked(expense)
                }
                .start()
        }


        holder.btnShowDetails.setOnClickListener {
            val isVisible = holder.expenseAmountTextView.visibility == View.VISIBLE
            if (isVisible) {
                holder.expenseAmountTextView.animate().alpha(0f).setDuration(300).withEndAction {
                    holder.expenseAmountTextView.visibility = View.GONE
                }.start()
                holder.btnShowDetails.text = "Show Details"
            } else {
                holder.expenseAmountTextView.visibility = View.VISIBLE
                holder.expenseAmountTextView.alpha = 0f
                holder.expenseAmountTextView.animate().alpha(1f).setDuration(300).start()
                holder.btnShowDetails.text = "Hide Details"
            }
        }

    }
}
