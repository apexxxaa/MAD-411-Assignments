package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.services.OverdueCheckService
import com.google.gson.Gson
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.Calendar

private const val FILE_NAME = "expenses.txt"

class MainFragment : Fragment() {

    private lateinit var etExpenseNameId: EditText
    private lateinit var etEnterAmount: EditText
    private lateinit var btnAddExpense: Button
    private lateinit var recyclerViewExpenses: RecyclerView
    private lateinit var btnFinancialTips: Button

    private val expenses = mutableListOf<Expense>()
    private lateinit var expenseAdapter: ExpenseAdapter
    private var selectedDate: String? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        etExpenseNameId = view.findViewById(R.id.etExpenseNameId)
        etEnterAmount = view.findViewById(R.id.etEnterAmount)
        btnAddExpense = view.findViewById(R.id.btnAddExpense)
        recyclerViewExpenses = view.findViewById(R.id.recyclerViewExpenses)
        btnFinancialTips = view.findViewById(R.id.btnFinancialTips)

        // Load expenses from file
        expenses.addAll(loadExpensesFromFile(requireContext()))

        // Setup RecyclerView
        recyclerViewExpenses.layoutManager = LinearLayoutManager(requireContext())
        expenseAdapter = ExpenseAdapter(expenses) {
            updateTotalExpenses()
        }
        recyclerViewExpenses.adapter = expenseAdapter

        // Add expense button click listener
        btnAddExpense.setOnClickListener {
            val name = etExpenseNameId.text.toString()
            val amountText = etEnterAmount.text.toString()

            if (name.isNotEmpty() && amountText.isNotEmpty()) {
                val amount = amountText.toDoubleOrNull()
                if (amount != null) {
                    val newExpense = Expense(name, amount, selectedDate ?: "No date")
                    expenses.add(newExpense)
                    expenseAdapter.notifyItemInserted(expenses.size - 1)
                    updateTotalExpenses()
                    saveExpensesToFile(requireContext(), expenses)

                    etExpenseNameId.text.clear()
                    etEnterAmount.text.clear()
                    selectedDate = null
                } else {
                    Toast.makeText(requireContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Financial tips button
        btnFinancialTips.setOnClickListener {
            val url = "https://www.investopedia.com/financial-tips-for-young-adults-11678397"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        // Load FooterFragment into the footer container if not already loaded
        if (childFragmentManager.findFragmentById(R.id.footerContainer) == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.footerContainer, FooterFragment())
                .commit()
        }

        if (childFragmentManager.findFragmentById(R.id.headerContainer) == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.headerContainer, HeaderFragment())
                .commit()
        }

        //date picker
        val selectDateButton = view.findViewById<Button>(R.id.btnSelectDate)

        selectDateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDateStr = "$selectedYear-${selectedMonth + 1}-$selectedDay"
                selectDateButton.text = selectedDateStr
                selectedDate = selectedDateStr
            }, year, month, day)

            datePickerDialog.show()
        }
//Trigger the service on creation AFTER the view is created

        val serviceIntent = Intent(requireContext(), OverdueCheckService::class.java)
        ContextCompat.startForegroundService(requireContext(), serviceIntent)



    }

    private fun saveExpensesToFile(context: Context, expenseList: List<Expense>) {
        try {
            val json = Gson().toJson(expenseList)
            context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE).use { output ->
                output.write(json.toByteArray())
            }
            Log.d("FileStorage", "Expenses saved successfully")
        } catch (e: IOException) {
            Log.e("FileStorage", "Error saving expenses: ${e.message}")
        }
    }

    private fun loadExpensesFromFile(context: Context): List<Expense> {
        val expenseList = mutableListOf<Expense>()
        try {
            val file = File(context.filesDir, FILE_NAME)
            if (!file.exists()) return expenseList

            val json = file.readText()
            val type = object : com.google.gson.reflect.TypeToken<List<Expense>>() {}.type
            val loadedExpenses: List<Expense> = Gson().fromJson(json, type)
            expenseList.addAll(loadedExpenses)

            Log.d("FileStorage", "Expenses loaded successfully")
        } catch (e: FileNotFoundException) {
            Log.e("FileStorage", "File not found: ${e.message}")
        } catch (e: IOException) {
            Log.e("FileStorage", "Error reading file: ${e.message}")
        }
        return expenseList



    }

    private fun updateTotalExpenses() {
        val footerFragment =
            childFragmentManager.findFragmentById(R.id.footerContainer) as? FooterFragment
        footerFragment?.updateTotalAmount(expenses.sumOf { it.amount })
    }

}
