package com.afca.trackmypower.ui.workout

import android.database.sqlite.SQLiteException
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.afca.trackmypower.data.models.Workout
import com.afca.trackmypower.data.repositories.workout.WorkoutRepository
import com.afca.trackmypower.exceptions.WorkoutStatsException
import com.afca.trackmypower.helpers.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class WorkoutStatsFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: WorkoutRepository
) : ViewModel() {
    val id: Long = savedStateHandle["id"]!!

    private val _workout = repository.get(id)
    val workout = _workout.asLiveData()

    val date = MutableLiveData<LocalDate>()
    val year = MutableLiveData<Int>()
    val month = MutableLiveData<Int>()
    val week = MutableLiveData<Int>()
    val day = MutableLiveData<Int>()
    val startTime = MutableLiveData<LocalTime>()
    val endTime = MutableLiveData<LocalTime>()

    private val _updateState = MutableSharedFlow<Result<Int>>()
    val updateState = _updateState.asSharedFlow()

    init {
        viewModelScope.launch {
            _workout.collect { workout ->
                workout!!

                date.value = workout.date
                year.value = workout.year
                month.value = workout.month
                week.value = workout.week
                day.value = workout.day
                startTime.value = LocalTime.of(workout.startTime.hour, workout.startTime.minute)
                endTime.value = LocalTime.of(workout.endTime.hour, workout.endTime.minute)
            }
        }
    }

    fun updateWorkout() {
        CoroutineScope(Dispatchers.IO).launch {
            val nullError = when {
                year.value == null -> Constants.WORKOUT_STATS_ERR_YEAR
                month.value == null -> Constants.WORKOUT_STATS_ERR_MONTH
                week.value == null -> Constants.WORKOUT_STATS_ERR_WEEK
                day.value == null -> Constants.WORKOUT_STATS_ERR_DAY
                else -> null
            }

            if (nullError != null) {
                _updateState.emit(Result.failure(WorkoutStatsException(nullError.toString())))

                return@launch
            }

            val workout = Workout(
                id = id,
                date = date.value!!,
                year = year.value!!,
                month = month.value!!,
                week = week.value!!,
                day = day.value!!,
                startTime = startTime.value!!,
                endTime = endTime.value!!,
            )

            val logicError = when {
                workout.date.isAfter(LocalDate.now()) -> Constants.WORKOUT_STATS_ERR_DATE_FUTURE
                workout.year < 1 -> Constants.WORKOUT_STATS_ERR_YEAR_RANGE
                workout.month < 1 || workout.month > 12 -> Constants.WORKOUT_STATS_ERR_MONTH_RANGE
                workout.week < 1 || workout.week > 4 -> Constants.WORKOUT_STATS_ERR_WEEK_RANGE
                workout.day < 1 || workout.day > 7 -> Constants.WORKOUT_STATS_ERR_DAY_RANGE
                else -> null
            }

            if (logicError != null) {
                _updateState.emit(Result.failure(WorkoutStatsException(logicError.toString())))

                return@launch
            }

            try {
                repository.update(workout)

                _updateState.emit(Result.success(0))
            } catch (e: SQLiteException) {
                _updateState.emit(Result.failure(e))
            }
        }
    }
}