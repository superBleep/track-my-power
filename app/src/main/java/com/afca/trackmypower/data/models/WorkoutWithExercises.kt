package com.afca.trackmypower.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class WorkoutWithExercises (
    @Embedded
    val workout: Workout,

    @Relation(
        parentColumn = Workout.ID,
        entityColumn = Exercise.WORKOUT_ID
    )
    val exercises: List<Exercise>
)