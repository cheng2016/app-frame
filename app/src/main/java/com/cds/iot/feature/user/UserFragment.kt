package com.cds.iot.feature.user

import android.os.Bundle
import android.view.View
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
import com.cds.iot.databinding.FragmentUserBinding
import com.cds.iot.domain.model.UserProfile
import com.cds.iot.ui.findRootNavController
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
        val binding = FragmentUserBinding.bind(view)
        val nav = { id: Int -> findRootNavController().navigate(id) }
        binding.profileHeader.setOnClickListener { nav(R.id.action_main_to_user_detail) }
        binding.itemUserDetail.setOnClickListener { nav(R.id.action_main_to_user_detail) }
        binding.itemChangePhone.setOnClickListener { nav(R.id.action_main_to_change_phone) }
        binding.itemFeedback.setOnClickListener { nav(R.id.action_main_to_feedback) }
        binding.itemWxPublic.setOnClickListener { nav(R.id.action_main_to_wx_public) }
        binding.itemProduct.setOnClickListener { nav(R.id.action_main_to_product) }
        binding.itemAbout.setOnClickListener { nav(R.id.action_main_to_about) }
        binding.itemSetting.setOnClickListener { nav(R.id.action_main_to_setting) }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.profile.collect { profile ->
                    binding.nickname.text = profile?.nickname ?: "酷达用户"
                    binding.phone.text = profile?.phone?.ifBlank { "未绑定手机号" }
                }
            }
        }
    }
}
