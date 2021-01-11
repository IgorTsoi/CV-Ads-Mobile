package com.example.cv_ads_mobile

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.example.cv_ads_mobile.dto.requests.SmartDeviceUpdateConfigurationRequest
import com.example.cv_ads_mobile.dto.responses.SmartDeviceResponse
import com.example.cv_ads_mobile.services.CVAdsApiService
import com.example.cv_ads_mobile.services.CVAdsApiUrlBuilder
import com.example.cv_ads_mobile.services.PersistentStorage
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private var serialNumberTextView: TextView? = null
    private var isCachingSwitch: Switch? = null
    private var isScreenTurnedOnSwitch: Switch? = null

    private val persistentStorage: PersistentStorage = PersistentStorage(this)
    private val httpClient = OkHttpClient()
    private val cvAdsApiService = CVAdsApiService(
        CVAdsApiUrlBuilder(this), persistentStorage
    )

    private var smartDevices: List<SmartDeviceResponse> = listOf()
    private var currentSmartDeviceIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serialNumberTextView = findViewById<TextView>(R.id.serialNumberTextBox)
        isCachingSwitch = findViewById<Switch>(R.id.isCachingSwitch)
        isScreenTurnedOnSwitch = findViewById<Switch>(R.id.isScreenTurnedOnSwitch)

        fetchSmartDevices()

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
            findViewById<TextView>(R.id.serialNumberTextView).text = "Серійний номер:"
            findViewById<TextView>(R.id.serialNumberTextView).setTypeface(null, Typeface.BOLD)
            findViewById<Switch>(R.id.isCachingSwitch).text = "Кешування увімкнено:"
            findViewById<Switch>(R.id.isScreenTurnedOnSwitch).text = "Екран увімкнено:"
            findViewById<Button>(R.id.nextButton).text = "Наступний"
            findViewById<Button>(R.id.previousButton).text = "Попередній"
        } else {
            findViewById<Button>(R.id.navigateToActivateButton).text = "Activate"
            findViewById<Button>(R.id.navigateToPaymentButton).text = "Payment"
            findViewById<Button>(R.id.navigateToSettingsButton).text = "Settings"
            findViewById<TextView>(R.id.mySmartDevicesTextView).text = "My smart devices"
            findViewById<TextView>(R.id.serialNumberTextView).text = "Serial number:"
            findViewById<TextView>(R.id.serialNumberTextView).setTypeface(null, Typeface.BOLD)
            findViewById<Switch>(R.id.isCachingSwitch).text = "Caching enabled:"
            findViewById<Switch>(R.id.isScreenTurnedOnSwitch).text = "Screen turned on:"
            findViewById<Button>(R.id.nextButton).text = "Next"
            findViewById<Button>(R.id.previousButton).text = "Previous"
        }
    }

    private fun fetchSmartDevices() {
        val httpCall = httpClient.newCall(cvAdsApiService.createGetAllSmartDevicesRequest())

        httpCall.enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("[*] Request error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonResponse = response.body()?.string()
                println("[*] Get smart devices response: $jsonResponse")

                if (response.isSuccessful) {
                    val objectType = object : TypeToken<List<SmartDeviceResponse>>() {}.type
                    smartDevices = GsonBuilder().create()
                        .fromJson<List<SmartDeviceResponse>>(jsonResponse, objectType)
                    runOnUiThread{
                        updateView()
                        setupPreviousNextButtons()
                    }
                } else {
                    println("[*] Request error: ${response.code()}")
                }
            }
        })
    }

    private fun updateView() {
        if (smartDevices.count() == 0) {
            isCachingSwitch?.isEnabled = false
            isScreenTurnedOnSwitch?.isEnabled = false
            return;
        }

        val smartDevice: SmartDeviceResponse = smartDevices[currentSmartDeviceIndex]

        if (smartDevice.mode != 1) {
            val blockedMessage = getLocalizedBlockedMessage()
            findViewById<TextView>(R.id.blockedTextView)?.setText(blockedMessage);
            isCachingSwitch?.isEnabled = false
            isScreenTurnedOnSwitch?.isEnabled = false
        } else {
            findViewById<TextView>(R.id.blockedTextView)?.setText("");
            isCachingSwitch?.isEnabled = true
            isScreenTurnedOnSwitch?.isEnabled = true
        }

        serialNumberTextView?.setText(smartDevice.serialNumber)
        isCachingSwitch?.setChecked(smartDevice.isCaching)
        isScreenTurnedOnSwitch?.setChecked(smartDevice.isTurnedOn)
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
            if(currentSmartDeviceIndex < smartDevices.count()) {
                val smartDevice = smartDevices[currentSmartDeviceIndex]
                updateSmartDeviceConfiguration(
                    smartDevice,
                    SmartDeviceUpdateConfigurationRequest(
                            !smartDevice.isCaching,
                            smartDevice.isTurnedOn
                    ),
                    this
                )
            }
        }

        isScreenTurnedOnSwitch?.setOnClickListener {
            if(currentSmartDeviceIndex < smartDevices.count()) {
                val smartDevice = smartDevices[currentSmartDeviceIndex]
                updateSmartDeviceConfiguration(
                    smartDevice,
                    SmartDeviceUpdateConfigurationRequest(
                            smartDevice.isCaching,
                            !smartDevice.isTurnedOn
                    ),
                    this
                )
            }
        }
    }

    private fun updateSmartDeviceConfiguration(
        smartDevice: SmartDeviceResponse,
        updateConfigurationRequest: SmartDeviceUpdateConfigurationRequest,
        context: Context
    ) {
        val httpCall = httpClient.newCall(
            cvAdsApiService.createUpdateSmartDeviceRequest(
                smartDevice, updateConfigurationRequest
            )
        )

        httpCall.enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                println("[*] Request error: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {
                val responseText = response.body()?.string()
                println("[*] Upd. configuration response: $responseText")

                var message = ""
                if (response.isSuccessful) {
                    message = getLocalizedSuccessMessage()
                    smartDevice.isCaching = updateConfigurationRequest.isCaching
                    smartDevice.isTurnedOn = updateConfigurationRequest.isTurnedOn
                } else {
                    message = getLocalizedErrorMessage()
                    runOnUiThread {
                        isCachingSwitch?.setChecked(smartDevice.isCaching)
                        isScreenTurnedOnSwitch?.setChecked(smartDevice.isTurnedOn)
                    }
                }
                runOnUiThread {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun getLocalizedBlockedMessage(): String {
        var message = "Blocked!"
        if (persistentStorage.getCurrentLanguage() == getString(R.string.shared_preferences_language_ua_key)) {
            message = "Заблоковано!"
        }
        return message
    }

    private fun getLocalizedErrorMessage(): String {
        var message = "Something went wrong! Please try again later!"
        if (persistentStorage.getCurrentLanguage() == getString(R.string.shared_preferences_language_ua_key)) {
            message = "Помилка! Будь-ласка спробуйте пізніше!"
        }
        return message
    }

    private fun getLocalizedSuccessMessage(): String {
        var message = "Configuration updated!"
        if (persistentStorage.getCurrentLanguage() == getString(R.string.shared_preferences_language_ua_key)) {
            message = "Налаштування оновлено!"
        }
        return message
    }
}