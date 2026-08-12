package com.cds.iot.ui

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.cds.iot.R

fun Fragment.findRootNavController(): NavController =
    Navigation.findNavController(requireActivity(), R.id.nav_host)
