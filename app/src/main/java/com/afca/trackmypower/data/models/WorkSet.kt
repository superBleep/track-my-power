package com.afca.trackmypower.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WorkSet (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = EXERCISE_ID)
    val exerciseId: Long,

    val number: Int,
    val workString: String,
    val score: Long
) {
    companion object {
        const val EXERCISE_ID = "exerciseID"
    }
}