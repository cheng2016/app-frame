package com.cds.iot.feature.user

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.cds.iot.R
import com.cds.iot.ui.setupActionBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WxPublicFragment : Fragment(R.layout.activity_public_number) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setupActionBar(getString(R.string.wx_public)) { findNavController().navigateUp() }
    }
}
