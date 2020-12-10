package com.example.cv_ads_mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.cv_ads_mobile.services.PersistentStorage

class PaymentActivity : AppCompatActivity() {
    private val persistentStorage: PersistentStorage = PersistentStorage(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        updatePaymentAmount()

        findViewById<Button>(R.id.withdrawButton).setOnClickListener{
            withdraw()
        }
    }

    override fun onResume() {
        super.onResume()
        if (persistentStorage.getCurrentLanguage() == getString(R.string.shared_preferences_language_ua_key)) {
            findViewById<TextView>(R.id.paymentTextView).text = "Оплата"
            findViewById<TextView>(R.id.paymentAmountTextView).text = "Ви заробили:"
            findViewById<Button>(R.id.withdrawButton).text = "Зняти гроші"

        } else {
            findViewById<TextView>(R.id.paymentTextView).text = "Payment"
            findViewById<TextView>(R.id.paymentAmountTextView).text = "You have earned:"
            findViewById<Button>(R.id.withdrawButton).text = "Withdraw"
        }
    }

    private fun updatePaymentAmount() {
        val paymentAmount = getPaymentAmount()

        val paymentAmountTextView = findViewById<TextView>(R.id.paymentInDollarsTextView)
        paymentAmountTextView.text = paymentAmount.toString() + "$"
    }

    private fun getPaymentAmount(): Int {
        // TODO: implement
        return 10
    }

    private fun withdraw() {
        // TODO: implement

        val message = getLocalizedSuccessMessage()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        updatePaymentAmount()
    }

    private fun getLocalizedSuccessMessage(): String {
        var message = "Withdrawed successfully"
        if (persistentStorage.getCurrentLanguage() == getString(R.string.shared_preferences_language_ua_key)) {
            message = "Гроші успішно знято!"
        }
        return message
    }
}