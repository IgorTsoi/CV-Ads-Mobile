package com.example.cv_ads_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.cv_ads_mobile.services.PersistentStorage

class ActivateSmartDeviceActivity : AppCompatActivity() {
    private val persistentStorage: PersistentStorage = PersistentStorage(this)

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

            if (activateSmartDevice(serialNumber, defaultPassword, newPassword)) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val message = getLocalizedFailureMessage()
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
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
        serialNumber: String,
        defaultPassword: String,
        newPassword: String
    ) : Boolean {
        // TODO: implement
        return newPassword == "password"
    }

    private fun getLocalizedFailureMessage(): String {
        var message = "Activation failed."
        if (persistentStorage.getCurrentLanguage() == getString(R.string.shared_preferences_language_ua_key)) {
            message = "Помилка!"
        }
        return message
    }
}