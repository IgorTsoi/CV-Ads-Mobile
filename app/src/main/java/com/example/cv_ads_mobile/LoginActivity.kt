package com.example.cv_ads_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.cv_ads_mobile.services.PersistentStorage

class LoginActivity : AppCompatActivity() {
    private val persistentStorage: PersistentStorage = PersistentStorage(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            if (login(emailEditText.text.toString(), passwordEditText.text.toString())) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val message = getLocalizedErrorMessage()
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        setupNavigation()
    }

    override fun onResume() {
        super.onResume()
        if (persistentStorage.getCurrentLanguage() == getString(R.string.shared_preferences_language_ua_key)) {
            findViewById<TextView>(R.id.loginTextView).text = "Вхід"
            findViewById<TextView>(R.id.emailLabelTextView).text = "Електронна пошта"
            findViewById<TextView>(R.id.passwordLabelTextView).text = "Пароль"
            findViewById<TextView>(R.id.orTextView).text = "або"
            findViewById<Button>(R.id.loginButton).text = "Увійти"
            findViewById<Button>(R.id.navigateToRegisterButton).text = "Зареєструватися"

        } else {
            findViewById<TextView>(R.id.loginTextView).text = "Login"
            findViewById<TextView>(R.id.emailLabelTextView).text = "Email"
            findViewById<TextView>(R.id.passwordLabelTextView).text = "Password"
            findViewById<TextView>(R.id.orTextView).text = "or"
            findViewById<Button>(R.id.loginButton).text = "Login"
            findViewById<Button>(R.id.navigateToRegisterButton).text = "Register"
        }
    }


    private fun getLocalizedErrorMessage(): String {
        var message = "Log in failed!"
        if (persistentStorage.getCurrentLanguage() == getString(R.string.shared_preferences_language_ua_key)) {
            message = "Помилка!"
        }
        return message
    }

    private fun setupNavigation() {
        val navigateToRegisterButton = findViewById<Button>(R.id.navigateToRegisterButton)
        navigateToRegisterButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }


    private fun login(email: String, password: String): Boolean {
        // TODO: implement
        return email == "ihor.tsoi@nure.ua" && password == "password"
    }
}