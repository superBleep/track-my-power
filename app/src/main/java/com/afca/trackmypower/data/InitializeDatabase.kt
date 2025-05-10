package com.afca.trackmypower.data

import android.content.Context
import com.afca.trackmypower.R
import com.afca.trackmypower.data.models.Exercise
import com.afca.trackmypower.data.models.WorkSet
import com.afca.trackmypower.data.models.Workout
import com.afca.trackmypower.data.repositories.workout.ExerciseRepository
import com.afca.trackmypower.data.repositories.workout.WorkSetRepository
import com.afca.trackmypower.data.repositories.workout.WorkoutRepository
import com.afca.trackmypower.models.MuscleGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

fun initializeDb(
    context: Context,
    workoutRepository: WorkoutRepository,
    exerciseRepository: ExerciseRepository,
    workSetRepository: WorkSetRepository
) {
    CoroutineScope(Dispatchers.IO).launch {
        workoutRepository.get(1).collect { workout ->
            if (workout == null) {
                val inputStream = context.resources.openRawResource(R.raw.gymdata)
                val reader = InputStreamReader(inputStream)

                var workoutId: Long? = null
                var exerciseId = 0L

                val workouts = arrayListOf<Workout>()
                val exercises = arrayListOf<Exercise>()
                val workSets = arrayListOf<WorkSet>()

                reader.buffered().forEachLine { line ->
                    // Skip commented lines and header
                    if (line.startsWith("#") || line.startsWith("Id"))
                        return@forEachLine
                    else {
                        val values = line.split(',')

                        // First workout or workout ID changed
                        if (workoutId == null || workoutId != values[0].toLong()) {
                            workoutId = values[0].toLong()

                            // Random start and end times for workouts
                            val startTime = LocalTime
                                .of(12, 30)
                                .plusMinutes(Random.nextInt(0, 21).toLong())
                            val endTime = LocalTime
                                .of(13, 50)
                                .plusMinutes(Random.nextInt(0, 56).toLong())

                            workouts.add(
                                Workout(
                                    values[0].toLong() - 1,
                                    LocalDate.parse(values[1], DateTimeFormatter.ISO_DATE),
                                    values[2].toInt(), values[3].toInt(),
                                    values[4].toInt(), values[5].toInt(),
                                    startTime, endTime
                                )
                            )
                        }

                        // Add exercise
                        exercises.add(
                            Exercise(
                                exerciseId, workoutId!!.toLong(),
                                values[6], MuscleGroup.valueOf(values[7])
                            )
                        )

                        // Add workload and pair with exercise
                        values[8].split(" ").forEachIndexed { nr, workString ->
                            workSets.add(
                                WorkSet(
                                    exerciseId = exerciseId,
                                    number = nr + 1,
                                    workString = workString,
                                    score = 0, // ToDo: Compute score
                                )
                            )
                        }
                        exerciseId += 1
                    }
                }

                workoutRepository.insertAll(workouts)
                exerciseRepository.insertAll(exercises)
                workSetRepository.insertAll(workSets)
            }
        }
    }
}
