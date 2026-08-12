package com.cds.iot.feature.scene

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.cds.iot.R
import com.cds.iot.core.result.AppResult
import com.cds.iot.data.repository.SceneRepository
import com.cds.iot.databinding.FragmentListBinding
import com.cds.iot.databinding.ItemSceneBinding
import com.cds.iot.domain.model.SceneItem
import com.cds.iot.ui.SimpleListAdapter
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScenesViewModel @Inject constructor(
    private val sceneRepository: SceneRepository,
) : ViewModel() {
    private val _scenes = MutableStateFlow<List<SceneItem>>(emptyList())
    val scenes = _scenes.asStateFlow()
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()
    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _loading.value = true
            when (val result = sceneRepository.listScenes()) {
                is AppResult.Success -> _scenes.value = result.data
                is AppResult.Error -> _message.emit(result.message)
                AppResult.Loading -> Unit
            }
            _loading.value = false
        }
    }

    fun add(name: String) {
        viewModelScope.launch {
            when (val result = sceneRepository.saveScene(name)) {
                is AppResult.Success -> refresh()
                is AppResult.Error -> _message.emit(result.message)
                AppResult.Loading -> Unit
            }
        }
    }

    fun delete(id: String) {
        viewModelScope.launch {
            when (val result = sceneRepository.deleteScene(id)) {
                is AppResult.Success -> refresh()
                is AppResult.Error -> _message.emit(result.message)
                AppResult.Loading -> Unit
            }
        }
    }
}

@AndroidEntryPoint
class ScenesFragment : Fragment(R.layout.fragment_list) {
    private val viewModel: ScenesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentListBinding.bind(view)
        binding.toolbar.title = getString(R.string.scenes)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        val adapter = SimpleListAdapter(
            inflate = ItemSceneBinding::inflate,
            bind = { itemBinding, item: SceneItem ->
                itemBinding.name.text = item.name
                itemBinding.meta.text = "${item.deviceCount} 个设备 · ${if (item.enabled) "已启用" else "未启用"}"
                itemBinding.root.setOnLongClickListener {
                    viewModel.delete(item.id)
                    true
                }
            },
        )
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        binding.list.adapter = adapter
        binding.fab.setOnClickListener {
            val input = EditText(requireContext())
            AlertDialog.Builder(requireContext())
                .setTitle("新建场景")
                .setView(input)
                .setPositiveButton(R.string.confirm) { _, _ ->
                    viewModel.add(input.text.toString().ifBlank { "新场景" })
                }
                .setNegativeButton(R.string.cancel, null)
                .show()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.scenes.collect {
                        adapter.submit(it)
                        binding.emptyView.isVisible = it.isEmpty()
                    }
                }
                launch { viewModel.loading.collect { binding.progress.isVisible = it } }
                launch {
                    viewModel.message.collect {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
