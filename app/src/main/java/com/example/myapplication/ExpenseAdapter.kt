package com.example.myapplication

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter (private val expenses: MutableList<Expense>): RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val expenseNameTextView: TextView = itemView.findViewById(R.id.expenseNameTextView)
        val expenseAmountTextView: TextView = itemView.findViewById(R.id.expenseAmountTextView)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        val btnShowDetails: Button = itemView.findViewById(R.id.btnShowDetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ExpenseViewHolder{
        val view = LayoutInflater.from(parent.context).inflate(R.layout.expense_item, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int){
        val expense = expenses[position]
        holder.expenseNameTextView.text = expense.name
        holder.expenseAmountTextView.text = "$${expense.amount}"


        holder.deleteButton.setOnClickListener{
            expenses.removeAt(position)
            notifyItemRemoved(position)
        }

        holder.itemView.setOnClickListener{
            val context = holder.itemView.context
            val intent = Intent(context, ExpenseDetailsActivity::class.java)
            intent.putExtra("Expense_name", expense.name)
            intent.putExtra("Expense_Amount", expense.amount)
            context.startActivity(intent)
        }
        holder.btnShowDetails.setOnClickListener{
            val context = holder.itemView.context
            val intent = Intent(context, ExpenseDetailsActivity::class.java)
            intent.putExtra("Expense_name", expense.name)
            intent.putExtra("Expense_Amount", expense.amount)
            context.startActivity(intent)
        }


    }


    override fun getItemCount(): Int = expenses.size

}