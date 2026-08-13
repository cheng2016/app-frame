package com.cds.iot.data.repository

import com.cds.iot.core.result.AppResult
import com.cds.iot.core.util.Md5
import com.cds.iot.data.demo.DemoDataSource
import com.cds.iot.data.dto.LoginReq
import com.cds.iot.data.dto.RegisterReq
import com.cds.iot.data.dto.ResetPwdReq
import com.cds.iot.data.dto.SendCodeReq
import com.cds.iot.data.dto.ThridLoginReq
import com.cds.iot.data.local.SessionDataStore
import com.cds.iot.data.remote.ApiService
import com.cds.iot.data.remote.RequestEncoder
import com.cds.iot.domain.model.Session
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: ApiService,
    private val encoder: RequestEncoder,
    private val sessionStore: SessionDataStore,
    private val demo: DemoDataSource,
) {
    val session: Flow<Session> = sessionStore.session
    val demoMode: Flow<Boolean> = sessionStore.demoMode

    suspend fun setDemoMode(enabled: Boolean) = sessionStore.setDemoMode(enabled)

    suspend fun login(phone: String, password: String): AppResult<Session> = runCatching {
        val session = if (sessionStore.demoMode.first()) {
            demo.login(phone, password)
        } else {
            val pwd = Md5.doubleHash(password)
            val json = encoder.encode(LoginReq(phone, pwd))
            val token = Md5.hash(phone + password + "device")
            val resp = api.login(json, token)
            if (!resp.isSuccess) error(resp.message)
            Session(
                userId = resp.userId(),
                phone = phone,
                nickname = phone,
                token = token,
                isLoggedIn = true,
            )
        }
        sessionStore.saveSession(session)
        AppResult.Success(session)
    }.getOrElse { AppResult.Error(it.message ?: "登录失败") }

    suspend fun register(phone: String, password: String, code: String): AppResult<Session> =
        runCatching {
            val session = if (sessionStore.demoMode.first()) {
                demo.register(phone, password, code)
            } else {
                val json = encoder.encode(
                    RegisterReq(phone, Md5.doubleHash(password), code),
                )
                val resp = api.register(json)
                if (!resp.isSuccess) error(resp.message)
                Session(
                    userId = resp.userId(),
                    phone = phone,
                    nickname = phone,
                    isLoggedIn = true,
                )
            }
            sessionStore.saveSession(session)
            AppResult.Success(session)
        }.getOrElse { AppResult.Error(it.message ?: "注册失败") }

    suspend fun resetPassword(phone: String, password: String, code: String): AppResult<Unit> =
        runCatching {
            if (sessionStore.demoMode.first()) {
                demo.resetPassword(phone, password, code)
            } else {
                val json = encoder.encode(
                    ResetPwdReq(phone, Md5.doubleHash(password), code),
                )
                val resp = api.resetPwd(json)
                if (!resp.isSuccess) error(resp.message)
            }
            AppResult.Success(Unit)
        }.getOrElse { AppResult.Error(it.message ?: "重置失败") }

    suspend fun sendCode(phone: String, type: String = "register"): AppResult<String> =
        runCatching {
            if (sessionStore.demoMode.first()) {
                AppResult.Success(demo.sendCode(phone))
            } else {
                val json = encoder.encode(SendCodeReq(phone, type))
                val resp = api.sendCode(json)
                if (!resp.isSuccess) error(resp.message)
                AppResult.Success("ok")
            }
        }.getOrElse { AppResult.Error(it.message ?: "发送失败") }

    suspend fun thirdLogin(
        openId: String,
        unionId: String = "",
        nickname: String = "",
        avatar: String = "",
    ): AppResult<Session> = runCatching {
        val session = if (sessionStore.demoMode.first()) {
            demo.thirdLogin(openId, nickname)
        } else {
            val json = encoder.encode(ThridLoginReq(openId, unionId, nickname, avatar))
            val resp = api.thirdLogin(json)
            if (!resp.isSuccess) error(resp.message)
            Session(
                userId = resp.userId(),
                nickname = nickname.ifBlank { "微信用户" },
                avatarUrl = avatar,
                token = openId,
                isLoggedIn = true,
            )
        }
        sessionStore.saveSession(session)
        AppResult.Success(session)
    }.getOrElse { AppResult.Error(it.message ?: "微信登录失败") }

    suspend fun logout() {
        sessionStore.clear()
    }
}
