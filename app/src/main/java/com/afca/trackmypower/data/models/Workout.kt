package com.afca.trackmypower.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Entity
data class Workout (
    @PrimaryKey(true)
    @ColumnInfo(name = ID)
    val id: Long = 0,

    val date: LocalDate,
    val year: Int,
    val month: Int,
    val week: Int,
    val day: Int,
    val startTime: LocalTime,
    val endTime: LocalTime
) {
    companion object {
        const val ID = "id"
    }
}