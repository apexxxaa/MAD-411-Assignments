package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.ExpenseViewModel
import com.example.myapplication.R

class FooterFragment : Fragment() {

    private var totalTextView: TextView? = null
    private var viewModel: ExpenseViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.footer_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        totalTextView = view.findViewById(R.id.footerTextView)

        viewModel = ViewModelProvider(requireActivity())[ExpenseViewModel::class.java]


    }


}
