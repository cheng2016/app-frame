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
class WxPublicFragment : Fragment(R.layout.fragment_simple_form) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentSimpleFormBinding.bind(view)
        binding.toolbar.title = getString(R.string.wx_public)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.fieldOneLayout.isVisible = false
        binding.fieldTwoLayout.isVisible = false
        binding.submitButton.isVisible = false
        binding.contentText.isVisible = true
        binding.contentText.text =
            "关注「酷达物联」微信公众号获取设备与服务资讯。\n\nDemo 页不再内嵌第三方票据 URL，避免硬编码敏感链接。"
    }
}
