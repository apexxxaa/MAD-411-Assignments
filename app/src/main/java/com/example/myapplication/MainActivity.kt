package com.example.myapplication



import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View

import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var inputBox: EditText
    private lateinit var greetingTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


       inputBox = findViewById(R.id.inputBox)
        greetingTextView = findViewById(R.id.greetingTextView)
    }

    fun onClick(view: View){
        val name = inputBox.text.toString()
        greetingTextView.text = "Hello, $name !!!!! :)"
    }
}
