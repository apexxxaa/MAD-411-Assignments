package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.myapplication.Data.ExpenseEntity
import com.example.myapplication.fragments.FooterFragment
import com.example.myapplication.fragments.HeaderFragment
import com.example.myapplication.services.OverdueCheckService
import com.example.myapplication.services.costCalculationWorker
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainFragment : Fragment() {

    private lateinit var viewModel: ExpenseViewModel
    private lateinit var expenseAdapter: ExpenseAdapter

    private lateinit var etExpenseNameId: EditText
    private lateinit var etEnterAmount: EditText
    private lateinit var btnAddExpense: Button
    private lateinit var btnFinancialTips: Button
    private lateinit var recyclerViewExpenses: RecyclerView
    private var syncStatusText: TextView? = null
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

        viewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]

        etExpenseNameId = view.findViewById(R.id.etExpenseNameId)
        etEnterAmount = view.findViewById(R.id.etEnterAmount)
        btnAddExpense = view.findViewById(R.id.btnAddExpense)
        btnFinancialTips = view.findViewById(R.id.btnFinancialTips)
        recyclerViewExpenses = view.findViewById(R.id.recyclerViewExpenses)
        syncStatusText = view.findViewById(R.id.syncStatusText)

        expenseAdapter = ExpenseAdapter { expenseToDelete ->

            viewModel.deleteExpense(
                ExpenseEntity(
                    id = expenseToDelete.id,
                    name = expenseToDelete.name,
                    amount = expenseToDelete.amount,
                    date = expenseToDelete.date
                )
            )

            val currentList = expenseAdapter.currentList.toMutableList()
            currentList.remove(expenseToDelete)
            expenseAdapter.submitList(currentList)
        }


        recyclerViewExpenses.layoutManager = LinearLayoutManager(requireContext())
        recyclerViewExpenses.adapter = expenseAdapter

        val animator = DefaultItemAnimator().apply {
            addDuration = 300
            removeDuration = 300
            moveDuration = 300
            changeDuration = 300
        }
        recyclerViewExpenses.itemAnimator = animator

        // Observe database changes
        viewModel.allExpenses.observe(viewLifecycleOwner) { expenses ->
            val mappedExpenses = expenses.map { Expense(0, it.name, it.amount, it.date) }
            expenseAdapter.submitList(mappedExpenses)
        }

        // Setup header and footer fragments
        if (childFragmentManager.findFragmentById(R.id.headerContainer) == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.headerContainer, HeaderFragment())
                .commit()
        }
        if (childFragmentManager.findFragmentById(R.id.footerContainer) == null) {
            childFragmentManager.beginTransaction()
                .replace(R.id.footerContainer, FooterFragment())
                .commit()
        }

        // Start animation
        val motionLayout = view.findViewById<MotionLayout>(R.id.motionLayout)
        motionLayout.transitionToEnd()

        // Add expense
        btnAddExpense.setOnClickListener {
            val name = etExpenseNameId.text.toString()
            val amountText = etEnterAmount.text.toString()

            if (name.isNotEmpty() && amountText.isNotEmpty()) {
                val amount = amountText.toDoubleOrNull()
                if (amount != null) {
                    val selected = selectedDate ?: "No date"
                    viewModel.addExpense(name, amount, selected)
                    etExpenseNameId.text.clear()
                    etEnterAmount.text.clear()
                    selectedDate = null
                } else {
                    Toast.makeText(requireContext(), "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        // Financial tips button
        btnFinancialTips.setOnClickListener {
            val url = "https://www.investopedia.com/financial-tips-for-young-adults-11678397"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        // Select date
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

        // Start foreground service
        val serviceIntent = Intent(requireContext(), OverdueCheckService::class.java)
        ContextCompat.startForegroundService(requireContext(), serviceIntent)

        // Schedule weekly cost calculation worker
        val workRequest = PeriodicWorkRequestBuilder<costCalculationWorker>(7, TimeUnit.DAYS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()
            )
            .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "costCalculationWork",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

        // Sync status observer
        SyncStatusManager.isSyncActive.observe(viewLifecycleOwner) { isActive ->
            syncStatusText?.text = if (isActive) "Sync Active" else "Sync Paused"
        }
    }
}
