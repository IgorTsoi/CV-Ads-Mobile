package com.example.cv_ads_mobile.services

import android.content.Context
import com.example.cv_ads_mobile.R

class CVAdsApiUrlBuilder(private val context: Context) {
    public fun createGetPaymentAmountRequestUrl() = (
            context.getString(R.string.api_base_url) +
                    context.getString(R.string.api_get_payment_amount_url)
    )

    public fun createWithdrawRequestUrl() = (
            context.getString(R.string.api_base_url) +
                    context.getString(R.string.api_withdraw_url)
    )

    public fun createRegisterRequestUrl() = (
            context.getString(R.string.api_base_url) +
                    context.getString(R.string.api_register_partner_url)
    )

    public fun createLoginRequestUrl() = (
            context.getString(R.string.api_base_url) +
                    context.getString(R.string.api_login_partner_url)
    )

    public fun createActivateSmartDeviceRequestUrl() = (
            context.getString(R.string.api_base_url) +
                    context.getString(R.string.api_activate_smart_device)
    )

    public fun createGetAllSmartDevicesRequestUrl() = (
            context.getString(R.string.api_base_url) +
                    context.getString(R.string.api_get_smart_devices)
    )

    public fun createUpdateSmartDeviceUrlWithId(id: String) = (
            context.getString(R.string.api_base_url) +
                    context.getString(R.string.api_update_smart_device_configuration)
                        .replace("{id}", id)
    )
}