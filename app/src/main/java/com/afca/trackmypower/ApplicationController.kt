package com.afca.trackmypower

import android.app.Application
import androidx.room.Room
import com.afca.trackmypower.data.LocalDatabase

class ApplicationController: Application() {
    lateinit var localDatabase: LocalDatabase

    companion object {
        var instance: ApplicationController? = null

        private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        initDatabase()
    }

    private fun initDatabase() {
        localDatabase = Room.databaseBuilder(
            context = this,
            klass = LocalDatabase::class.java,
            name = "track_my_power_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}