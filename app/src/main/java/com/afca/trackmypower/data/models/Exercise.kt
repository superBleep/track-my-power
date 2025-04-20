package com.afca.trackmypower.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.afca.trackmypower.models.MuscleGroup

@Entity
data class Exercise (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    val id: Long = 0,

    @ColumnInfo(name = WORKOUT_ID)
    val workoutId: Long,

    val name: String,
    val group: MuscleGroup
) {
    companion object {
        const val ID = "id"
        const val WORKOUT_ID = "workoutId"
    }
}
