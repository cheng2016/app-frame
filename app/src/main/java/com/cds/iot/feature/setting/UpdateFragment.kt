package com.cds.iot.feature.setting

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
import com.cds.iot.BuildConfig
import com.cds.iot.R
import com.cds.iot.core.result.AppResult
import com.cds.iot.data.repository.UserRepository
import com.cds.iot.databinding.FragmentSimpleFormBinding
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
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()
    private val _content = MutableStateFlow("正在检查更新…")
    val content = _content.asStateFlow()
    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    init {
        check()
    }

    fun check() {
        viewModelScope.launch {
            _loading.value = true
            when (val result = userRepository.checkUpdate(BuildConfig.VERSION_CODE)) {
                is AppResult.Success -> {
                    val info = result.data
                    _content.value = "当前版本 ${BuildConfig.VERSION_NAME}\n最新 ${info.versionName}\n\n${info.changelog}"
                    _message.emit(if (info.versionCode > BuildConfig.VERSION_CODE) "发现新版本" else "已是最新版本")
                }
                is AppResult.Error -> {
                    _content.value = result.message
                    _message.emit(result.message)
                }
                AppResult.Loading -> Unit
            }
            _loading.value = false
        }
    }
}

@AndroidEntryPoint
class UpdateFragment : Fragment(R.layout.fragment_simple_form) {
    private val viewModel: UpdateViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentSimpleFormBinding.bind(view)
        binding.toolbar.title = getString(R.string.check_update)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.fieldOneLayout.isVisible = false
        binding.fieldTwoLayout.isVisible = false
        binding.submitButton.text = "重新检查"
        binding.contentText.isVisible = true
        binding.submitButton.setOnClickListener { viewModel.check() }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.loading.collect { binding.progress.isVisible = it } }
                launch { viewModel.content.collect { binding.contentText.text = it } }
                launch {
                    viewModel.message.collect {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
