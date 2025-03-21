package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/*
reference: https://developer.android.com/topic/libraries/view-binding?hl=en
: https://developer.android.com/develop/ui/views/layout/recyclerview
 */
class MainActivity : AppCompatActivity() {

//empty box that will store things from the screen
    private lateinit var etExpenseNameId: EditText
    private lateinit var etEnterAmount: EditText
    private lateinit var btnAddExpense: Button
    private lateinit var expenseListLayout: LinearLayout

    //this will load screen from aactivity.xml
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*this will finds the buttons and text fields from xml and connects them to the kotlin
        * SO now i can read what user will type and changes things on the screen
        * */
        etExpenseNameId = findViewById(R.id.etExpenseNameId)
        etEnterAmount = findViewById(R.id.etEnterAmount)
        btnAddExpense = findViewById(R.id.btnAddExpense)
        expenseListLayout = findViewById(R.id.expenseListLayout)

        //what happens when i click button
        btnAddExpense.setOnClickListener {
            val name = etExpenseNameId.text.toString()
            val amountText = etEnterAmount.text.toString()

            //checking if user typed anything or not if they did do this if not do nothing
            if (name.isNotEmpty() && amountText.isNotEmpty()) {
                val amount = amountText.toDoubleOrNull() //if i type 10.0 as text convert it to number
                if (amount != null) {
                    /*
                    inflater take file expense_xml and created new view from it
                    then it give reference to this new view expenseView that we can add later to our list of expenses
                    inflate-> new version of layout
                     */
                    val expenseView = LayoutInflater.from(this).inflate(R.layout.expense_item, expenseListLayout, false)

                    /*
                    * binding view inside the inflated layout
                    * binds the inner view textView from expense Namme and amount and delete button
                    * */


                    val expenseNameTextView: TextView = expenseView.findViewById(R.id.expenseNameTextView)
                    val expenseAmountTextView: TextView = expenseView.findViewById(R.id.expenseAmountTextView)
                    val deleteButton: Button = expenseView.findViewById(R.id.deleteButton)

                    //this wil set the tect of expenseName to name and expense amount to amount
                    expenseNameTextView.text = name
                    expenseAmountTextView.text = "$$amount"

                    //now we add the inflated expenseview to expenseListlayout so that new expense is displayed on the screen
                    expenseListLayout.addView(expenseView)

                    //remove the expense from screen when clicked
                    deleteButton.setOnClickListener {
                        expenseListLayout.removeView(expenseView)
                    }

                    etExpenseNameId.text.clear()
                    etEnterAmount.text.clear()
                }
            }
        }
    }
}
