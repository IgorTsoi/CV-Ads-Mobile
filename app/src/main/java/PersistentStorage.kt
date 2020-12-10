package com.example.cv_ads_mobile.services

import android.content.Context
import android.content.SharedPreferences
import com.example.cv_ads_mobile.R

class PersistentStorage {
    private var context: Context

    constructor(context: Context) {
        this.context = context
    }

    public fun getCurrentLanguage(): String {
        val preferences = getPreferences()
        return preferences.getString(
            context.getString(R.string.shared_preferences_language_key),
            context.getString(R.string.shared_preferences_language_ua_key)
        ) ?: ""
    }

    public fun setLanguage(languageKey: String) {
        val preferences = getPreferences()
        with(preferences.edit()) {
            putString(
                context.getString(R.string.shared_preferences_language_key),
                languageKey
            )
            commit()
        }
    }


    private fun getPreferences(): SharedPreferences {
        return context.getSharedPreferences(
            context.getString(R.string.shared_preferences_key),
            Context.MODE_PRIVATE
        )
    }
}