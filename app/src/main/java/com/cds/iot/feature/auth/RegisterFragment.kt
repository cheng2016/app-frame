package com.cds.iot.feature.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.cds.iot.R
import com.cds.iot.ui.setupActionBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.activity_register) {
    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setupActionBar(getString(R.string.register)) { findNavController().navigateUp() }
        val phone = view.findViewById<AppCompatEditText>(R.id.name_edit)
        val password = view.findViewById<AppCompatEditText>(R.id.password_edit)
        val code = view.findViewById<AppCompatEditText>(R.id.code_edit)

        view.findViewById<AppCompatTextView>(R.id.getcode_tv).setOnClickListener {
            viewModel.sendCode(phone.text?.toString().orEmpty())
        }
        view.findViewById<AppCompatButton>(R.id.register_submit_btn).setOnClickListener {
            viewModel.register(
                phone.text?.toString().orEmpty(),
                password.text?.toString().orEmpty(),
                code.text?.toString().orEmpty(),
            )
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.message.collect {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }
                launch {
                    viewModel.done.collect {
                        findNavController().navigate(R.id.action_register_to_main)
                    }
                }
            }
        }
    }
}
