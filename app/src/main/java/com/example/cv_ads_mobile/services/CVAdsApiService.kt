package com.example.cv_ads_mobile.services

import com.example.cv_ads_mobile.dto.requests.ActivateSmartDeviceRequest
import com.example.cv_ads_mobile.dto.requests.LoginRequest
import com.example.cv_ads_mobile.dto.requests.RegisterRequest
import com.example.cv_ads_mobile.dto.requests.SmartDeviceUpdateConfigurationRequest
import com.example.cv_ads_mobile.dto.responses.SmartDeviceResponse
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.RequestBody

class CVAdsApiService(
    private val cvAdsApiUrlBuilder: CVAdsApiUrlBuilder,
    private val persistentStorage: PersistentStorage
) {
    public fun createGetPaymentAmountRequest() : Request {
        val url = cvAdsApiUrlBuilder.createGetPaymentAmountRequestUrl()

        return Request.Builder()
            .url(url)
            .addHeader("Accept-Language", persistentStorage.getCurrentLanguage())
            .addHeader("Authorization", "Bearer " + persistentStorage.getAccessToken())
            .build()
    }

    public fun createWithdrawRequest(): Request {
        val url = cvAdsApiUrlBuilder.createWithdrawRequestUrl()

        return Request.Builder()
            .url(url)
            .method("POST", RequestBody.create(null, ""))
            .addHeader("Accept-Language", persistentStorage.getCurrentLanguage())
            .addHeader("Authorization", "Bearer " + persistentStorage.getAccessToken())
            .build()
    }

    public fun createRegisterRequest(registerRequest: RegisterRequest): Request {
        val url = cvAdsApiUrlBuilder.createRegisterRequestUrl()
        val content = GsonBuilder().create().toJson(registerRequest)

        return Request.Builder()
            .url(url)
            .post(RequestBody.create(MediaType.parse("application/json"), content))
            .addHeader("Accept-Language", persistentStorage.getCurrentLanguage())
            .build()
    }

    public fun createLoginRequest(loginRequest: LoginRequest): Request {
        val url = cvAdsApiUrlBuilder.createLoginRequestUrl()
        val content = GsonBuilder().create().toJson(loginRequest)

        return Request.Builder()
            .url(url)
            .post(RequestBody.create(MediaType.parse("application/json"), content))
            .addHeader("Accept-Language", persistentStorage.getCurrentLanguage())
            .build()
    }

    public fun createActivateSmartDeviceRequest(
        activateSmartDeviceRequest: ActivateSmartDeviceRequest
    ): Request {
        val url = cvAdsApiUrlBuilder.createActivateSmartDeviceRequestUrl()
        val content = GsonBuilder().create().toJson(activateSmartDeviceRequest)

        return Request.Builder()
            .url(url)
            .post(RequestBody.create(MediaType.parse("application/json"), content))
            .addHeader("Accept-Language", persistentStorage.getCurrentLanguage())
            .addHeader("Authorization", "Bearer " + persistentStorage.getAccessToken())
            .build()
    }


    public fun createGetAllSmartDevicesRequest(): Request {
        val url = cvAdsApiUrlBuilder.createGetAllSmartDevicesRequestUrl()

        return Request.Builder()
            .url(url)
            .addHeader("Accept-Language", persistentStorage.getCurrentLanguage())
            .addHeader("Authorization", "Bearer " + persistentStorage.getAccessToken())
            .build()
    }

    public fun createUpdateSmartDeviceRequest(
        smartDevice: SmartDeviceResponse,
        updateConfigurationRequest: SmartDeviceUpdateConfigurationRequest
    ): Request {
        val url = cvAdsApiUrlBuilder.createUpdateSmartDeviceUrlWithId(smartDevice.id)
        val content = GsonBuilder().create().toJson(updateConfigurationRequest)

        return Request.Builder()
            .url(url)
            .patch(RequestBody.create(MediaType.parse("application/json"), content))
            .addHeader("Accept-Language", persistentStorage.getCurrentLanguage())
            .addHeader("Authorization", "Bearer " + persistentStorage.getAccessToken())
            .build()
    }
}