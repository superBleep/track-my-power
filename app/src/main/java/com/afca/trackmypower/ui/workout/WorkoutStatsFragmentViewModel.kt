package com.afca.trackmypower.ui.workout

import android.database.sqlite.SQLiteException
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.afca.trackmypower.helpers.Constants
import com.afca.trackmypower.helpers.utils.Formatters.formatDate
import com.afca.trackmypower.helpers.utils.Formatters.formatTime
import com.afca.trackmypower.data.LocalDatabaseConverters
import com.afca.trackmypower.data.models.Workout
import com.afca.trackmypower.data.repositories.workout.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class WorkoutStatsFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val repository: WorkoutRepository
) : ViewModel() {
    val id: Long = savedStateHandle["id"]!!

    private val _workout = repository.get(id)
    val workout = _workout.asLiveData()

    val date = MutableLiveData<String>()
    val year = MutableLiveData<String>()
    val month = MutableLiveData<String>()
    val week = MutableLiveData<String>()
    val day = MutableLiveData<String>()
    val startTime = MutableLiveData<String>()
    val endTime = MutableLiveData<String>()

    private val _updateState = MutableSharedFlow<Result<String>>()
    val updateState = _updateState.asSharedFlow()

    init {
        viewModelScope.launch {
            _workout.collect { value ->
                value!!

                date.value = formatDate(value.date)
                year.value = value.year.toString()
                month.value = value.month.toString()
                week.value = value.week.toString()
                day.value = value.day.toString()
                startTime.value = formatTime(LocalTime.of(value.startTime.hour, value.startTime.minute))
                endTime.value = formatTime(LocalTime.of(value.endTime.hour, value.endTime.minute))
            }
        }
    }

    fun updateWorkout() {
        CoroutineScope(Dispatchers.IO).launch {
            val nullError = when {
                year.value.isNullOrBlank() -> "year"
                month.value.isNullOrBlank() -> "month"
                week.value.isNullOrBlank() -> "week"
                day.value.isNullOrBlank() -> "day"
                else -> null
            }

            if (!nullError.isNullOrBlank()) {
                _updateState.emit(Result.failure(Exception("New $nullError can't be null")))
                return@launch
            }

            val workout = Workout(
                id = id,
                date = LocalDate.parse(
                    date.value,
                    DateTimeFormatter.ofLocalizedDate(Constants.LOCAL_DATE_FORMAT_STYLE)
                ),
                year = year.value!!.toInt(),
                month = month.value!!.toInt(),
                week = week.value!!.toInt(),
                day = day.value!!.toInt(),
                startTime = LocalDatabaseConverters.toLocalTime(startTime.value!!),
                endTime = LocalDatabaseConverters.toLocalTime(endTime.value!!),
            )

            val logicError = when {
                workout.date.isAfter(LocalDate.now()) -> "New date can't be in the future"
                workout.year < 1 -> "New year can't be less than 1"
                workout.month < 1 || workout.month > 12 -> "New month must be between 1 and 12"
                workout.week < 1 || workout.week > 4 -> "New week must be between 1 and 4"
                workout.day < 1 || workout.day > 7 -> "New day must be between 1 and 7"
                else -> null
            }

            if (!logicError.isNullOrBlank()) {
                _updateState.emit(Result.failure(Exception(logicError)))
                return@launch
            }

            try {
                repository.update(workout)

                _updateState.emit(Result.success(""))
            } catch (e: SQLiteException) {
                _updateState.emit(Result.failure(e))
            }
        }
    }
}