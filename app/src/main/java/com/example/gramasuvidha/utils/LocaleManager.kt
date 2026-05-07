package com.example.gramasuvidha.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LocaleManager {

    private const val LANGUAGE_KEY = "selected_language"
    private const val DEFAULT_LANGUAGE = "en"

    // Get the saved language preference
    fun getLanguage(context: Context): String {
        val sharedPref = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        return sharedPref.getString(LANGUAGE_KEY, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }

    // Save the language preference, apply it, and recreate the activity
    fun setLanguage(context: Context, languageCode: String, activity: Activity? = null) {
        val sharedPref = context.getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        sharedPref.edit().putString(LANGUAGE_KEY, languageCode).apply()
        applyLocale(context, languageCode)

        // Recreate the activity to trigger full recomposition
        // This is the standard Android approach for language switching
        activity?.recreate()
    }

    // Apply the locale to the app
    fun applyLocale(context: Context, languageCode: String) {
        val locale =
                when (languageCode) {
                    "kn" ->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Locale.Builder().setLanguage("kn").setRegion("IN").build()
                            } else {
                                @Suppress("DEPRECATION") Locale("kn", "IN")
                            }
                    else ->
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Locale.Builder().setLanguage("en").setRegion("US").build()
                            } else {
                                @Suppress("DEPRECATION") Locale("en", "US")
                            }
                }

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)
        @Suppress("DEPRECATION")
        context.resources.updateConfiguration(config, context.resources.displayMetrics)
    }

    // Check if Kannada is selected
    fun isKannada(context: Context): Boolean {
        return getLanguage(context) == "kn"
    }
}
