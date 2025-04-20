package com.afca.trackmypower.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class ExerciseWithSets (
    @Embedded
    val exercise: Exercise,

    @Relation(
        parentColumn = Exercise.ID,
        entityColumn = WorkSet.EXERCISE_ID
    )
    val workSets: List<WorkSet>
)