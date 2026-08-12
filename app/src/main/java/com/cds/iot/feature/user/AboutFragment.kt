package com.cds.iot.feature.user

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cds.iot.BuildConfig
import com.cds.iot.R
import com.cds.iot.databinding.FragmentSimpleFormBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment : Fragment(R.layout.fragment_simple_form) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentSimpleFormBinding.bind(view)
        binding.toolbar.title = getString(R.string.about)
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.fieldOneLayout.isVisible = false
        binding.fieldTwoLayout.isVisible = false
        binding.submitButton.isVisible = false
        binding.contentText.isVisible = true
        binding.contentText.text = buildString {
            appendLine(getString(R.string.app_name))
            appendLine("版本 ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
            appendLine()
            appendLine(getString(R.string.about_name))
            appendLine(getString(R.string.about_website))
            appendLine(getString(R.string.about_phone))
            appendLine()
            append("Kotlin · MVVM · Material 3 现代化 Demo")
        }
    }
}
