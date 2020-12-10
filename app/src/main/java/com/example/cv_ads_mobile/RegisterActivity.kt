package com.example.cv_ads_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.cv_ads_mobile.services.PersistentStorage

class RegisterActivity : AppCompatActivity() {
    private val persistentStorage: PersistentStorage = PersistentStorage(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val emailEditText = findViewById<EditText>(R.id.registerEmailEditText)
        val passwordEditText = findViewById<EditText>(R.id.registerPasswordEditText)

        findViewById<Button>(R.id.registerButton).setOnClickListener {
            if (register(emailEditText.text.toString(), passwordEditText.text.toString())) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                val message = getLocalizedErrorMessage()
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (persistentStorage.getCurrentLanguage() == getString(R.string.shared_preferences_language_ua_key)) {
            findViewById<TextView>(R.id.registerTextView).text = "Реєстрація"
            findViewById<TextView>(R.id.emailTextView).text = "Електронна пошта"
            findViewById<TextView>(R.id.passwordTextView).text = "Новий пароль"
            findViewById<Button>(R.id.registerButton).text = "Зареєструватися"

        } else {
            findViewById<TextView>(R.id.registerTextView).text = "Registration"
            findViewById<TextView>(R.id.emailTextView).text = "Email"
            findViewById<TextView>(R.id.passwordTextView).text = "New password"
            findViewById<Button>(R.id.registerButton).text = "Register"
        }
    }

    private fun register(email: String, password: String): Boolean {
        // TODO: implement
        return email == "new.ihor.tsoi@nure.ua" && password == "password"
    }

    private fun getLocalizedErrorMessage(): String {
        var message = "Registration failed!"
        if (persistentStorage.getCurrentLanguage() == getString(R.string.shared_preferences_language_ua_key)) {
            message = "Помилка!"
        }
        return message
    }
}