package com.example.kistcalculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.activity.enableEdgeToEdge

class MainActivity : AppCompatActivity() {

    // Declare views
    private lateinit var editTotalMonths: EditText
    private lateinit var editLotteryRupees: EditText
    private lateinit var editRemainingMonths: EditText
    private lateinit var editDiscount: EditText
    private lateinit var textGuarantee: TextView
    private lateinit var textRoundOff: TextView
    private lateinit var textEMI: TextView
    private lateinit var rootLayout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        rootLayout = findViewById(R.id.main)

        // Handle insets cleanly
        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Bind view IDs
        editTotalMonths = findViewById(R.id.editTotalMonths)
        editLotteryRupees = findViewById(R.id.editLotteryRupees)
        editRemainingMonths = findViewById(R.id.editRemainingMonths)
        editDiscount = findViewById(R.id.editDiscount)
        textGuarantee = findViewById(R.id.textGuarantee)
        textRoundOff = findViewById(R.id.textRoundOff)
        textEMI = findViewById(R.id.textEMI)

        // Watch text inputs
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                calculateKist()
            }
            override fun afterTextChanged(s: Editable?) {}
        }

        // Add listeners
        editTotalMonths.addTextChangedListener(watcher)
        editLotteryRupees.addTextChangedListener(watcher)
        editDiscount.addTextChangedListener(watcher)
        editRemainingMonths.addTextChangedListener(watcher)
    }

    private fun calculateKist() {
        try {
            val totalMonths = editTotalMonths.text.toString().toIntOrNull() ?: 0
            val lotteryRupees = editLotteryRupees.text.toString().toIntOrNull() ?: 0
            val discount = editDiscount.text.toString().toIntOrNull() ?: 0
            val remainingMonths = editRemainingMonths.text.toString().toIntOrNull() ?: 0

            if (totalMonths <= 0 || lotteryRupees <= 0 || remainingMonths <= 0) {
                textEMI.text = "किस्त: Invalid"
                textGuarantee.text = "गारंटी: ₹0.00"
                textRoundOff.text = "RoundOFF: ₹0.00"
                return
            }

            val emiRaw = (lotteryRupees - discount).toFloat() / totalMonths
            val guarantee = lotteryRupees - (lotteryRupees.toFloat() / (remainingMonths + 100) * 100)
            val roundOff = roundToNearest(guarantee, 100)

            textEMI.text = "किस्त: ₹%.2f".format(emiRaw)
            textGuarantee.text = "गारंटी: ₹%.2f".format(guarantee)
            textRoundOff.text = "RoundOFF: ₹%.2f".format(roundOff)

        } catch (e: Exception) {
            Toast.makeText(this, "कृपया सभी इनपुट सही भरें", Toast.LENGTH_SHORT).show()
        }
    }

    private fun roundToNearest(value: Float, nearest: Int): Float {
        return (Math.round(value / nearest.toFloat()) * nearest).toFloat()
    }
}