package com.cds.iot.feature.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cds.iot.core.result.AppResult
import com.cds.iot.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()
    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message.asSharedFlow()
    private val _done = MutableSharedFlow<Unit>()
    val done: SharedFlow<Unit> = _done.asSharedFlow()

    fun sendCode(phone: String) {
        viewModelScope.launch {
            when (val result = authRepository.sendCode(phone, "reset")) {
                is AppResult.Success -> _message.emit("验证码已发送（Demo: ${result.data}）")
                is AppResult.Error -> _message.emit(result.message)
                AppResult.Loading -> Unit
            }
        }
    }

    fun reset(phone: String, password: String, code: String) {
        viewModelScope.launch {
            _loading.value = true
            when (val result = authRepository.resetPassword(phone, password, code)) {
                is AppResult.Success -> {
                    _message.emit("密码已重置")
                    _done.emit(Unit)
                }
                is AppResult.Error -> _message.emit(result.message)
                AppResult.Loading -> Unit
            }
            _loading.value = false
        }
    }
}
