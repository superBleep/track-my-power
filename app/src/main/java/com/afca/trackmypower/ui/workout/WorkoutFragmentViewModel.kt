package com.afca.trackmypower.ui.workout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afca.trackmypower.R
import com.afca.trackmypower.adapters.ExerciseAdapter
import com.afca.trackmypower.data.models.ExerciseWithWorkSets
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

    private var exerciseAndWorkSetList = mutableListOf<ExerciseWithWorkSets>()
    var exerciseAdapter = ExerciseAdapter(exerciseAndWorkSetList)

    private val _stats = MutableLiveData<Map<String, String>>()
    val stats: LiveData<Map<String, String>>
        get() = _stats

    init {
        setStats()
    }

    fun setStats() {
        viewModelScope.launch {
            workoutRepository.getWithExercises(id).collect { workoutAndExercises ->
                workoutAndExercises?.let {
                    val dateString = workoutAndExercises.workout.date.format(
                        DateTimeFormatter.ofLocalizedDate(Constants.LOCAL_DATE_FORMAT_STYLE)
                    )
                    val positionString = stringProvider.getString(
                        R.string.workout_position,
                        workoutAndExercises.workout.year, workoutAndExercises.workout.month,
                        workoutAndExercises.workout.week, workoutAndExercises.workout.day
                    )
                    val timeString = String.format(
                        Locale.getDefault(),
                        "%s - %s (%s)",
                        formatTime(workoutAndExercises.workout.startTime),
                        formatTime(workoutAndExercises.workout.endTime),
                        formatDuration(calculateDuration(
                            workoutAndExercises.workout.startTime,
                            workoutAndExercises.workout.endTime))
                    )

                    _stats.value = mapOf(
                        "date" to dateString,
                        "position" to positionString,
                        "time" to timeString
                    )

                    exerciseAndWorkSetList.clear()
                    exerciseAndWorkSetList.addAll(workoutAndExercises.exercises)

                    exerciseAdapter.notifyItemRangeChanged(0, exerciseAndWorkSetList.size)
                }
            }
        }
    }
}
