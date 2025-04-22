package com.afca.trackmypower.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.afca.trackmypower.data.models.Exercise
import com.afca.trackmypower.data.models.ExerciseWithWorkSets
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exercises: List<Exercise>)

    @Query(
        """
            SELECT *
            FROM Exercise
            WHERE workoutId = :workoutId
        """
    )
    fun getWithWorkSetsByWorkoutId(workoutId: Long): Flow<List<ExerciseWithWorkSets>>
}