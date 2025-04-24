package com.afca.trackmypower.ui.workout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afca.trackmypower.R
import com.afca.trackmypower.data.models.ExerciseWithWorkSets
import com.afca.trackmypower.data.repositories.workout.ExerciseRepository
import com.afca.trackmypower.data.repositories.workout.WorkoutRepository
import com.afca.trackmypower.helpers.Constants
import com.afca.trackmypower.helpers.extensions.debugLog
import com.afca.trackmypower.helpers.utils.Formatters.calculateDuration
import com.afca.trackmypower.helpers.utils.Formatters.formatDuration
import com.afca.trackmypower.helpers.utils.Formatters.formatTime
import com.afca.trackmypower.helpers.utils.StringProvider
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
    val id: Long = savedStateHandle["id"] ?: 86 // ToDo: get through NavArgs from workout list

    val date = MutableLiveData<String>()
    val position = MutableLiveData<String>()
    val time = MutableLiveData<String>()
    val exercisesWithWorkSets = MutableLiveData<List<ExerciseWithWorkSets>>()

    fun setStats() {
        viewModelScope.launch {
            workoutRepository.get(id).collect { workout ->
                workout?.let {
                    workout.toString().debugLog()

                    date.value = workout.date.format(
                        DateTimeFormatter.ofLocalizedDate(Constants.LOCAL_DATE_FORMAT_STYLE)
                    )
                    position.value = stringProvider.getString(
                        R.string.workout_position,
                        workout.year, workout.month, workout.week, workout.day
                    )
                    time.value = String.format(
                        Locale.getDefault(),
                        "%s - %s (%s)",
                        formatTime(workout.startTime),
                        formatTime(workout.endTime),
                        formatDuration(calculateDuration(workout.startTime, workout.endTime))
                    )

                    exerciseRepository.getWithWorkSetsByWorkoutId(workout.id).collect { exercises ->
                        exercisesWithWorkSets.value = exercises
                    }
                }
            }
        }
    }
}