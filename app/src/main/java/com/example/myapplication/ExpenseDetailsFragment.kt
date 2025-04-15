package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class ExpenseDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_expense_details, container, false)


        val name = arguments?.getString("EXPENSE_NAME") ?: "NO NAME"
        val amount = arguments?.getString("EXPENSE_AMOUNT")

        view.findViewById<TextView>(R.id.tvExpenseName).text = name
        view.findViewById<TextView>(R.id.tvExpenseAmount).text = amount

        return view

    }

}
