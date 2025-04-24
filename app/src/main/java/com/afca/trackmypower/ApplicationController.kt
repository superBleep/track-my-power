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
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlin.random.Random

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
                    val inputStream = applicationContext.resources.openRawResource(R.raw.gymdata)
                    val reader = InputStreamReader(inputStream)

                    var workoutId: Long? = null
                    var exerciseId = 0L

                    val workouts = arrayListOf<Workout>()
                    val exercises = arrayListOf<Exercise>()
                    val workSets = arrayListOf<WorkSet>()

                    reader.buffered().forEachLine { line ->
                        val values = line.split(',')

                        if (workoutId == null || workoutId != values[0].toLong()) {
                            workoutId = values[0].toLong()

                            val startTime = LocalTime
                                .of(12, 30)
                                .plusMinutes(Random.nextInt(0, 21).toLong())
                            val endTime = LocalTime
                                .of(13, 50)
                                .plusMinutes(Random.nextInt(0, 56).toLong())

                            workouts.add(Workout(
                                values[0].toLong() - 1,
                                LocalDate.parse(values[1], DateTimeFormatter.ISO_DATE),
                                values[2].toInt(), values[3].toInt(), values[4].toInt(), values[5].toInt(),
                                startTime, endTime
                            ))
                        }

                        exercises.add(Exercise(exerciseId, values[0].toInt().toLong(),
                            values[6], MuscleGroup.valueOf(values[7])
                        ))

                        values[8].forEachIndexed { nr, workString ->
                            workSets.add(WorkSet(
                                exerciseId = exerciseId,
                                number = nr + 1,
                                workString = workString.toString(),
                                score = 0,
                            ))
                        }

                        exerciseId += 1
                    }

                    workoutRepository.insertAll(workouts)
                    exerciseRepository.insertAll(exercises)
                    workSetRepository.insertAll(workSets)
                }
            }
        }
    }
}