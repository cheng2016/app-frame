package com.cds.iot.feature.user

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
import com.cds.iot.data.repository.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()
    private val _nickname = MutableStateFlow("")
    val nickname = _nickname.asStateFlow()
    private val _phone = MutableStateFlow("")
    val phone = _phone.asStateFlow()

    init {
        viewModelScope.launch {
            when (val result = userRepository.profile()) {
                is AppResult.Success -> {
                    _nickname.value = result.data.nickname
                    _phone.value = result.data.phone
                }
                is AppResult.Error -> _message.emit(result.message)
                AppResult.Loading -> Unit
            }
        }
    }

    fun save(nickname: String) {
        viewModelScope.launch {
            when (val result = userRepository.updateNickname(nickname)) {
                is AppResult.Success -> _message.emit("资料已更新")
                is AppResult.Error -> _message.emit(result.message)
                AppResult.Loading -> Unit
            }
        }
    }
}

@AndroidEntryPoint
class UserDetailFragment : Fragment(R.layout.activity_user_detail) {
    private val viewModel: UserDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<View>(R.id.back_button).setOnClickListener {
            findNavController().navigateUp()
        }
        val nicknameEdit = view.findViewById<EditText>(R.id.imageView4)
        nicknameEdit.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                viewModel.save(nicknameEdit.text?.toString().orEmpty())
            }
        }
        view.findViewById<View>(R.id.phone_layout).setOnClickListener {
            findNavController().navigate(R.id.changePhoneFragment)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.nickname.collect {
                        if (nicknameEdit.text.isNullOrEmpty()) nicknameEdit.setText(it)
                    }
                }
                launch {
                    viewModel.phone.collect {
                        view.findViewById<TextView>(R.id.phone_tv).text = it.ifBlank { "未绑定" }
                    }
                }
                launch {
                    viewModel.message.collect {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
