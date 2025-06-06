package com.afca.trackmypower.ui.auth

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.afca.trackmypower.R
import com.afca.trackmypower.data.models.User
import com.afca.trackmypower.databinding.FragmentRegisterBinding
import java.text.SimpleDateFormat
import java.util.*

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val genderOptions = listOf("Male", "Female", "Other")
        val genderAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            genderOptions
        )
        binding.actvGender.setAdapter(genderAdapter)

        val experienceOptions = listOf("Beginner", "Intermediate", "Experienced")
        val expAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            experienceOptions
        )
        binding.actvExperience.setAdapter(expAdapter)

        binding.etBirthDate.setOnClickListener {

            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                requireContext(),
                { _, pickedYear, pickedMonth, pickedDayOfMonth ->

                    val selectedCal = Calendar.getInstance().apply {
                        set(pickedYear, pickedMonth, pickedDayOfMonth)
                    }
                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                    binding.etBirthDate.setText(sdf.format(selectedCal.time))
                },
                year, month, day
            ).show()
        }

        viewModel.registrationResult.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                Toast.makeText(requireContext(), "Registration successful", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Registration failed (email may already exist)",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

        binding.btnRegister.setOnClickListener {
            binding.tilFirstName.error = null
            binding.tilLastName.error = null
            binding.tilEmail.error = null
            binding.tilPassword.error = null
            binding.tilBirthDate.error = null
            binding.tilGender.error = null
            binding.tilHeight.error = null
            binding.tilWeight.error = null
            binding.tilAvatar.error = null
            binding.tilExperience.error = null

            val firstName = binding.etFirstName.text.toString().trim()
            val lastName = binding.etLastName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString()
            val birthDate = binding.etBirthDate.text.toString().trim()
            val gender = binding.actvGender.text.toString().trim()
            val heightText = binding.etHeight.text.toString().trim()
            val weightText = binding.etWeight.text.toString().trim()
            val avatar = binding.etAvatar.text.toString().trim()
            val experience = binding.actvExperience.text.toString().trim()

            var valid = true

            if (firstName.isEmpty()) {
                binding.tilFirstName.error = "First name is required"
                valid = false
            }

            if (lastName.isEmpty()) {
                binding.tilLastName.error = "Last name is required"
                valid = false
            }

            if (email.isEmpty()) {
                binding.tilEmail.error = "Email is required"
                valid = false
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.tilEmail.error = "Enter a valid email"
                valid = false
            }

            if (password.isEmpty()) {
                binding.tilPassword.error = "Password is required"
                valid = false
            }

            if (birthDate.isEmpty()) {
                binding.tilBirthDate.error = "Birth date is required"
                valid = false
            } else {
            }

            val allowedGenders = listOf("Male", "Female", "Other")
            if (gender.isEmpty()) {
                binding.tilGender.error = "Select gender"
                valid = false
            } else if (gender !in allowedGenders) {
                binding.tilGender.error = "Choose Male, Female, or Other"
                valid = false
            }

            val height = heightText.toFloatOrNull()
            if (heightText.isEmpty()) {
                binding.tilHeight.error = "Height is required"
                valid = false
            } else if (height == null || height <= 0f) {
                binding.tilHeight.error = "Enter a valid height"
                valid = false
            }

            val weight = weightText.toFloatOrNull()
            if (weightText.isEmpty()) {
                binding.tilWeight.error = "Weight is required"
                valid = false
            } else if (weight == null || weight <= 0f) {
                binding.tilWeight.error = "Enter a valid weight"
                valid = false
            }

            if (avatar.isEmpty()) {
                binding.tilAvatar.error = "Avatar URL is required"
                valid = false
            }

            val allowedExp = listOf("Beginner", "Intermediate", "Experienced")
            if (experience.isEmpty()) {
                binding.tilExperience.error = "Select experience level"
                valid = false
            } else if (experience !in allowedExp) {
                binding.tilExperience.error = "Choose Beginner, Intermediate, or Experienced"
                valid = false
            }

            if (!valid) {
                return@setOnClickListener
            }

            val expInt = when (experience) {
                "Beginner" -> 0
                "Intermediate" -> 1
                "Experienced" -> 2
                else -> 0
            }

            val newUser = User(
                id = UUID.randomUUID().toString(),
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = password,
                birthDate = birthDate,
                gender = gender,
                height = height!!,
                weight = weight!!,
                avatar = avatar,
                experience = expInt
            )

            viewModel.registerUser(newUser)
        }

        binding.tvLoginLink.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}