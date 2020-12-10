package com.example.cv_ads_mobile.models

class SmartDevice {
    var SerialNumber: String
    var IsCahing: Boolean
    var IsScreenTurnedOn: Boolean

    constructor(serialNumber: String, isCaching: Boolean, isScreenTurnedOn: Boolean) {
        SerialNumber = serialNumber;
        IsCahing = isCaching;
        IsScreenTurnedOn = isScreenTurnedOn;
    }
}