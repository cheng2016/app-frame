package com.cds.iot.feature.setting

import android.os.Bundle
import android.view.View
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
import com.cds.iot.BuildConfig
import com.cds.iot.R
import com.cds.iot.core.result.AppResult
import com.cds.iot.data.repository.UserRepository
import com.cds.iot.ui.setupActionBar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _content = MutableStateFlow("当前已经是最新版本")
    val content = _content.asStateFlow()
    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    init {
        check()
    }

    fun check() {
        viewModelScope.launch {
            when (val result = userRepository.checkUpdate(BuildConfig.VERSION_CODE)) {
                is AppResult.Success -> {
                    val info = result.data
                    _content.value = if (info.versionCode > BuildConfig.VERSION_CODE) {
                        "发现新版本 ${info.versionName}\n${info.changelog}"
                    } else {
                        "当前已经是最新版本"
                    }
                    _message.emit(
                        if (info.versionCode > BuildConfig.VERSION_CODE) "发现新版本" else "已是最新版本",
                    )
                }
                is AppResult.Error -> {
                    _content.value = result.message
                    _message.emit(result.message)
                }
                AppResult.Loading -> Unit
            }
        }
    }
}

@AndroidEntryPoint
class UpdateFragment : Fragment(R.layout.activity_update) {
    private val viewModel: UpdateViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setupActionBar(getString(R.string.check_update)) { findNavController().navigateUp() }
        val status = view.findViewById<TextView>(R.id.update_status_tv)
        status.setOnClickListener { viewModel.check() }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.content.collect { status.text = it } }
                launch {
                    viewModel.message.collect {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
