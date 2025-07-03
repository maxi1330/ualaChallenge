package com.mgnovatto.uala.utils

import android.os.Build
import java.util.Locale

fun getCountryNameFromCode(countryCode: String): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.BAKLAVA) {
        Locale.of("", countryCode).displayCountry
    } else {
        @Suppress("DEPRECATION")
        Locale("", countryCode).displayCountry
    }
}