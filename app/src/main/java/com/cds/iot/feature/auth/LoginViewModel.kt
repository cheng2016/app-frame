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
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message.asSharedFlow()

    private val _navigateMain = MutableSharedFlow<Unit>()
    val navigateMain: SharedFlow<Unit> = _navigateMain.asSharedFlow()

    val demoMode = authRepository.demoMode

    init {
        viewModelScope.launch {
            WeChatAuthBridge.events.collect { event ->
                when (event) {
                    is WeChatEvent.Code -> handleWeChatCode(event.code)
                    is WeChatEvent.Error -> _message.emit(event.message)
                }
            }
        }
    }

    fun login(phone: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            when (val result = authRepository.login(phone.trim(), password)) {
                is AppResult.Success -> _navigateMain.emit(Unit)
                is AppResult.Error -> _message.emit(result.message)
                AppResult.Loading -> Unit
            }
            _loading.value = false
        }
    }

    fun loginWithWeChatDemo() {
        viewModelScope.launch {
            _loading.value = true
            when (val result = authRepository.thirdLogin(openId = "demo-openid", nickname = "微信用户")) {
                is AppResult.Success -> _navigateMain.emit(Unit)
                is AppResult.Error -> _message.emit(result.message)
                AppResult.Loading -> Unit
            }
            _loading.value = false
        }
    }

    private fun handleWeChatCode(code: String) {
        viewModelScope.launch {
            // Auth code should be exchanged on backend; DemoMode maps code to a session.
            _loading.value = true
            when (val result = authRepository.thirdLogin(openId = code, nickname = "微信用户")) {
                is AppResult.Success -> _navigateMain.emit(Unit)
                is AppResult.Error -> _message.emit(result.message)
                AppResult.Loading -> Unit
            }
            _loading.value = false
        }
    }
}
