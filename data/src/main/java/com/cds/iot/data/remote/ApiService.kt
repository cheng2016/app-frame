package com.cds.iot.data.remote

import com.cds.iot.data.dto.BaseResp
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Aligns with the legacy HttpApi contract: JSON payload in `content` query + optional custom_token.
 */
interface ApiService {

    @POST("user/register")
    suspend fun register(@Query("content") json: String): BaseResp

    @POST("user/login")
    suspend fun login(
        @Query("content") json: String,
        @Header("custom_token") token: String,
    ): BaseResp

    @POST("user/thridlogin")
    suspend fun thirdLogin(@Query("content") json: String): BaseResp

    @POST("user/thridbind")
    suspend fun thirdBind(@Query("content") json: String): BaseResp

    @POST("user/thridunbind")
    suspend fun thirdUnbind(@Query("content") json: String): BaseResp

    @GET("user/info")
    suspend fun getInfo(@Query("content") json: String): BaseResp

    @POST("user/info")
    suspend fun updateInfo(@Query("content") json: String): BaseResp

    @POST("user/sendcode")
    suspend fun sendCode(@Query("content") json: String): BaseResp

    @POST("user/resetpwd")
    suspend fun resetPwd(@Query("content") json: String): BaseResp

    @POST("user/updatepwd")
    suspend fun updatePwd(@Query("content") json: String): BaseResp

    @POST("user/updatephonenumber")
    suspend fun updatePhoneNumber(@Query("content") json: String): BaseResp

    @POST("user/feedback")
    suspend fun feedback(@Query("content") json: String): BaseResp

    @POST("device/delete")
    suspend fun deleteDevice(@Query("content") json: String): BaseResp

    @GET("device/info")
    suspend fun getDeviceInfo(@Query("content") json: String): BaseResp

    @POST("device/info")
    suspend fun updateDeviceInfo(@Query("content") json: String): BaseResp

    @POST("scene/delete")
    suspend fun deleteScene(@Query("content") json: String): BaseResp

    @GET("scene/info")
    suspend fun getSceneInfo(@Query("content") json: String): BaseResp

    @POST("scene/info")
    suspend fun updateSceneInfo(@Query("content") json: String): BaseResp

    @GET("version/update")
    suspend fun updateVersion(@Query("content") json: String): BaseResp
}
