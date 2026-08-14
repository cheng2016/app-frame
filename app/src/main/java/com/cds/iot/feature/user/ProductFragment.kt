package com.cds.iot.feature.user

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cds.iot.R
import com.cds.iot.ui.setupActionBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : Fragment(R.layout.activity_product_description) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setupActionBar(getString(R.string.product_desc)) { findNavController().navigateUp() }
        view.findViewById<View>(R.id.content_layout).let { container ->
            val text = TextView(requireContext()).apply {
                setPadding(48, 48, 48, 48)
                text = "酷达物联智能家居产品说明\n\n支持设备绑定、场景联动、消息提醒与微信生态能力。"
                setTextColor(resources.getColor(R.color.text_enable_color, null))
                textSize = 15f
            }
            (container as android.widget.FrameLayout).addView(text)
        }
    }
}
