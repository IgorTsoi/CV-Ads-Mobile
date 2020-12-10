package com.example.cv_ads_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import com.example.cv_ads_mobile.models.SmartDevice
import com.example.cv_ads_mobile.services.PersistentStorage

class MainActivity : AppCompatActivity() {
    private var serialNumberTextView: TextView? = null
    private var isCachingSwitch: Switch? = null
    private var isScreenTurnedOnSwitch: Switch? = null

    val persistentStorage: PersistentStorage = PersistentStorage(this)
    private var list: List<SmartDevice> = listOf()
    private var index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serialNumberTextView = findViewById<TextView>(R.id.serialNumberTextBox)
        isCachingSwitch = findViewById<Switch>(R.id.isCachingSwitch)
        isScreenTurnedOnSwitch = findViewById<Switch>(R.id.isScreenTurnedOnSwitch)

        list = getSmartDevices()
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

    private fun getSmartDevices(): List<SmartDevice> {
        // TODO: implement
        return listOf(
            SmartDevice("a", true, true),
            SmartDevice("b", true, false),
            SmartDevice("c", false, false)
        )
    }

    private fun updateView() {
        if (list.count() == 0) {
            return;
        }

        val smartDevice: SmartDevice = list[index]

        serialNumberTextView?.setText(smartDevice.SerialNumber)
        isCachingSwitch?.setChecked(smartDevice.IsCahing)
        isScreenTurnedOnSwitch?.setChecked(smartDevice.IsScreenTurnedOn)
    }

    private fun setupPreviousNextButtons() {
        val nextButton = findViewById<Button>(R.id.nextButton)
        val previousButton = findViewById<Button>(R.id.previousButton)

        fun resetButtons() {
            val first = index == 0
            val last = index >= (list.count() - 1)

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
            if (index + 1 < list.count()) {
                index++
                updateView()
                resetButtons()
            }

        }

        previousButton.setOnClickListener {
            if (index - 1 > -1) {
                index--
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