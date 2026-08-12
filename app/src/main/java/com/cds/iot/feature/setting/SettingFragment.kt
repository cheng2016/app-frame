package com.cds.iot.feature.setting

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.cds.iot.R
import com.cds.iot.data.repository.AuthRepository
import com.cds.iot.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    val demoMode = authRepository.demoMode
    private val _loggedOut = MutableSharedFlow<Unit>()
    val loggedOut = _loggedOut.asSharedFlow()
    private val _toast = MutableSharedFlow<String>()
    val toast = _toast.asSharedFlow()

    fun setDemoMode(enabled: Boolean) {
        viewModelScope.launch { authRepository.setDemoMode(enabled) }
    }

    fun clearCache() {
        viewModelScope.launch { _toast.emit("缓存已清理") }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _loggedOut.emit(Unit)
        }
    }

    suspend fun currentDemoMode(): Boolean = authRepository.demoMode.first()
}

@AndroidEntryPoint
class SettingFragment : Fragment(R.layout.fragment_setting) {
    private val viewModel: SettingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentSettingBinding.bind(view)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.itemNotify.setOnClickListener {
            findNavController().navigate(R.id.notifyFragment)
        }
        binding.itemUpdate.setOnClickListener {
            findNavController().navigate(R.id.updateFragment)
        }
        binding.itemClearCache.setOnClickListener { viewModel.clearCache() }
        binding.itemLogout.setOnClickListener { viewModel.logout() }

        viewLifecycleOwner.lifecycleScope.launch {
            binding.demoSwitch.isChecked = viewModel.currentDemoMode()
            binding.demoSwitch.setOnCheckedChangeListener { _, checked ->
                viewModel.setDemoMode(checked)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.toast.collect {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }
                launch {
                    viewModel.loggedOut.collect {
                        findNavController().navigate(
                            R.id.loginFragment,
                            null,
                            NavOptions.Builder()
                                .setPopUpTo(R.id.nav_root, true)
                                .build(),
                        )
                    }
                }
            }
        }
    }
}
