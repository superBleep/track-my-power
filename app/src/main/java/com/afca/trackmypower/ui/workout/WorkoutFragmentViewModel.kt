package com.afca.trackmypower.ui.workout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afca.trackmypower.adapters.ExerciseAdapter
import com.afca.trackmypower.data.models.ExerciseWithWorkSets
import com.afca.trackmypower.data.repositories.workout.WorkoutRepository
import com.afca.trackmypower.helpers.utils.Formatters.calculateDuration
import com.afca.trackmypower.helpers.utils.Formatters.formatDuration
import com.afca.trackmypower.helpers.utils.Formatters.formatTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class WorkoutFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val workoutRepository: WorkoutRepository
) : ViewModel() {
    val id: Long = savedStateHandle["id"] ?: 86 // ToDo: get through NavArgs from workout list

    private var exerciseAndWorkSetList = mutableListOf<ExerciseWithWorkSets>()
    var exerciseAdapter = ExerciseAdapter(exerciseAndWorkSetList)

    private val _date = MutableLiveData<LocalDate>()
    val date: LiveData<LocalDate>
        get() = _date

    private val _position = MutableLiveData<Array<Int>>()
    val position: LiveData<Array<Int>>
        get() = _position

    private val _time = MutableLiveData<Array<String>>()
    val time: LiveData<Array<String>>
        get() = _time

    init {
        setStats()
    }

    fun setStats() {
        viewModelScope.launch {
            workoutRepository.getWithExercises(id).collect { workoutAndExercises ->
                workoutAndExercises?.let {
                    _date.value = workoutAndExercises.workout.date
                    _position.value = arrayOf(workoutAndExercises.workout.year,
                        workoutAndExercises.workout.month, workoutAndExercises.workout.week,
                        workoutAndExercises.workout.day)
                    _time.value = arrayOf(formatTime(workoutAndExercises.workout.startTime),
                        formatTime(workoutAndExercises.workout.endTime),
                        formatDuration(calculateDuration(
                            workoutAndExercises.workout.startTime,
                            workoutAndExercises.workout.endTime)))

                    exerciseAndWorkSetList.clear()
                    exerciseAndWorkSetList.addAll(workoutAndExercises.exercises)

                    exerciseAdapter.notifyItemRangeChanged(0, exerciseAndWorkSetList.size)
                }
            }
        }
    }
}
