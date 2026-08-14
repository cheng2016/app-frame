package com.cds.iot.feature.auth

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.cds.iot.BuildConfig
import com.cds.iot.R
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.activity_login) {

    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val account = view.findViewById<AppCompatEditText>(R.id.acount)
        val password = view.findViewById<AppCompatEditText>(R.id.password)

        view.findViewById<AppCompatButton>(R.id.email_sign_in_button).setOnClickListener {
            viewModel.login(
                account.text?.toString().orEmpty(),
                password.text?.toString().orEmpty(),
            )
        }
        view.findViewById<View>(R.id.register_btn).setOnClickListener {
            findNavController().navigate(R.id.action_login_to_register)
        }
        view.findViewById<View>(R.id.modify_password_button).setOnClickListener {
            findNavController().navigate(R.id.action_login_to_forget)
        }
        view.findViewById<View>(R.id.weixin).setOnClickListener { startWeChatLogin() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
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
                        // Old login UI has no dedicated demo chip; toast once via message if needed.
                        view.findViewById<View>(R.id.bottom_title_rly).isVisible = true
                        if (demo) {
                            view.findViewById<View>(R.id.bottom_title_rly).alpha = 0.9f
                        }
                    }
                }
            }
        }
    }

    private fun startWeChatLogin() {
        val appId = BuildConfig.WX_APP_ID
        if (appId.isBlank()) {
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
