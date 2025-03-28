package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class FooterFragment : Fragment() {

    private lateinit var totalAmountTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.footer_fragment, container, false)
        totalAmountTextView = view.findViewById(R.id.tvTotalAmount)
        return view


    }

    fun updateTotalAmount(total: Double) {
        view?.findViewById<TextView>(R.id.tvTotalAmount)?.text = "Total Expenses: $$total"
    }



}