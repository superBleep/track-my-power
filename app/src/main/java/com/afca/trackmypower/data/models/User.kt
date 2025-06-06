package com.afca.trackmypower.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val birthDate: String,
    val gender: String,
    val height: Float,
    val weight: Float,
    val avatar: String,
    val experience: Int
)