package com.cds.iot.feature.user

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.cds.iot.R
import com.cds.iot.core.result.AppResult
import com.cds.iot.data.repository.AuthRepository
import com.cds.iot.data.repository.UserRepository
import com.cds.iot.domain.model.UserProfile
import com.cds.iot.ui.findRootNavController
import com.cds.iot.ui.setupActionBar
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile = _profile.asStateFlow()

    init {
        viewModelScope.launch {
            val session = authRepository.session.first()
            when (val result = userRepository.profile()) {
                is AppResult.Success -> _profile.value = result.data
                else -> _profile.value = UserProfile(
                    userId = session.userId,
                    phone = session.phone,
                    nickname = session.nickname.ifBlank { "酷达用户" },
                    avatarUrl = session.avatarUrl,
                )
            }
        }
    }
}

@AndroidEntryPoint
class UserFragment : Fragment(R.layout.fragment_user) {
    private val viewModel: UserViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.setupActionBar(getString(R.string.my), showBack = false)
        val nav = { id: Int -> findRootNavController().navigate(id) }
        view.findViewById<View>(R.id.user_detail_layout).setOnClickListener {
            nav(R.id.action_main_to_user_detail)
        }
        view.findViewById<View>(R.id.product_description_layout).setOnClickListener {
            nav(R.id.action_main_to_product)
        }
        view.findViewById<View>(R.id.wx_public_number_layout).setOnClickListener {
            nav(R.id.action_main_to_wx_public)
        }
        view.findViewById<View>(R.id.feedback_layout).setOnClickListener {
            nav(R.id.action_main_to_feedback)
        }
        view.findViewById<View>(R.id.about_us_layout).setOnClickListener {
            nav(R.id.action_main_to_about)
        }
        view.findViewById<View>(R.id.setting_layout).setOnClickListener {
            nav(R.id.action_main_to_setting)
        }

        val nickname = view.findViewById<TextView>(R.id.nickname)
        val phone = view.findViewById<TextView>(R.id.phone)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.profile.collect { profile ->
                    nickname.text = profile?.nickname ?: "酷达用户"
                    phone.text = profile?.phone?.ifBlank { "未绑定手机号" }
                }
            }
        }
    }
}
