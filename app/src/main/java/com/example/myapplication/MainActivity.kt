package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/*
reference: https://developer.android.com/topic/libraries/view-binding?hl=en
: https://developer.android.com/develop/ui/views/layout/recyclerview
 */
class MainActivity : AppCompatActivity() {

    //empty box that will store things from the screen
    private lateinit var etExpenseNameId: EditText
    private lateinit var etEnterAmount: EditText
    private lateinit var btnAddExpense: Button
    private val expenses = mutableListOf<Expense>()
    private lateinit var expenseAdapter: ExpenseAdapter


    private lateinit var recyclerViewExpenses: RecyclerView
    private lateinit var btnFinancialTips: Button
    private lateinit var footerFragment: FooterFragment


    //this will load screen from aactivity.xml
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("ActivityLifeCycle", "OnCreate called")


        /*this will finds the buttons and text fields from xml and connects them to the kotlin
        * SO now i can read what user will type and changes things on the screen
        * */
        etExpenseNameId = findViewById(R.id.etExpenseNameId)
        etEnterAmount = findViewById(R.id.etEnterAmount)
        btnAddExpense = findViewById(R.id.btnAddExpense)
        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses)
        btnFinancialTips = findViewById(R.id.btnFinancialTips)


        recyclerViewExpenses.layoutManager = LinearLayoutManager(this)
        expenseAdapter = ExpenseAdapter(expenses) {updateTotalExpenses()}
        recyclerViewExpenses.adapter = expenseAdapter

        //what happens when i click button
        btnAddExpense.setOnClickListener {
            val name = etExpenseNameId.text.toString()
            val amountText = etEnterAmount.text.toString()

            //checking if user typed anything or not if they did do this if not do nothing
            if (name.isNotEmpty() && amountText.isNotEmpty()) {
                val amount =
                    amountText.toDoubleOrNull() //if i type 10.0 as text convert it to number
                if (amount != null) {

                    val newExpense = Expense(name, amount)
                    expenses.add(newExpense)
                    expenseAdapter.notifyItemInserted(expenses.size - 1)

                    updateTotalExpenses()



                    etExpenseNameId.text.clear()
                    etEnterAmount.text.clear()
                }
            }
        }

        btnFinancialTips.setOnClickListener {

            var url = "https://www.investopedia.com/financial-tips-for-young-adults-11678397"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        if (savedInstanceState == null) {
            val headerFragment = HeaderFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.headerContainer, headerFragment)
                .commit()
        }

        footerFragment = FooterFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.footerContainer, footerFragment)
            .commit()
    }

    fun calculateTotalExpenses(): Double {
        return expenses.sumOf { it.amount }
    }

    fun updateTotalExpenses() {
        val footerFragment = supportFragmentManager.findFragmentById(R.id.footerContainer) as? FooterFragment
        footerFragment?.updateTotalAmount(calculateTotalExpenses())
    }




    override fun onStart(){
        super.onStart()
        Log.d("ActivityLifecycle", "calling onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d("ActivityLifecycle", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        Log.d("ActivityLifecycle", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ActivityLifecycle", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ActivityLifecycle", "onDestroy called")
    }



}
