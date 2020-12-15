package com.example.cv_ads_mobile.dto.requests

class ActivateSmartDeviceRequest(
    val serialNumber: String,
    val defaultPassword: String,
    val newPassword: String
) {}
