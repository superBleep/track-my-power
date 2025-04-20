package com.afca.trackmypower.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.afca.trackmypower.data.dao.ExerciseDAO
import com.afca.trackmypower.data.dao.WorkSetDAO
import com.afca.trackmypower.data.dao.WorkoutDAO
import com.afca.trackmypower.data.models.Exercise
import com.afca.trackmypower.data.models.WorkSet
import com.afca.trackmypower.data.models.Workout

@Database(
    entities = [
        Workout::class,
        Exercise::class,
        WorkSet::class
    ],
    version = 2,
    exportSchema = false
)
@TypeConverters(LocalDatabaseConverters::class)
abstract class LocalDatabase: RoomDatabase() {
    abstract val workoutDAO: WorkoutDAO
    abstract val exerciseDAO: ExerciseDAO
    abstract val workSetDAO: WorkSetDAO
}