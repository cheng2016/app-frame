package com.cds.iot.feature.device

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cds.iot.R
import com.cds.iot.databinding.FragmentSimpleFormBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanFragment : Fragment(R.layout.fragment_simple_form) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentSimpleFormBinding.bind(view)
        binding.toolbar.title = getString(R.string.scan_qr)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.fieldOneLayout.hint = "扫码结果 / 设备序列号"
        binding.fieldTwoLayout.isVisible = false
        binding.contentText.isVisible = true
        binding.contentText.text = "Demo：输入序列号即可模拟扫码添加（真实扫码可接入 ML Kit / ZXing）。"
        binding.submitButton.text = "确认添加"
        binding.submitButton.setOnClickListener {
            val code = binding.fieldOne.text?.toString().orEmpty()
            if (code.isBlank()) {
                Toast.makeText(requireContext(), "请输入序列号", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "已识别：$code", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }
    }
}
