package com.afca.trackmypower

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class DataBindingFragment<VB: ViewDataBinding>: Fragment() {
    private var _binding: ViewDataBinding? = null
    abstract val layoutId: Int

    @Suppress("UNCHECKED_CAST")
    protected val binding: VB
        get() = (_binding as? VB) ?: throw IllegalStateException("Binding is null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        binding.lifecycleOwner = this

        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}
