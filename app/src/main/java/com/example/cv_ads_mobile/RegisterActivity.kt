package com.example.cv_ads_mobile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.cv_ads_mobile.dto.requests.RegisterRequest
import com.example.cv_ads_mobile.services.PersistentStorage
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class RegisterActivity : AppCompatActivity() {
    private val persistentStorage: PersistentStorage = PersistentStorage(this)
    private val httpClient = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val emailEditText = findViewById<EditText>(R.id.registerEmailEditText)
        val passwordEditText = findViewById<EditText>(R.id.registerPasswordEditText)

        findViewById<Button>(R.id.registerButton).setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            register(
                RegisterRequest(
                    "DEFAULT_FIRST_NAME",
                    "DEFAULT_LAST_NAME",
                    email,
                    password
                ), this)
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

    private fun register(registerRequest: RegisterRequest, context: Context) {
        val url = getString(R.string.api_base_url) + getString(R.string.api_register_partner_url)
        val content = GsonBuilder().create().toJson(registerRequest)
        println("[*] Request: $content")

        val request = Request.Builder()
            .url(url)
            .post(RequestBody.create(MediaType.parse("application/json"), content))
            .addHeader("Accept-Language", persistentStorage.getCurrentLanguage())
            .build()

        httpClient.newCall(request).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("[*] Request error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonResponse = response.body()?.string()
                println("[*] Response: $jsonResponse")

                if (response.isSuccessful) {
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    val message = getLocalizedErrorMessage()
                    runOnUiThread {
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun getLocalizedErrorMessage(): String {
        var message = "Registration failed!"
        if (persistentStorage.getCurrentLanguage() == getString(R.string.shared_preferences_language_ua_key)) {
            message = "Помилка!"
        }
        return message
    }
}