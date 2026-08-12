package com.cds.iot.feature.user

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.cds.iot.R
import com.cds.iot.core.result.AppResult
import com.cds.iot.data.repository.AuthRepository
import com.cds.iot.data.repository.UserRepository
import com.cds.iot.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePhoneViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()
    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()
    private val _done = MutableSharedFlow<Unit>()
    val done = _done.asSharedFlow()

    fun sendCode(phone: String) {
        viewModelScope.launch {
            when (val result = authRepository.sendCode(phone, "change_phone")) {
                is AppResult.Success -> _message.emit("验证码已发送（Demo: ${result.data}）")
                is AppResult.Error -> _message.emit(result.message)
                AppResult.Loading -> Unit
            }
        }
    }

    fun submit(phone: String, code: String) {
        viewModelScope.launch {
            _loading.value = true
            when (val result = userRepository.updatePhone(phone, code)) {
                is AppResult.Success -> {
                    _message.emit("手机号已更新")
                    _done.emit(Unit)
                }
                is AppResult.Error -> _message.emit(result.message)
                AppResult.Loading -> Unit
            }
            _loading.value = false
        }
    }
}

@AndroidEntryPoint
class ChangePhoneFragment : Fragment(R.layout.fragment_register) {
    private val viewModel: ChangePhoneViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentRegisterBinding.bind(view)
        binding.toolbar.title = getString(R.string.change_phone)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.passwordInput.hint = "可忽略"
        binding.passwordInput.isEnabled = false
        binding.registerButton.text = getString(R.string.confirm)
        binding.sendCodeButton.setOnClickListener {
            viewModel.sendCode(binding.phoneInput.text?.toString().orEmpty())
        }
        binding.registerButton.setOnClickListener {
            viewModel.submit(
                binding.phoneInput.text?.toString().orEmpty(),
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
