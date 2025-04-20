package com.afca.trackmypower.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.afca.trackmypower.data.models.WorkSet

@Dao
interface WorkSetDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(workSets: List<WorkSet>)
}