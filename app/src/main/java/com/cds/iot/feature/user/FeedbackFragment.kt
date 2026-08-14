package com.cds.iot.feature.user

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
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
import com.cds.iot.ui.setupActionBar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _message = MutableSharedFlow<String>()
    val message = _message.asSharedFlow()
    private val _done = MutableSharedFlow<Unit>()
    val done = _done.asSharedFlow()

    fun submit(content: String, contact: String) {
        viewModelScope.launch {
            when (val result = userRepository.feedback(content, contact)) {
                is AppResult.Success -> {
                    _message.emit("感谢反馈")
                    _done.emit(Unit)
                }
                is AppResult.Error -> _message.emit(result.message)
                AppResult.Loading -> Unit
            }
        }
    }
}

@AndroidEntryPoint
class FeedbackFragment : Fragment(R.layout.activity_feedback) {
    private val viewModel: FeedbackViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setupActionBar(getString(R.string.feedback)) { findNavController().navigateUp() }
        val content = view.findViewById<EditText>(R.id.sosMsg)
        view.findViewById<AppCompatButton>(R.id.feedback_submit_btn).setOnClickListener {
            viewModel.submit(content.text?.toString().orEmpty(), "")
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
