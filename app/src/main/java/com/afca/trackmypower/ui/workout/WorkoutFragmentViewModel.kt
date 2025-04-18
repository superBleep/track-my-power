package com.afca.trackmypower.ui.workout

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.afca.trackmypower.data.repositories.workout.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkoutFragmentViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val repository: WorkoutRepository
) : ViewModel() {
    val id: Long = savedStateHandle["id"] ?: 1 // ToDo: get through NavArgs from workout list
    val workoutLiveData = repository.get(id).asLiveData()
}