package com.afca.trackmypower.data.repositories.workout

import com.afca.trackmypower.data.dao.WorkSetDAO
import com.afca.trackmypower.data.models.WorkSet
import javax.inject.Inject

class WorkSetRepository @Inject constructor(
    private val dao: WorkSetDAO
) {
    suspend fun insertAll(workSets: List<WorkSet>) = dao.insertAll(workSets)
}