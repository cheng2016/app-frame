package com.cds.iot.data.demo

import com.cds.iot.domain.model.DeviceItem
import com.cds.iot.domain.model.MessageItem
import com.cds.iot.domain.model.SceneItem
import com.cds.iot.domain.model.Session
import com.cds.iot.domain.model.UserProfile
import com.cds.iot.domain.model.VersionInfo
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoDataSource @Inject constructor() {

    private val devices = mutableListOf(
        DeviceItem("d1", "客厅空调", "空调", true, "客厅"),
        DeviceItem("d2", "主卧灯", "灯光", true, "主卧"),
        DeviceItem("d3", "智能门锁", "安防", false, "玄关"),
        DeviceItem("d4", "空气净化器", "环境", true, "客厅"),
    )

    private val scenes = mutableListOf(
        SceneItem("s1", "回家模式", 3, true),
        SceneItem("s2", "离家模式", 4, true),
        SceneItem("s3", "睡眠模式", 2, false),
    )

    private val messages = mutableListOf(
        MessageItem("m1", "设备上线", "客厅空调已上线", "今天 09:20", false),
        MessageItem("m2", "场景执行", "回家模式已执行", "昨天 18:02", true),
        MessageItem("m3", "系统通知", "欢迎使用酷达物联 Demo", "周一 10:00", true),
    )

    suspend fun login(phone: String, password: String): Session {
        delay(800)
        require(phone.isNotBlank() && password.length >= 4) { "账号或密码格式不正确" }
        return Session(
            userId = 10001,
            phone = phone,
            nickname = "酷达用户",
            avatarUrl = "",
            token = "demo-token",
            isLoggedIn = true,
        )
    }

    suspend fun register(phone: String, password: String, code: String): Session {
        delay(800)
        require(code.length >= 4) { "验证码无效" }
        return login(phone, password)
    }

    suspend fun resetPassword(phone: String, password: String, code: String) {
        delay(600)
        require(code.length >= 4) { "验证码无效" }
        require(password.length >= 6) { "密码至少 6 位" }
    }

    suspend fun sendCode(phone: String): String {
        delay(400)
        require(phone.length >= 6) { "手机号无效" }
        return "1234"
    }

    suspend fun thirdLogin(openId: String, nickname: String): Session {
        delay(600)
        return Session(
            userId = 10002,
            phone = "",
            nickname = nickname.ifBlank { "微信用户" },
            avatarUrl = "",
            token = "demo-wx-$openId",
            isLoggedIn = true,
        )
    }

    suspend fun profile(userId: Int): UserProfile {
        delay(300)
        return UserProfile(
            userId = userId,
            phone = "138****0000",
            nickname = "酷达用户",
            avatarUrl = "",
            email = "demo@wecarelove.com",
        )
    }

    suspend fun devices(): List<DeviceItem> {
        delay(400)
        return devices.toList()
    }

    suspend fun addDevice(name: String, type: String): DeviceItem {
        delay(500)
        val item = DeviceItem("d${devices.size + 1}", name, type, true, "未分组")
        devices.add(0, item)
        return item
    }

    suspend fun deleteDevice(id: String) {
        delay(300)
        devices.removeAll { it.id == id }
    }

    suspend fun scenes(): List<SceneItem> {
        delay(350)
        return scenes.toList()
    }

    suspend fun saveScene(name: String): SceneItem {
        delay(400)
        val item = SceneItem("s${scenes.size + 1}", name, 0, true)
        scenes.add(0, item)
        return item
    }

    suspend fun deleteScene(id: String) {
        delay(300)
        scenes.removeAll { it.id == id }
    }

    suspend fun messages(): List<MessageItem> {
        delay(300)
        return messages.toList()
    }

    suspend fun feedback(content: String) {
        delay(500)
        require(content.isNotBlank()) { "请输入反馈内容" }
    }

    suspend fun updatePhone(phone: String, code: String) {
        delay(500)
        require(code.length >= 4) { "验证码无效" }
        require(phone.length >= 6) { "手机号无效" }
    }

    suspend fun version(): VersionInfo {
        delay(300)
        return VersionInfo(
            versionName = "2.0.0",
            versionCode = 2,
            forceUpdate = false,
            changelog = "架构现代化：Kotlin + MVVM + Material 3",
            downloadUrl = "https://www.wecarelove.com",
        )
    }
}
