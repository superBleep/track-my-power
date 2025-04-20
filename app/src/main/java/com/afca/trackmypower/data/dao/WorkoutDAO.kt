package com.afca.trackmypower.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.afca.trackmypower.data.models.Workout
import com.afca.trackmypower.data.models.WorkoutWithExercises
import kotlinx.coroutines.flow.Flow

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
    fun get(id: Long): Flow<Workout?>

    @Transaction
    @Query(
        """
            SELECT *
            FROM Workout
            WHERE id = :id
        """
    )
    fun getWithExercises(id: Long): Flow<WorkoutWithExercises?>

    @Update
    suspend fun update(workout: Workout)
}