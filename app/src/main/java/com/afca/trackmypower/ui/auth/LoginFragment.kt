package com.afca.trackmypower.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.afca.trackmypower.R
import com.afca.trackmypower.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loginResult.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                findNavController().navigate(R.id.action_loginFragment_to_workoutFragment)
            } else {
                Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        })

        binding.btnLogin.setOnClickListener {
            val emailInput = binding.etEmail.text.toString().trim()
            val passwordInput = binding.etPassword.text.toString()

            if (emailInput.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(requireContext(), "Enter both email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.loginWithEmailAndPassword(emailInput, passwordInput)
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}