package com.cds.iot.feature.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.cds.iot.R
import com.cds.iot.databinding.FragmentForgetBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgetFragment : Fragment(R.layout.fragment_forget) {
    private val viewModel: ForgetViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentForgetBinding.bind(view)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.sendCodeButton.setOnClickListener {
            viewModel.sendCode(binding.phoneInput.text?.toString().orEmpty())
        }
        binding.submitButton.setOnClickListener {
            viewModel.reset(
                binding.phoneInput.text?.toString().orEmpty(),
                binding.passwordInput.text?.toString().orEmpty(),
                binding.codeInput.text?.toString().orEmpty(),
            )
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.loading.collect { binding.progress.isVisible = it } }
                launch {
                    viewModel.message.collect {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }
                launch { viewModel.done.collect { findNavController().navigateUp() } }
            }
        }
    }
}
