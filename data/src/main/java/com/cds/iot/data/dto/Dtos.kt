package com.cds.iot.data.dto

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class BaseReq<T>(val content: T)

data class BaseInfo(
    val code: String? = null,
    val info: String? = null,
)

/**
 * Flexible envelope: `data` may be an object, array, or null depending on endpoint.
 */
data class BaseResp(
    val data: JsonElement? = null,
    val info: BaseInfo? = null,
) {
    val isSuccess: Boolean get() = info?.code == "200"
    val message: String get() = info?.info.orEmpty().ifBlank { "未知错误" }

    fun userId(): Int {
        val obj = data?.takeIf { it.isJsonObject }?.asJsonObject ?: return 0
        return sequenceOf("user_id", "userId", "id")
            .mapNotNull { key -> obj.get(key)?.takeIf { !it.isJsonNull } }
            .firstOrNull()
            ?.asInt
            ?: 0
    }
}

data class LoginReq(
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("login_pwd") val loginPwd: String,
)

data class RegisterReq(
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("login_pwd") val loginPwd: String,
    @SerializedName("code") val code: String,
)

data class ResetPwdReq(
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("login_pwd") val loginPwd: String,
    @SerializedName("code") val code: String,
)

data class SendCodeReq(
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("type") val type: String = "register",
)

data class GetUserInfoReq(
    @SerializedName("user_id") val userId: String,
)

data class UpdateUserInfoReq(
    @SerializedName("user_id") val userId: String,
    @SerializedName("nickname") val nickname: String? = null,
    @SerializedName("avatar") val avatar: String? = null,
)

data class UpdatePhoneReq(
    @SerializedName("user_id") val userId: String,
    @SerializedName("phone_number") val phoneNumber: String,
    @SerializedName("code") val code: String,
)

data class FeedBackReq(
    @SerializedName("user_id") val userId: String,
    @SerializedName("content") val content: String,
    @SerializedName("contact") val contact: String = "",
)

data class ThridLoginReq(
    @SerializedName("openid") val openId: String,
    @SerializedName("unionid") val unionId: String = "",
    @SerializedName("nickname") val nickname: String = "",
    @SerializedName("headimgurl") val headImgUrl: String = "",
)

data class ThridBindReq(
    @SerializedName("user_id") val userId: String,
    @SerializedName("openid") val openId: String,
    @SerializedName("unionid") val unionId: String = "",
)

data class DeviceReq(
    @SerializedName("user_id") val userId: String,
    @SerializedName("device_id") val deviceId: String = "",
    @SerializedName("device_name") val deviceName: String = "",
    @SerializedName("type") val type: String = "",
)

data class SceneReq(
    @SerializedName("user_id") val userId: String,
    @SerializedName("scene_id") val sceneId: String = "",
    @SerializedName("scene_name") val sceneName: String = "",
)

data class VersionReq(
    @SerializedName("version_code") val versionCode: Int,
    @SerializedName("platform") val platform: String = "android",
)

/** Aligns with legacy ScenesDevice + common API aliases. */
data class DeviceDto(
    val id: String? = null,
    @SerializedName("device_id") val deviceId: String? = null,
    val name: String? = null,
    @SerializedName("device_name") val deviceName: String? = null,
    val type: String? = null,
    @SerializedName("device_type") val deviceType: String? = null,
    val online: Boolean? = null,
    @SerializedName("is_online") val isOnline: Boolean? = null,
    val status: String? = null,
    val room: String? = null,
    @SerializedName("room_name") val roomName: String? = null,
    val imageurl: String? = null,
    @SerializedName("image_url") val imageUrl: String? = null,
)

/** Aligns with legacy Scenes + common API aliases. */
data class SceneDto(
    val id: String? = null,
    @SerializedName("scene_id") val sceneId: String? = null,
    val name: String? = null,
    @SerializedName("scene_name") val sceneName: String? = null,
    @SerializedName("device_count") val deviceCount: Int? = null,
    val enabled: Boolean? = null,
    @SerializedName("is_enabled") val isEnabled: Boolean? = null,
    val status: String? = null,
    @SerializedName("icon_url") val iconUrl: String? = null,
)
