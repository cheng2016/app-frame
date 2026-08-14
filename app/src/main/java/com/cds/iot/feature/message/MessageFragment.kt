package com.cds.iot.feature.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.cds.iot.R
import com.cds.iot.core.result.AppResult
import com.cds.iot.data.repository.UserRepository
import com.cds.iot.domain.model.MessageItem
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
class MessageViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _messages = MutableStateFlow<List<MessageItem>>(emptyList())
    val messages = _messages.asStateFlow()
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
            when (val result = userRepository.messages()) {
                is AppResult.Success -> _messages.value = result.data
                is AppResult.Error -> _message.emit(result.message)
                AppResult.Loading -> Unit
            }
            _loading.value = false
        }
    }
}

@AndroidEntryPoint
class MessageFragment : Fragment(R.layout.fragment_message) {
    private val viewModel: MessageViewModel by viewModels()
    private val items = mutableListOf<MessageItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setupActionBar(getString(R.string.message), showBack = false)
        val listView = view.findViewById<ListView>(R.id.message_listview)
        val emptyLayout = view.findViewById<View>(R.id.empty_layout)
        val adapter = object : BaseAdapter() {
            override fun getCount(): Int = items.size
            override fun getItem(position: Int): MessageItem = items[position]
            override fun getItemId(position: Int): Long = position.toLong()
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val row = convertView ?: LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message, parent, false)
                val item = getItem(position)
                row.findViewById<TextView>(R.id.title).text =
                    item.title + if (item.read) "" else " · 未读"
                row.findViewById<TextView>(R.id.body).text = item.body
                row.findViewById<TextView>(R.id.time).text = item.time
                return row
            }
        }
        listView.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.messages.collect {
                        items.clear()
                        items.addAll(it)
                        adapter.notifyDataSetChanged()
                        emptyLayout.isVisible = it.isEmpty()
                        listView.isVisible = it.isNotEmpty()
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
