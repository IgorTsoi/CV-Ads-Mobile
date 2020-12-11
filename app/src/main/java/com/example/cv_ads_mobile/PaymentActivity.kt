package com.example.cv_ads_mobile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.cv_ads_mobile.services.PersistentStorage
import okhttp3.*
import java.io.IOException

class PaymentActivity : AppCompatActivity() {
    private val persistentStorage: PersistentStorage = PersistentStorage(this)
    private val httpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        getPaymentAmount()

        findViewById<Button>(R.id.withdrawButton).setOnClickListener{
            withdraw(this)
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

    private fun getPaymentAmount() {
        val url = getString(R.string.api_base_url) + getString(R.string.api_get_payment_amount_url)

        val request = Request.Builder()
            .url(url)
            .addHeader("Accept-Language", persistentStorage.getCurrentLanguage())
            .addHeader("Authorization", "Bearer " + persistentStorage.getAccessToken())
            .build()

        httpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("[*] Request error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseText = response.body()?.string()
                println("[*] Get payment amount response: $responseText")

                if (response.isSuccessful) {
                    runOnUiThread {
                        val paymentAmountTextView = findViewById<TextView>(
                            R.id.paymentInDollarsTextView)
                        paymentAmountTextView.text = "$responseText $"
                    }
                } else {
                    println("[*] Request error: ${response.code()}")
                }
            }
        })
    }

    private fun withdraw(context: Context) {
        val url = getString(R.string.api_base_url) + getString(R.string.api_withdraw_url)

        val request = Request.Builder()
            .url(url)
            .method("POST", RequestBody.create(null, ""))
            .addHeader("Accept-Language", persistentStorage.getCurrentLanguage())
            .addHeader("Authorization", "Bearer " + persistentStorage.getAccessToken())
            .build()

        httpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("[*] Request error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseText = response.body()?.string()
                println("[*] Withdraw response: $responseText")

                if (response.isSuccessful) {
                    runOnUiThread{
                        val message = getLocalizedSuccessMessage()
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        getPaymentAmount()
                    }
                } else {
                    println("[*] Request error: ${response.code()}")
                }
            }
        })


    }

    private fun getLocalizedSuccessMessage(): String {
        var message = "Withdraw succeed"
        if (persistentStorage.getCurrentLanguage() == getString(R.string.shared_preferences_language_ua_key)) {
            message = "Гроші успішно знято!"
        }
        return message
    }
}