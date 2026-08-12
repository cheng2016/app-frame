package com.cds.iot.feature.device

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
import com.cds.iot.data.repository.DeviceRepository
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
class AddDeviceViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository,
) : ViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()
    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()
    private val _done = MutableSharedFlow<Unit>()
    val done = _done.asSharedFlow()

    fun add(name: String, type: String) {
        viewModelScope.launch {
            _loading.value = true
            when (val result = deviceRepository.addDevice(name, type.ifBlank { "通用设备" })) {
                is AppResult.Success -> {
                    _message.emit("已添加 ${result.data.name}")
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
class AddDeviceFragment : Fragment(R.layout.fragment_simple_form) {
    private val viewModel: AddDeviceViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentSimpleFormBinding.bind(view)
        binding.toolbar.title = getString(R.string.add_device)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.fieldOneLayout.hint = "设备名称"
        binding.fieldTwoLayout.hint = "设备类型"
        binding.submitButton.setOnClickListener {
            viewModel.add(
                binding.fieldOne.text?.toString().orEmpty(),
                binding.fieldTwo.text?.toString().orEmpty(),
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
