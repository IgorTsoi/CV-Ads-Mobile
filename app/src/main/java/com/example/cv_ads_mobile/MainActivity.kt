package com.example.cv_ads_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.example.cv_ads_mobile.dto.responses.SmartDeviceResponse
import com.example.cv_ads_mobile.services.PersistentStorage

class MainActivity : AppCompatActivity() {
    private var serialNumberTextView: TextView? = null
    private var isCachingSwitch: Switch? = null
    private var isScreenTurnedOnSwitch: Switch? = null

    private val persistentStorage: PersistentStorage = PersistentStorage(this)
    private var smartDevices: List<SmartDeviceResponse> = listOf()
    private var currentSmartDeviceIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serialNumberTextView = findViewById<TextView>(R.id.serialNumberTextBox)
        isCachingSwitch = findViewById<Switch>(R.id.isCachingSwitch)
        isScreenTurnedOnSwitch = findViewById<Switch>(R.id.isScreenTurnedOnSwitch)

        smartDevices = getSmartDevices()
        updateView()

        setupPreviousNextButtons()
        setupNavigation()
        setupToggles()
    }

    override fun onResume() {
        super.onResume()
        if (persistentStorage.getCurrentLanguage() == getString(R.string.shared_preferences_language_ua_key)) {
            findViewById<Button>(R.id.navigateToActivateButton).text = "Активувати"
            findViewById<Button>(R.id.navigateToPaymentButton).text = "Оплата"
            findViewById<Button>(R.id.navigateToSettingsButton).text = "Налаштування"
            findViewById<TextView>(R.id.mySmartDevicesTextView).text = "Мої розумні пристрої"
            findViewById<Switch>(R.id.isCachingSwitch).text = "Кешування увімкнено"
            findViewById<Switch>(R.id.isScreenTurnedOnSwitch).text = "Екран увімкнено"
            findViewById<Button>(R.id.nextButton).text = "Наступний"
            findViewById<Button>(R.id.previousButton).text = "Попередній"
        } else {
            findViewById<Button>(R.id.navigateToActivateButton).text = "Activate"
            findViewById<Button>(R.id.navigateToPaymentButton).text = "Payment"
            findViewById<Button>(R.id.navigateToSettingsButton).text = "Settings"
            findViewById<TextView>(R.id.mySmartDevicesTextView).text = "My smart devices"
            findViewById<Switch>(R.id.isCachingSwitch).text = "Caching enabled"
            findViewById<Switch>(R.id.isScreenTurnedOnSwitch).text = "Screen turned on"
            findViewById<Button>(R.id.nextButton).text = "Next"
            findViewById<Button>(R.id.previousButton).text = "Previous"
        }
    }

    private fun getSmartDevices(): List<SmartDeviceResponse> {
        // TODO: implement
        return listOf(
            SmartDeviceResponse(
                "a",
                true,
                true
            ),
            SmartDeviceResponse(
                "b",
                true,
                false
            ),
            SmartDeviceResponse(
                "c",
                false,
                false
            )
        )
    }

    private fun updateView() {
        if (smartDevices.count() == 0) {
            return;
        }

        val smartDevice: SmartDeviceResponse = smartDevices[currentSmartDeviceIndex]

        serialNumberTextView?.setText(smartDevice.serialNumber)
        isCachingSwitch?.setChecked(smartDevice.isCaching)
        isScreenTurnedOnSwitch?.setChecked(smartDevice.isScreenTurnedOn)
    }

    private fun setupPreviousNextButtons() {
        val nextButton = findViewById<Button>(R.id.nextButton)
        val previousButton = findViewById<Button>(R.id.previousButton)

        fun resetButtons() {
            val first = currentSmartDeviceIndex == 0
            val last = currentSmartDeviceIndex >= (smartDevices.count() - 1)

            previousButton.isEnabled = true
            previousButton.isClickable = true
            nextButton.isEnabled = true
            nextButton.isClickable = true

            if (first) {
                previousButton.isEnabled = false
                previousButton.isClickable = false
            }
            if (last) {
                nextButton.isEnabled = false
                nextButton.isClickable = false
            }
        }

        nextButton.setOnClickListener {
            if (currentSmartDeviceIndex + 1 < smartDevices.count()) {
                currentSmartDeviceIndex++
                updateView()
                resetButtons()
            }

        }

        previousButton.setOnClickListener {
            if (currentSmartDeviceIndex - 1 > -1) {
                currentSmartDeviceIndex--
                updateView()
                resetButtons()
            }
        }

        resetButtons()
    }

    private fun setupNavigation() {
        findViewById<Button>(R.id.navigateToActivateButton).setOnClickListener {
            val intent = Intent(this, ActivateSmartDeviceActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.navigateToPaymentButton).setOnClickListener {
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.navigateToSettingsButton).setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupToggles() {
        isCachingSwitch?.setOnClickListener {
            toggleCaching()
        }

        isScreenTurnedOnSwitch?.setOnClickListener {
            toggleScreen()
        }
    }

    private fun toggleCaching() {
        // TODO: implement
    }

    private fun toggleScreen() {
        // TODO: implement
    }
}