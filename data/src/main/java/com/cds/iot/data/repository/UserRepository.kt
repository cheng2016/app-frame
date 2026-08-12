package com.cds.iot.data.repository

import com.cds.iot.core.result.AppResult
import com.cds.iot.data.demo.DemoDataSource
import com.cds.iot.data.dto.FeedBackReq
import com.cds.iot.data.dto.GetUserInfoReq
import com.cds.iot.data.dto.UpdatePhoneReq
import com.cds.iot.data.dto.UpdateUserInfoReq
import com.cds.iot.data.dto.VersionReq
import com.cds.iot.data.local.SessionDataStore
import com.cds.iot.data.remote.ApiService
import com.cds.iot.data.remote.RequestEncoder
import com.cds.iot.domain.model.MessageItem
import com.cds.iot.domain.model.UserProfile
import com.cds.iot.domain.model.VersionInfo
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val api: ApiService,
    private val encoder: RequestEncoder,
    private val sessionStore: SessionDataStore,
    private val demo: DemoDataSource,
) {
    suspend fun profile(): AppResult<UserProfile> = runCatching {
        val session = sessionStore.session.first()
        if (sessionStore.demoMode.first()) {
            AppResult.Success(demo.profile(session.userId))
        } else {
            val resp = api.getInfo(encoder.encode(GetUserInfoReq(session.userId.toString())))
            if (!resp.isSuccess) error(resp.message)
            AppResult.Success(
                UserProfile(
                    userId = session.userId,
                    phone = session.phone,
                    nickname = session.nickname,
                    avatarUrl = session.avatarUrl,
                ),
            )
        }
    }.getOrElse { AppResult.Error(it.message ?: "加载资料失败") }

    suspend fun updateNickname(nickname: String): AppResult<Unit> = runCatching {
        val session = sessionStore.session.first()
        if (!sessionStore.demoMode.first()) {
            val resp = api.updateInfo(
                encoder.encode(UpdateUserInfoReq(session.userId.toString(), nickname = nickname)),
            )
            if (!resp.isSuccess) error(resp.message)
        }
        sessionStore.saveSession(session.copy(nickname = nickname))
        AppResult.Success(Unit)
    }.getOrElse { AppResult.Error(it.message ?: "更新失败") }

    suspend fun updatePhone(phone: String, code: String): AppResult<Unit> = runCatching {
        val session = sessionStore.session.first()
        if (sessionStore.demoMode.first()) {
            demo.updatePhone(phone, code)
        } else {
            val resp = api.updatePhoneNumber(
                encoder.encode(UpdatePhoneReq(session.userId.toString(), phone, code)),
            )
            if (!resp.isSuccess) error(resp.message)
        }
        sessionStore.saveSession(session.copy(phone = phone))
        AppResult.Success(Unit)
    }.getOrElse { AppResult.Error(it.message ?: "换绑失败") }

    suspend fun feedback(content: String, contact: String): AppResult<Unit> = runCatching {
        if (sessionStore.demoMode.first()) {
            demo.feedback(content)
        } else {
            val userId = sessionStore.session.first().userId.toString()
            val resp = api.feedback(encoder.encode(FeedBackReq(userId, content, contact)))
            if (!resp.isSuccess) error(resp.message)
        }
        AppResult.Success(Unit)
    }.getOrElse { AppResult.Error(it.message ?: "提交失败") }

    suspend fun messages(): AppResult<List<MessageItem>> = runCatching {
        AppResult.Success(demo.messages())
    }.getOrElse { AppResult.Error(it.message ?: "加载消息失败") }

    suspend fun checkUpdate(versionCode: Int): AppResult<VersionInfo> = runCatching {
        if (sessionStore.demoMode.first()) {
            AppResult.Success(demo.version())
        } else {
            val resp = api.updateVersion(encoder.encode(VersionReq(versionCode)))
            if (!resp.isSuccess) error(resp.message)
            AppResult.Success(
                VersionInfo(
                    versionName = "remote",
                    versionCode = versionCode,
                    forceUpdate = false,
                    changelog = resp.message,
                    downloadUrl = "",
                ),
            )
        }
    }.getOrElse { AppResult.Error(it.message ?: "检查更新失败") }
}
