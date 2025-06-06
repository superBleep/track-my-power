package com.afca.trackmypower.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.afca.trackmypower.data.dao.ExerciseDAO
import com.afca.trackmypower.data.dao.UserDAO
import com.afca.trackmypower.data.dao.WorkSetDAO
import com.afca.trackmypower.data.dao.WorkoutDAO
import com.afca.trackmypower.data.models.Exercise
import com.afca.trackmypower.data.models.User
import com.afca.trackmypower.data.models.WorkSet
import com.afca.trackmypower.data.models.Workout

@Database(
    entities = [
        Workout::class,
        Exercise::class,
        WorkSet::class,
        User::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(LocalDatabaseConverters::class)
abstract class LocalDatabase : RoomDatabase() {

    abstract val workoutDAO: WorkoutDAO
    abstract val exerciseDAO: ExerciseDAO
    abstract val workSetDAO: WorkSetDAO

    abstract val userDAO: UserDAO

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "trackmypower_db"
                )
                    // If schema changes and no migration, just wipe & rebuild:
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}