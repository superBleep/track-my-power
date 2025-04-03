package com.afca.trackmypower.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.afca.trackmypower.data.models.Workout

@Dao
interface WorkoutDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: Workout)

    @Query(
        """
        SELECT *
        FROM Workout
        WHERE id = :id
    """
    )
    suspend fun get(id: Long): Workout

    @Update
    suspend fun update(workout: Workout)
}