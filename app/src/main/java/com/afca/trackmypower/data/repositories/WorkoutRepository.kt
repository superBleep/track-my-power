package com.afca.trackmypower.data.repositories

import com.afca.trackmypower.ApplicationController
import com.afca.trackmypower.data.models.Workout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object WorkoutRepository {
    fun insert(workout: Workout, onFinish: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            ApplicationController.instance?.localDatabase?.workoutDAO?.insert(workout)

            withContext(Dispatchers.Main) {
                onFinish()
            }
        }
    }

    fun get(id: Long, onFinish: (Workout?) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val res = ApplicationController.instance?.localDatabase?.workoutDAO?.get(id)

            withContext(Dispatchers.Main) {
                onFinish(res)
            }
        }
    }

    fun update(workout: Workout, onFinish: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            ApplicationController.instance?.localDatabase?.workoutDAO?.update(workout)

            withContext(Dispatchers.Main) {
                onFinish()
            }
        }
    }
}