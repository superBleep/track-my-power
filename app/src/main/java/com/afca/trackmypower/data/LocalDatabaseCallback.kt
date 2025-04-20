package com.afca.trackmypower.data

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.afca.trackmypower.data.dao.ExerciseDAO
import com.afca.trackmypower.data.dao.WorkSetDAO
import com.afca.trackmypower.data.dao.WorkoutDAO
import com.afca.trackmypower.data.models.Exercise
import com.afca.trackmypower.data.models.WorkSet
import com.afca.trackmypower.data.models.Workout
import com.afca.trackmypower.models.MuscleGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Provider

class LocalDatabaseCallback @Inject constructor(
    private val workoutDAO: Provider<WorkoutDAO>,
    private val exerciseDAO: Provider<ExerciseDAO>,
    private val workSetDAO: Provider<WorkSetDAO>
) : RoomDatabase.Callback() {
    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)

        CoroutineScope(Dispatchers.IO).launch {
            workoutDAO.get().insert(
                Workout(
                    id = 1,
                    date = LocalDate.parse("2025-01-01", DateTimeFormatter.ISO_DATE),
                    year = 1,
                    month = 2,
                    week = 3,
                    day = 4,
                    startTime = LocalTime.parse("00:00", DateTimeFormatter.ofPattern("HH:mm")),
                    endTime = LocalTime.parse("01:00", DateTimeFormatter.ofPattern("HH:mm")),
                )
            )

            exerciseDAO.get().insertAll(arrayListOf(
                    Exercise(
                        id = 1,
                        workoutId = 1,
                        name = "Dumbbell Bench Press",
                        group = MuscleGroup.Chest
                    ),
                    Exercise(
                        id = 2,
                        workoutId = 1,
                        name = "Sled Push",
                        group = MuscleGroup.Legs
                    )
                )
            )

            workSetDAO.get().insertAll(arrayListOf(
                WorkSet(
                    exerciseId = 1,
                    number = 1,
                    workString = "9x25",
                    score = 0
                ),
                WorkSet(
                    exerciseId = 1,
                    number = 2,
                    workString = "8x(25,20,15)",
                    score = 1
                ),
                WorkSet(
                    exerciseId = 2,
                    number = 1,
                    workString = "90",
                    score = 1
                ),
                WorkSet(
                    exerciseId = 2,
                    number = 2,
                    workString = "100",
                    score = 1
                ),
            ))
        }
    }
}