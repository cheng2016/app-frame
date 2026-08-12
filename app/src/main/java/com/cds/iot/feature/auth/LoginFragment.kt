package com.cds.iot.feature.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.cds.iot.BuildConfig
import com.cds.iot.R
import com.cds.iot.databinding.FragmentLoginBinding
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentLoginBinding.bind(view)

        binding.loginButton.setOnClickListener {
            viewModel.login(
                binding.phoneInput.text?.toString().orEmpty(),
                binding.passwordInput.text?.toString().orEmpty(),
            )
        }
        binding.registerLink.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
        binding.forgetLink.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_forget)
        }
        binding.wechatButton.setOnClickListener { startWeChatLogin() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.loading.collect { binding.progress.isVisible = it }
                }
                launch {
                    viewModel.message.collect {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    }
                }
                launch {
                    viewModel.navigateMain.collect {
                        findNavController().navigate(R.id.action_login_to_main)
                    }
                }
                launch {
                    viewModel.demoMode.collect { demo ->
                        binding.demoHint.isVisible = demo
                    }
                }
            }
        }
    }

    private fun startWeChatLogin() {
        val appId = BuildConfig.WX_APP_ID
        if (appId.isBlank()) {
            // No client secret / app id configured — use DemoMode WeChat path.
            viewModel.loginWithWeChatDemo()
            return
        }
        val api = WXAPIFactory.createWXAPI(requireContext(), appId, true)
        if (!api.isWXAppInstalled) {
            Toast.makeText(requireContext(), "未安装微信，已使用 Demo 登录", Toast.LENGTH_SHORT).show()
            viewModel.loginWithWeChatDemo()
            return
        }
        api.registerApp(appId)
        val req = SendAuth.Req().apply {
            scope = "snsapi_userinfo"
            state = "kuda_iot_login"
        }
        api.sendReq(req)
    }
}
