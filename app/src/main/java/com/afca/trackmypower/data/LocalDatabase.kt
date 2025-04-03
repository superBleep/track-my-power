package com.afca.trackmypower.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.afca.trackmypower.data.dao.WorkoutDAO
import com.afca.trackmypower.data.models.Workout

@Database(
    entities = [
        Workout::class
    ],
    version = 1
)
@TypeConverters(LocalDatabaseConverters::class)
abstract class LocalDatabase: RoomDatabase() {
    abstract val workoutDAO: WorkoutDAO
}