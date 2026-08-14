package com.cds.iot.feature.device

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cds.iot.R
import com.cds.iot.domain.model.DeviceItem
import com.cds.iot.ui.findRootNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DeviceFragment : Fragment(R.layout.fragment_device) {
    private val viewModel: DeviceViewModel by viewModels()
    private val devices = mutableListOf<DeviceItem>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val grid = view.findViewById<GridView>(R.id.device_gridview)
        val emptyView = view.findViewById<TextView>(R.id.empty_view)
        val adapter = object : BaseAdapter() {
            override fun getCount(): Int = devices.size
            override fun getItem(position: Int): DeviceItem = devices[position]
            override fun getItemId(position: Int): Long = position.toLong()
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val row = convertView ?: LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_device, parent, false)
                val item = getItem(position)
                row.findViewById<TextView>(R.id.device_name).text = item.name
                row.findViewById<TextView>(R.id.device_status).text =
                    if (item.online) "在线" else "离线"
                row.setOnLongClickListener {
                    viewModel.delete(item.id)
                    true
                }
                return row
            }
        }
        grid.adapter = adapter

        view.findViewById<ImageView>(R.id.add_img).setOnClickListener {
            findRootNavController().navigate(R.id.action_main_to_add_device)
        }
        view.findViewById<View>(R.id.often_layout).setOnClickListener {
            findRootNavController().navigate(R.id.action_main_to_scenes)
        }
        view.findViewById<View>(R.id.often_layout).setOnLongClickListener {
            findRootNavController().navigate(R.id.action_main_to_scan)
            true
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.devices.collect {
                        devices.clear()
                        devices.addAll(it)
                        adapter.notifyDataSetChanged()
                        emptyView.isVisible = it.isEmpty()
                        grid.isVisible = it.isNotEmpty()
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
