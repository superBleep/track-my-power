package com.afca.trackmypower.di

import android.content.Context
import androidx.room.Room
import com.afca.trackmypower.data.LocalDatabase
import com.afca.trackmypower.data.LocalDatabaseCallback
import com.afca.trackmypower.data.dao.WorkoutDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDatabaseModule {
    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext context: Context,
        callback: LocalDatabaseCallback
    ): LocalDatabase {
        return Room.databaseBuilder(
            context,
            LocalDatabase::class.java,
            "track_my_power_db"
        )
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .build()
    }

    @Provides
    fun providedWorkoutDAO(db: LocalDatabase): WorkoutDAO = db.workoutDAO
}