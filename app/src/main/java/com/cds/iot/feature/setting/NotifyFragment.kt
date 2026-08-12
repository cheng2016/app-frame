package com.cds.iot.feature.setting

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cds.iot.R
import com.cds.iot.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotifyFragment : Fragment(R.layout.fragment_setting) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentSettingBinding.bind(view)
        binding.toolbar.title = getString(R.string.message_notify)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.demoSwitch.isVisible = false
        binding.itemNotify.text = "设备告警通知"
        binding.itemUpdate.text = "场景执行通知"
        binding.itemClearCache.text = "系统公告"
        binding.itemLogout.isVisible = false
        listOf(binding.itemNotify, binding.itemUpdate, binding.itemClearCache).forEach { row ->
            row.setOnClickListener {
                Toast.makeText(requireContext(), "${row.text}：已开启（Demo）", Toast.LENGTH_SHORT).show()
            }
        }
        // hide demo row container's switch row by collapsing parent — switch already hidden
        (binding.demoSwitch.parent as? View)?.isVisible = false
    }
}
