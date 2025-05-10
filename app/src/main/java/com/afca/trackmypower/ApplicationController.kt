package com.afca.trackmypower

import android.app.Application
import com.afca.trackmypower.data.initializeDb
import com.afca.trackmypower.data.repositories.workout.ExerciseRepository
import com.afca.trackmypower.data.repositories.workout.WorkSetRepository
import com.afca.trackmypower.data.repositories.workout.WorkoutRepository
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ApplicationController: Application() {
    @Inject lateinit var workoutRepository: WorkoutRepository
    @Inject lateinit var exerciseRepository: ExerciseRepository
    @Inject lateinit var workSetRepository: WorkSetRepository

    companion object {
        var instance: ApplicationController? = null

        private set
    }

    override fun onCreate() {
        super.onCreate()

        instance = this
        initializeDb(applicationContext, workoutRepository, exerciseRepository, workSetRepository)
    }
}
