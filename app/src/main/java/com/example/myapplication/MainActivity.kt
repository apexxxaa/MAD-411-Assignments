package com.example.myapplication



import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button

import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var etExpenseNameId: EditText
    private lateinit var etEnterAmount: EditText
    private lateinit var btnAddExpense: Button
    private lateinit var recyclerView: RecyclerView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


       etExpenseNameId = findViewById(R.id.etExpenseNameId)
       etEnterAmount = findViewById(R.id.etEnterAmount)
       btnAddExpense = findViewById(R.id.btnAddExpense)
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager =  LinearLayoutManager(this)

        btnAddExpense.setOnClickListener{
            val name = etExpenseNameId.text.toString()
            val amountText = etEnterAmount.text.toString()

            if(name.isNotEmpty() && amountText.isNotEmpty()){
                val amount = amountText.toDouble()

            }
        }

    }


}
