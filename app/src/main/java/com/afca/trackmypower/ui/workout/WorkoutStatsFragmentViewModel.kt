package com.afca.trackmypower.ui.workout

import android.database.sqlite.SQLiteException
import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.afca.trackmypower.data.models.Workout
import com.afca.trackmypower.data.repositories.workout.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WorkoutStatsFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: WorkoutRepository
) : ViewModel() {
    val id: Long = savedStateHandle["id"]!!
    private val _updateState = MutableSharedFlow<Result<String>>()

    val workoutLiveData = repository.get(id).asLiveData()
    val updateState = _updateState.asSharedFlow()

    fun updateWorkout(workout: Workout) {
        CoroutineScope(Dispatchers.IO).launch {
            if (workout.date.isAfter(LocalDate.now())) {
                _updateState.emit(Result.failure(Exception("New date can't be in the future")))
            } else if (workout.year < 1) {
                _updateState.emit(Result.failure(Exception("New year can't be less than 1")))
            } else if (workout.month < 1 || workout.month > 12) {
                _updateState.emit(Result.failure(Exception("New month must be between 1 and 12")))
            } else if (workout.week < 1 || workout.week > 4) {
                _updateState.emit(Result.failure(Exception("New week must be between 1 and 4")))
            } else if (workout.day < 1 || workout.day > 7) {
                _updateState.emit(Result.failure(Exception("New day must be between 1 and 7")))
            } else {
                try {
                    repository.update(workout)

                    _updateState.emit(Result.success(""))
                } catch (e: SQLiteException) {
                    _updateState.emit(Result.failure(e))
                }
            }
        }
    }
}