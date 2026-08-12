package com.cds.iot.feature.user

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cds.iot.R
import com.cds.iot.databinding.FragmentSimpleFormBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : Fragment(R.layout.fragment_simple_form) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentSimpleFormBinding.bind(view)
        binding.toolbar.title = getString(R.string.product_desc)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.fieldOneLayout.isVisible = false
        binding.fieldTwoLayout.isVisible = false
        binding.submitButton.isVisible = false
        binding.contentText.isVisible = true
        binding.contentText.text =
            "酷达物联产品说明\n\n支持智能设备接入、场景联动、消息通知与账号体系。本页保留业务入口，内容可对接正式产品文档站点。"
    }
}
