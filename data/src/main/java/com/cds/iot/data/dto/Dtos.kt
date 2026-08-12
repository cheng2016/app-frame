package com.cds.iot.data.dto

import com.google.gson.annotations.SerializedName

data class BaseReq<T>(val content: T)

data class BaseInfo(
    val code: String? = null,
    val info: String? = null,
)

data class BaseResp(
    val data: DataBean? = null,
    val info: BaseInfo? = null,
) {
    data class DataBean(
        @SerializedName("user_id") val userId: Int = 0,
    )

    val isSuccess: Boolean get() = info?.code == "200"
    val message: String get() = info?.info.orEmpty().ifBlank { "未知错误" }
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
