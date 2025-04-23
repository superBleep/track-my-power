package com.afca.trackmypower

import android.app.Application
import com.afca.trackmypower.data.models.Exercise
import com.afca.trackmypower.data.models.WorkSet
import com.afca.trackmypower.data.models.Workout
import com.afca.trackmypower.data.repositories.workout.ExerciseRepository
import com.afca.trackmypower.data.repositories.workout.WorkSetRepository
import com.afca.trackmypower.data.repositories.workout.WorkoutRepository
import com.afca.trackmypower.models.MuscleGroup
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
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

        initializeDb()
    }

    private fun initializeDb() {
        CoroutineScope(Dispatchers.IO).launch {
            workoutRepository.get(1).collect { workout ->
                if (workout == null) {
                    workoutRepository.insert(
                        Workout(1, LocalDate.parse("2025-01-01", DateTimeFormatter.ISO_DATE),
                            1, 2, 3, 4,
                            LocalTime.parse("00:00", DateTimeFormatter.ofPattern("HH:mm")),
                            LocalTime.parse("01:00", DateTimeFormatter.ofPattern("HH:mm")))
                    )

                    exerciseRepository.insertAll(arrayListOf(
                        Exercise(1, 1, "Dumbbell Bench Press", MuscleGroup.Chest),
                        Exercise(2, 1, "Sled Push", MuscleGroup.Legs)
                    )
                    )

                    workSetRepository.insertAll(arrayListOf(
                        WorkSet(1, 1,1, "9x25", 0),
                        WorkSet(2, 1, 2, "8x(25,20,15)", 1),
                        WorkSet(3, 2,1, "90", 1),
                        WorkSet(4, 2,2, "100", 1)
                    ))
                }
            }
        }
    }
}