package com.afca.trackmypower.ui.workout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afca.trackmypower.R
import com.afca.trackmypower.data.models.WorkoutWithExercises
import com.afca.trackmypower.data.repositories.workout.WorkoutRepository
import com.afca.trackmypower.helpers.Constants
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
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    val id: Long = savedStateHandle["id"] ?: 86 // ToDo: get through NavArgs from workout list

    val date = MutableLiveData<String>()
    val position = MutableLiveData<String>()
    val time = MutableLiveData<String>()
    val workout = MutableLiveData<WorkoutWithExercises?>()

    fun setStats() {
        viewModelScope.launch {
            workoutRepository.getWithExercises(id).collect { packed ->
                packed?.let {
                    date.value = packed.workout.date.format(
                        DateTimeFormatter.ofLocalizedDate(Constants.LOCAL_DATE_FORMAT_STYLE)
                    )
                    position.value = stringProvider.getString(
                        R.string.workout_position,
                        packed.workout.year, packed.workout.month, packed.workout.week, packed.workout.day
                    )
                    time.value = String.format(
                        Locale.getDefault(),
                        "%s - %s (%s)",
                        formatTime(packed.workout.startTime),
                        formatTime(packed.workout.endTime),
                        formatDuration(calculateDuration(packed.workout.startTime, packed.workout.endTime))
                    )

                    workout.value = packed
                }
            }
        }
    }
}