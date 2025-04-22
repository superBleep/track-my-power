package com.afca.trackmypower.ui.workout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.afca.trackmypower.R
import com.afca.trackmypower.adapters.ExerciseAdapter
import com.afca.trackmypower.adapters.WorkSetAdapter
import com.afca.trackmypower.data.models.WorkSet
import com.afca.trackmypower.databinding.FragmentWorkoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkoutFragment : Fragment(), WorkoutListener {
    private var _binding: FragmentWorkoutBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<WorkoutFragmentViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_workout, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.listener = this

        viewModel.exercisesWithWorkSets.observe(viewLifecycleOwner) { exercisesWithWorkSets ->
            val exercisesRV = view.findViewById<RecyclerView>(R.id.rv_exercises)

            val exercisesRVAdapter = ExerciseAdapter(exercisesWithWorkSets)
            val exercisesRVLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            exercisesRV.apply {
                this.adapter = exercisesRVAdapter
                this.layoutManager = exercisesRVLayoutManager
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

    override fun goToWorkoutStats(): Boolean {
        val action = WorkoutFragmentDirections.actionWorkoutFragmentToWorkoutStatsFragment(viewModel.id)
        findNavController().navigate(action)

        return true
    }
}

interface WorkoutListener {
    fun goToWorkoutStats(): Boolean
}