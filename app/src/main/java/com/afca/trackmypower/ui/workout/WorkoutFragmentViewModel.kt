package com.afca.trackmypower.ui.workout

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.afca.trackmypower.helpers.Constants
import com.afca.trackmypower.R
import com.afca.trackmypower.helpers.utils.StringProvider
import com.afca.trackmypower.helpers.utils.Formatters.calculateDuration
import com.afca.trackmypower.helpers.utils.Formatters.formatDuration
import com.afca.trackmypower.helpers.utils.Formatters.formatTime
import com.afca.trackmypower.data.repositories.workout.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WorkoutFragmentViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val stringProvider: StringProvider,
    private val repository: WorkoutRepository
) : ViewModel() {
    val id: Long = savedStateHandle["id"] ?: 1 // ToDo: get through NavArgs from workout list

    private val _workout = repository.get(id)
    val workout = _workout.asLiveData()

    val date = MutableLiveData<String>()
    val position = MutableLiveData<String>()
    val time = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            _workout.collect { value ->
                value!!

                date.value = value.date.format(
                    DateTimeFormatter.ofLocalizedDate(Constants.LOCAL_DATE_FORMAT_STYLE)
                )
                position.value = stringProvider.getString(
                    R.string.workout_position,
                    value.year, value.month, value.week, value.day
                )
                time.value = String.format(
                    Locale.getDefault(),
                    "%s - %s (%s)",
                    formatTime(value.startTime),
                    formatTime(value.endTime),
                    formatDuration(calculateDuration(value.startTime, value.endTime))
                )
            }
        }
    }
}