package com.example.cv_ads_mobile.dto.responses

class SmartDeviceResponse(
    val id: String,
    val mode: Int,
    var isCaching: Boolean,
    var isTurnedOn: Boolean,
    val serialNumber: String
) {}
