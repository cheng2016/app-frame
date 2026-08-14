package com.cds.iot.feature.scene

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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
import com.cds.iot.data.repository.SceneRepository
import com.cds.iot.domain.model.SceneItem
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
class ScenesViewModel @Inject constructor(
    private val sceneRepository: SceneRepository,
) : ViewModel() {
    private val _scenes = MutableStateFlow<List<SceneItem>>(emptyList())
    val scenes = _scenes.asStateFlow()
    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            when (val result = sceneRepository.listScenes()) {
                is AppResult.Success -> _scenes.value = result.data
                is AppResult.Error -> _message.emit(result.message)
                AppResult.Loading -> Unit
            }
        }
    }

    fun add(name: String) {
        viewModelScope.launch {
            when (val result = sceneRepository.saveScene(name)) {
                is AppResult.Success -> {
                    _message.emit("场景已添加")
                    refresh()
                }
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
class ScenesFragment : Fragment(R.layout.activity_scenes) {
    private val viewModel: ScenesViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setupActionBar(getString(R.string.scenes)) { findNavController().navigateUp() }
        val nameEdit = view.findViewById<EditText>(R.id.editText)
        view.findViewById<View>(R.id.scene_add_btn).setOnClickListener {
            viewModel.add(nameEdit.text.toString().ifBlank { "新场景" })
        }
        view.findViewById<View>(R.id.scene_img).setOnLongClickListener {
            val scenes = viewModel.scenes.value
            if (scenes.isEmpty()) {
                Toast.makeText(requireContext(), R.string.empty_scenes, Toast.LENGTH_SHORT).show()
            } else {
                val names = scenes.map { it.name }.toTypedArray()
                AlertDialog.Builder(requireContext())
                    .setTitle("我的场景（长按删除）")
                    .setItems(names) { _, which ->
                        nameEdit.setText(scenes[which].name)
                    }
                    .setNeutralButton("删除首个") { _, _ ->
                        scenes.firstOrNull()?.let { viewModel.delete(it.id) }
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            }
            true
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.scenes.collect { list ->
                        list.firstOrNull()?.let { nameEdit.setText(it.name) }
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
