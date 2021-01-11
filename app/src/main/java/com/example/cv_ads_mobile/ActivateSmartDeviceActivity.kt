package com.example.cv_ads_mobile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.cv_ads_mobile.dto.requests.ActivateSmartDeviceRequest
import com.example.cv_ads_mobile.services.CVAdsApiService
import com.example.cv_ads_mobile.services.CVAdsApiUrlBuilder
import com.example.cv_ads_mobile.services.PersistentStorage
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class ActivateSmartDeviceActivity : AppCompatActivity() {
    private val persistentStorage: PersistentStorage = PersistentStorage(this)
    private val httpClient = OkHttpClient()
    private val cvAdsApiService = CVAdsApiService(
        CVAdsApiUrlBuilder(this), persistentStorage
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activate_smart_device)

        val serialNumberEditText = findViewById<EditText>(R.id.activateSerialNumberEditText)
        val defaultPasswordEditText = findViewById<EditText>(R.id.activateDefaultPasswordEditText)
        val newPasswordEditText = findViewById<EditText>(R.id.activateNewPasswordEditText)

        val activateButton = findViewById<Button>(R.id.activateButton)
        activateButton.setOnClickListener {
            val serialNumber = serialNumberEditText.text.toString()
            val defaultPassword = defaultPasswordEditText.text.toString()
            val newPassword = newPasswordEditText.text.toString()

            activateSmartDevice(
                ActivateSmartDeviceRequest(serialNumber, defaultPassword, newPassword),
                this
            )
        }
    }

    override fun onResume() {
        super.onResume()
        if (persistentStorage.getCurrentLanguage() == getString(R.string.shared_preferences_language_ua_key)) {
            findViewById<TextView>(R.id.acivateTextView).text = "Активація пристрою"
            findViewById<TextView>(R.id.activateSerialNumberTextView).text = "Серійний номер:"
            findViewById<TextView>(R.id.activateDefaultPasswordTextView).text = "Пароль за замовченням:"
            findViewById<TextView>(R.id.activateNewPasswordTextView).text = "Новий пароль:"
            findViewById<Button>(R.id.activateButton).text = "Активувати"

        } else {
            findViewById<TextView>(R.id.acivateTextView).text = "Device activation"
            findViewById<TextView>(R.id.activateSerialNumberTextView).text = "Serial number:"
            findViewById<TextView>(R.id.activateDefaultPasswordTextView).text = "Default password:"
            findViewById<TextView>(R.id.activateNewPasswordTextView).text = "New password:"
            findViewById<Button>(R.id.activateButton).text = "Activate"
        }
    }

    private fun activateSmartDevice(
        activateSmartDeviceRequest: ActivateSmartDeviceRequest,
        context: Context
    ) {
        val httpCall = httpClient.newCall(
            cvAdsApiService.createActivateSmartDeviceRequest(activateSmartDeviceRequest)
        )

        httpCall.enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("[*] Request error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonResponse = response.body()?.string()
                println("[*] Response: $jsonResponse")

                if (response.isSuccessful) {
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    val message = getLocalizedFailureMessage()
                    runOnUiThread {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun getLocalizedFailureMessage(): String {
        var message = "Activation failed. Please connect the device to WI-FI."
        if (persistentStorage.getCurrentLanguage() == getString(R.string.shared_preferences_language_ua_key)) {
            message = "Помилка активації! Будь-ласка під'єднайте пристрій до WI-FI."
        }
        return message
    }
}