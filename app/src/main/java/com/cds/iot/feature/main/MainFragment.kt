package com.cds.iot.feature.main

import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.cds.iot.R
import com.cds.iot.feature.device.DeviceFragment
import com.cds.iot.feature.message.MessageFragment
import com.cds.iot.feature.user.UserFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.activity_main) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewPager = view.findViewById<ViewPager>(R.id.vp_horizontal_ntb)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radio_group)

        viewPager.offscreenPageLimit = 3
        viewPager.adapter = object : FragmentPagerAdapter(
            childFragmentManager,
            BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
        ) {
            override fun getCount(): Int = 3

            override fun getItem(position: Int): Fragment = when (position) {
                0 -> DeviceFragment()
                1 -> MessageFragment()
                else -> UserFragment()
            }
        }

        viewPager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> radioGroup.check(R.id.device)
                    1 -> radioGroup.check(R.id.message)
                    2 -> radioGroup.check(R.id.me)
                }
            }
        })

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val index = when (checkedId) {
                R.id.device -> 0
                R.id.message -> 1
                R.id.me -> 2
                else -> return@setOnCheckedChangeListener
            }
            if (viewPager.currentItem != index) {
                viewPager.setCurrentItem(index, false)
            }
        }
        radioGroup.check(R.id.device)
    }
}
