package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.gson.Gson


import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.reflect.TypeToken
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStreamWriter

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

    // Method to load expenses from the file
    private fun loadExpensesFromFile() {
        try {
            openFileInput("expenses.json").use { fis ->
                val json = fis.bufferedReader().use { it.readText() }
                val loadedExpenses: List<Expense> = Gson().fromJson(json, object : TypeToken<List<Expense>>() {}.type)
                expenses.clear()
                expenses.addAll(loadedExpenses)
                expenseAdapter.notifyDataSetChanged()
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    // Method to save expenses to the file
    private fun saveExpensesToFile(expenses: List<Expense>) {
        val json = Gson().toJson(expenses) // Convert the expenses list to JSON
        openFileOutput("expenses.json", MODE_PRIVATE).use { fos ->
            fos.write(json.toByteArray())
        }
    }






    //this will load screen from activity.xml
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
        expenseAdapter = ExpenseAdapter(expenses) { position ->
            deleteExpense(position)  // Call the deleteExpense method when the delete button is clicked
        }
        recyclerViewExpenses.adapter = expenseAdapter
        loadExpensesFromFile()

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

                    saveExpensesToFile(expenses)

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


    fun deleteExpense(position: Int) {
        expenses.removeAt(position)

        expenseAdapter.notifyItemRemoved(position)

        saveExpensesToFile(expenses)

        updateTotalExpenses()
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
