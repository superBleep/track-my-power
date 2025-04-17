package com.afca.trackmypower

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationController: Application() {
    companion object {
        var instance: ApplicationController? = null

        private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
    }
}