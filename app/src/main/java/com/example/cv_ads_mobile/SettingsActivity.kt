package com.example.cv_ads_mobile

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import com.example.cv_ads_mobile.services.PersistentStorage

class SettingsActivity : AppCompatActivity() {
    private var ukrainianRadioButton: RadioButton? = null
    private var englishRadioButton: RadioButton? = null
    private val persistentStorage: PersistentStorage = PersistentStorage(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        ukrainianRadioButton = findViewById<RadioButton>(R.id.ukrainianRadioButton)
        englishRadioButton = findViewById<RadioButton>(R.id.englishRadioButton)

        setupCurrentLanguage()
        setupRadioButtonsListeners()
    }

    override fun onResume() {
        super.onResume()
        if (persistentStorage.getCurrentLanguage() == getString(R.string.shared_preferences_language_ua_key)) {
            findViewById<TextView>(R.id.settingsTextView).text = "Налаштування"
            findViewById<TextView>(R.id.languageTextView).text = "Мова"
            findViewById<RadioButton>(R.id.ukrainianRadioButton).text = "Українська"
            findViewById<RadioButton>(R.id.englishRadioButton).text = "Англійська"
        } else {
            findViewById<TextView>(R.id.settingsTextView).text = "Settings"
            findViewById<TextView>(R.id.languageTextView).text = "Language"
            findViewById<RadioButton>(R.id.ukrainianRadioButton).text = "Ukrainian"
            findViewById<RadioButton>(R.id.englishRadioButton).text = "English"
        }
    }

    private fun setupCurrentLanguage() {
        if (persistentStorage.getCurrentLanguage() == getString(R.string.shared_preferences_language_ua_key)) {
            ukrainianRadioButton?.setChecked(true)
        } else {
            englishRadioButton?.setChecked(true)
        }
    }

    private fun setupRadioButtonsListeners() {
        ukrainianRadioButton?.setOnClickListener {
            persistentStorage.setLanguage(getString(R.string.shared_preferences_language_ua_key))
            onResume()
        }

        englishRadioButton?.setOnClickListener {
            persistentStorage.setLanguage(getString(R.string.shared_preferences_language_en_key))
            onResume()
        }
    }
}