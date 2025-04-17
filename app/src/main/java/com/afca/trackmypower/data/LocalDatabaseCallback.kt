package com.afca.trackmypower.data

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.afca.trackmypower.data.dao.WorkoutDAO
import com.afca.trackmypower.data.models.Workout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Provider

class LocalDatabaseCallback @Inject constructor(
    private val workoutDAO: Provider<WorkoutDAO>
) : RoomDatabase.Callback() {
    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)

        CoroutineScope(Dispatchers.IO).launch {
            workoutDAO.get().insert(
                Workout(
                    id = 1,
                    date = LocalDate.parse("2025-10-02", DateTimeFormatter.ISO_DATE),
                    year = 1,
                    month = 6,
                    week = 3,
                    day = 1,
                    startTime = LocalTime.parse("12:00", DateTimeFormatter.ofPattern("HH:mm")),
                    endTime = LocalTime.parse("13:45", DateTimeFormatter.ofPattern("HH:mm")),
                )
            )
        }
    }
}