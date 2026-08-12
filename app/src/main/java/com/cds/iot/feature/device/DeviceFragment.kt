package com.cds.iot.feature.device

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.cds.iot.R
import com.cds.iot.databinding.FragmentDeviceBinding
import com.cds.iot.databinding.ItemDeviceBinding
import com.cds.iot.domain.model.DeviceItem
import com.cds.iot.ui.SimpleListAdapter
import com.cds.iot.ui.findRootNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DeviceFragment : Fragment(R.layout.fragment_device) {
    private val viewModel: DeviceViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentDeviceBinding.bind(view)
        val adapter = SimpleListAdapter(
            inflate = ItemDeviceBinding::inflate,
            bind = { itemBinding, item: DeviceItem ->
                itemBinding.name.text = item.name
                itemBinding.meta.text = "${item.type} · ${item.room}"
                itemBinding.status.text = if (item.online) "在线" else "离线"
                itemBinding.status.setTextColor(
                    requireContext().getColor(
                        if (item.online) R.color.status_online else R.color.status_offline,
                    ),
                )
                itemBinding.root.setOnLongClickListener {
                    viewModel.delete(item.id)
                    true
                }
            },
        )
        binding.deviceList.layoutManager = LinearLayoutManager(requireContext())
        binding.deviceList.adapter = adapter
        binding.refresh.setOnRefreshListener { viewModel.refresh() }
        binding.toolbar.setOnMenuItemClickListener { item ->
            val rootNav = findRootNavController()
            when (item.itemId) {
                R.id.action_add -> rootNav.navigate(R.id.action_main_to_add_device)
                R.id.action_scenes -> rootNav.navigate(R.id.action_main_to_scenes)
                R.id.action_scan -> rootNav.navigate(R.id.action_main_to_scan)
            }
            true
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.devices.collect {
                        adapter.submit(it)
                        binding.emptyView.isVisible = it.isEmpty()
                    }
                }
                launch {
                    viewModel.loading.collect {
                        binding.progress.isVisible = it && adapter.itemCount == 0
                        binding.refresh.isRefreshing = it && adapter.itemCount > 0
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
