package com.afca.trackmypower.ui.workout

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.afca.trackmypower.DataBindingFragment
import com.afca.trackmypower.R
import com.afca.trackmypower.databinding.FragmentWorkoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutFragment: DataBindingFragment<FragmentWorkoutBinding>(), WorkoutListener {
    private val viewModel by viewModels<WorkoutFragmentViewModel>()

    override val layoutId: Int
        get() = R.layout.fragment_workout

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.listener = this
        viewModel.setStats()

        binding.imbLogout.setOnClickListener {
            findNavController().navigate(R.id.action_logout)
        }
    }

    override fun goToWorkoutStats(): Boolean {
        val action =
            WorkoutFragmentDirections.actionWorkoutFragmentToWorkoutStatsFragment(viewModel.id)
        findNavController().navigate(action)
        return true
    }
}

interface WorkoutListener {
    fun goToWorkoutStats(): Boolean
}