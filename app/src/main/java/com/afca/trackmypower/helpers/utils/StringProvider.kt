package com.afca.trackmypower.helpers.utils

interface StringProvider {
    fun getString(resId: Int, vararg args: Any): String
}