package com.cds.iot.feature.user

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
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
import com.cds.iot.ui.setupActionBar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePhoneViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
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
            when (val result = userRepository.updatePhone(phone, code)) {
                is AppResult.Success -> {
                    _message.emit("手机号已更新")
                    _done.emit(Unit)
                }
                is AppResult.Error -> _message.emit(result.message)
                AppResult.Loading -> Unit
            }
        }
    }
}

@AndroidEntryPoint
class ChangePhoneFragment : Fragment(R.layout.activity_change_phone) {
    private val viewModel: ChangePhoneViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setupActionBar(getString(R.string.change_phone)) { findNavController().navigateUp() }
        val phone = view.findViewById<AppCompatEditText>(R.id.phone_edit)
        val code = view.findViewById<AppCompatEditText>(R.id.code_edit)
        view.findViewById<AppCompatTextView>(R.id.getcode_tv).setOnClickListener {
            viewModel.sendCode(phone.text?.toString().orEmpty())
        }
        view.findViewById<AppCompatButton>(R.id.change_phone_submit_btn).setOnClickListener {
            viewModel.submit(
                phone.text?.toString().orEmpty(),
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
                launch { viewModel.done.collect { findNavController().navigateUp() } }
            }
        }
    }
}
