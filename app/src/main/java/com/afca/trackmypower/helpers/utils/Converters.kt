package com.afca.trackmypower.helpers.utils

import androidx.databinding.InverseMethod

object Converters {
    @JvmStatic
    @InverseMethod("stringToInt")
    fun intToString(value: Int?): String {
        return value?.toString() ?: ""
    }

    @JvmStatic
    fun stringToInt(value: String?): Int? {
        return value?.toIntOrNull()
    }
}