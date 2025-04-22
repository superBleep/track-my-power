package com.afca.trackmypower.data.repositories.workout

import com.afca.trackmypower.data.dao.ExerciseDAO
import com.afca.trackmypower.data.models.Exercise
import javax.inject.Inject

class ExerciseRepository @Inject constructor(
    private val dao: ExerciseDAO
) {
    suspend fun insertAll(exercises: List<Exercise>) = dao.insertAll(exercises)
    fun getWithWorkSetsByWorkoutId(workoutId: Long) = dao.getWithWorkSetsByWorkoutId(workoutId)
}