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
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.cds.iot.BuildConfig
import com.cds.iot.R
import com.cds.iot.data.repository.AuthRepository
import com.cds.iot.ui.setupActionBar
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
class SettingFragment : Fragment(R.layout.activity_setting) {
    private val viewModel: SettingViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setupActionBar(getString(R.string.setting)) { findNavController().navigateUp() }
        view.findViewById<TextView>(R.id.version_name_tv).text = "v${BuildConfig.VERSION_NAME}"
        view.findViewById<TextView>(R.id.file_size_tv).text = "0KB"

        view.findViewById<View>(R.id.message_notify_layout).setOnClickListener {
            findNavController().navigate(R.id.notifyFragment)
        }
        view.findViewById<View>(R.id.modify_password_layout).setOnClickListener {
            findNavController().navigate(R.id.forgetFragment)
        }
        view.findViewById<View>(R.id.update_layout).setOnClickListener {
            findNavController().navigate(R.id.updateFragment)
        }
        view.findViewById<View>(R.id.clean_cache_layout).setOnClickListener {
            viewModel.clearCache()
            view.findViewById<TextView>(R.id.file_size_tv).text = "0KB"
        }
        view.findViewById<View>(R.id.logout_layout).setOnClickListener { viewModel.logout() }

        viewLifecycleOwner.lifecycleScope.launch {
            val demo = viewModel.currentDemoMode()
            if (!demo) {
                // Keep demo mode available via long-press on version row.
            }
            view.findViewById<View>(R.id.update_layout).setOnLongClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    val enabled = !viewModel.currentDemoMode()
                    viewModel.setDemoMode(enabled)
                    Toast.makeText(
                        requireContext(),
                        if (enabled) "已开启 Demo 模式" else "已关闭 Demo 模式",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
                true
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
