package com.cds.iot.feature.user

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cds.iot.BuildConfig
import com.cds.iot.R
import com.cds.iot.ui.setupActionBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AboutFragment : Fragment(R.layout.activity_about_us) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setupActionBar(getString(R.string.about)) { findNavController().navigateUp() }
        view.findViewById<TextView>(R.id.version_name_tv).text =
            "v${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
    }
}
