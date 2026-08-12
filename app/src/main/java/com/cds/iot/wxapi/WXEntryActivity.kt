package com.cds.iot.wxapi

import android.app.Activity
import android.os.Bundle
import com.cds.iot.BuildConfig
import com.cds.iot.core.util.Logger
import com.cds.iot.feature.auth.WeChatAuthBridge
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory

/**
 * WeChat callback entry. Only handles auth code; AppSecret must stay on the server.
 */
class WXEntryActivity : Activity(), IWXAPIEventHandler {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val api = WXAPIFactory.createWXAPI(this, BuildConfig.WX_APP_ID, false)
        api.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: android.content.Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        val api = WXAPIFactory.createWXAPI(this, BuildConfig.WX_APP_ID, false)
        api.handleIntent(intent, this)
    }

    override fun onReq(req: BaseReq?) {
        finish()
    }

    override fun onResp(resp: BaseResp?) {
        when (resp?.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                val code = (resp as? SendAuth.Resp)?.code.orEmpty()
                Logger.d("WXEntry", "auth code received")
                WeChatAuthBridge.emitCode(code)
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> WeChatAuthBridge.emitError("用户取消微信授权")
            else -> WeChatAuthBridge.emitError("微信授权失败: ${resp?.errCode}")
        }
        finish()
    }
}
