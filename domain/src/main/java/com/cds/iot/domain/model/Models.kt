package com.cds.iot.domain.model

data class Session(
    val userId: Int = 0,
    val phone: String = "",
    val nickname: String = "",
    val avatarUrl: String = "",
    val token: String = "",
    val isLoggedIn: Boolean = false,
)

data class DeviceItem(
    val id: String,
    val name: String,
    val type: String,
    val online: Boolean,
    val room: String = "",
)

data class SceneItem(
    val id: String,
    val name: String,
    val deviceCount: Int,
    val enabled: Boolean,
)

data class MessageItem(
    val id: String,
    val title: String,
    val body: String,
    val time: String,
    val read: Boolean,
)

data class UserProfile(
    val userId: Int,
    val phone: String,
    val nickname: String,
    val avatarUrl: String,
    val email: String = "",
)

data class VersionInfo(
    val versionName: String,
    val versionCode: Int,
    val forceUpdate: Boolean,
    val changelog: String,
    val downloadUrl: String,
)
