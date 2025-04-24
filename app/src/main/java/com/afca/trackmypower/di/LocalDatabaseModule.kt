package com.afca.trackmypower.di

import android.content.Context
import androidx.room.Room
import com.afca.trackmypower.data.LocalDatabase
import com.afca.trackmypower.data.dao.ExerciseDAO
import com.afca.trackmypower.data.dao.WorkSetDAO
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
        @ApplicationContext context: Context
    ): LocalDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            LocalDatabase::class.java
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providedWorkoutDAO(db: LocalDatabase): WorkoutDAO = db.workoutDAO

    @Provides
    fun providedExerciseDAO(db: LocalDatabase): ExerciseDAO = db.exerciseDAO

    @Provides
    fun providedWorkSetDAO(db: LocalDatabase): WorkSetDAO = db.workSetDAO
}