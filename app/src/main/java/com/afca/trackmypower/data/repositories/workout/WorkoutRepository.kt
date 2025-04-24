package com.afca.trackmypower.data.repositories.workout

import com.afca.trackmypower.data.dao.WorkoutDAO
import com.afca.trackmypower.data.models.Workout
import javax.inject.Inject

class WorkoutRepository @Inject constructor(
    private val dao: WorkoutDAO
) {
    suspend fun insert(workout: Workout) = dao.insert(workout)
    suspend fun insertAll(workouts: List<Workout>) = dao.insertAll(workouts)
    fun get(id: Long) = dao.get(id)
    fun getWithExercises(id: Long) = dao.getWithExercises(id)
    suspend fun update(workout: Workout) = dao.update(workout)
}