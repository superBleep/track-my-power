package com.afca.trackmypower.ui.workout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.afca.trackmypower.helpers.Constants
import com.afca.trackmypower.R
import com.afca.trackmypower.data.models.Exercise
import com.afca.trackmypower.data.repositories.workout.ExerciseRepository
import com.afca.trackmypower.helpers.utils.StringProvider
import com.afca.trackmypower.helpers.utils.Formatters.calculateDuration
import com.afca.trackmypower.helpers.utils.Formatters.formatDuration
import com.afca.trackmypower.helpers.utils.Formatters.formatTime
import com.afca.trackmypower.data.repositories.workout.WorkoutRepository
import com.afca.trackmypower.helpers.extensions.debugLog
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WorkoutFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val stringProvider: StringProvider,
    private val workoutRepository: WorkoutRepository,
    private val exerciseRepository: ExerciseRepository
) : ViewModel() {
    val id: Long = savedStateHandle["id"] ?: 1 // ToDo: get through NavArgs from workout list

    private val _workout = workoutRepository.getWithExercises(id)
    val workout = _workout.asLiveData()

    val date = MutableLiveData<String>()
    val position = MutableLiveData<String>()
    val time = MutableLiveData<String>()
    val exercises = MutableLiveData<List<Exercise>>()

    init {
        viewModelScope.launch {
            _workout.collect { value ->
                value!!

                date.value = value.workout.date.format(
                    DateTimeFormatter.ofLocalizedDate(Constants.LOCAL_DATE_FORMAT_STYLE)
                )
                position.value = stringProvider.getString(
                    R.string.workout_position,
                    value.workout.year, value.workout.month, value.workout.week, value.workout.day
                )
                time.value = String.format(
                    Locale.getDefault(),
                    "%s - %s (%s)",
                    formatTime(value.workout.startTime),
                    formatTime(value.workout.endTime),
                    formatDuration(calculateDuration(value.workout.startTime, value.workout.endTime))
                )

                exercises.value = value.exercises

                value.exercises.forEach { exercise ->
                    exerciseRepository.getWithWorkSets(exercise.id).collect { value ->
                        value.toString().debugLog()
                    }
                }
            }
        }
    }
}