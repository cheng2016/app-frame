package com.cds.iot.feature.setting

import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cds.iot.R
import com.cds.iot.ui.setupActionBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotifyFragment : Fragment(R.layout.activity_message_notify) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setupActionBar(getString(R.string.message_notify)) { findNavController().navigateUp() }
        listOf(
            R.id.message_switchbtn to "新消息通知",
            R.id.voice_switchbtn to "声音",
            R.id.vibrate_switchbtn to "震动",
        ).forEach { (id, label) ->
            view.findViewById<CheckBox>(id).setOnCheckedChangeListener { _, checked ->
                Toast.makeText(
                    requireContext(),
                    "$label：${if (checked) "已开启" else "已关闭"}（Demo）",
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }
}
