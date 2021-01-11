package com.example.cv_ads_mobile

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.cv_ads_mobile.services.PersistentStorage
import com.example.cv_ads_mobile.dto.requests.LoginRequest
import com.example.cv_ads_mobile.dto.responses.JwtResponse
import com.example.cv_ads_mobile.services.CVAdsApiService
import com.example.cv_ads_mobile.services.CVAdsApiUrlBuilder
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException

class LoginActivity : AppCompatActivity() {
    private val persistentStorage: PersistentStorage = PersistentStorage(this)
    private val httpClient = OkHttpClient()
    private val cvAdsApiService = CVAdsApiService(
        CVAdsApiUrlBuilder(this), persistentStorage
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)

        val loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            login(
                LoginRequest(
                    email,
                    password
                ), this)
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

    private fun setupNavigation() {
        val navigateToRegisterButton = findViewById<Button>(R.id.navigateToRegisterButton)
        navigateToRegisterButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(loginRequest: LoginRequest, context: Context) {
        val httpCall = httpClient.newCall(cvAdsApiService.createLoginRequest(loginRequest))

        httpCall.enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("[*] Request error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonResponse = response.body()?.string()
                println("[*] Response: $jsonResponse")

                if (response.isSuccessful) {
                    val jwt = GsonBuilder().create()
                        .fromJson(jsonResponse, JwtResponse::class.java)
                    persistentStorage.setAccessToken(jwt.accessToken)

                    val intent = Intent(context, MainActivity::class.java)
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
        var message = "Log in failed!"
        if (persistentStorage.getCurrentLanguage() == getString(R.string.shared_preferences_language_ua_key)) {
            message = "Помилка!"
        }
        return message
    }
}