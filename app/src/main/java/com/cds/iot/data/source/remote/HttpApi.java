package com.cds.iot.data.source.remote;

import com.cds.iot.data.entity.BaseResp;
import com.cds.iot.data.entity.GankDaily;
import com.cds.iot.data.entity.NewsList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by chengzj on 2017/6/18.
 */

public interface HttpApi {
    //http://gank.io/api/day/2016/10/12
    public static final String base_url = "http://sit.wecarelove.com/api/";

    @GET("day/{year}/{month}/{day}")
    Observable<GankDaily> getDaily(
            @Path("year") int year, @Path("month") int month, @Path("day") int day);

    @GET("list")
    Observable<NewsList> getNewsList(@Query("req_funType") String funType,
                                     @Query("req_count") String count);

    /*---------------------------------------------    用户相关      -----------------------------------------*/

    @POST("user/register")
    Observable<BaseResp> register(@Query("content") String json);

    @POST("user/login")
    Observable<BaseResp> login(@Query("content") String json, @Header("custom_token") String token);

    @POST("user/thridlogin")
    Observable<BaseResp> thridlogin(@Query("content") String json);

    @POST("user/thridbind")
    Observable<BaseResp> thridbind(@Query("content") String json);

    @POST("user/thridunbind")
    Observable<BaseResp> thridunbind(@Query("content") String json);

    @GET("user/info")
    Observable<BaseResp> getInfo(@Query("content") String json);

    @POST("user/info")
    Observable<BaseResp> updateInfo(@Query("content") String json);

    @POST("user/sendcode")
    Observable<BaseResp> sendcode(@Query("content") String json);

    @POST("user/resetpwd")
    Observable<BaseResp> resetpwd(@Query("content") String json);

    @POST("user/updatepwd")
    Observable<BaseResp> updatepwd(@Query("content") String json);

    @POST("user/updatephonenumber")
    Observable<BaseResp> updatephonenumber(@Query("content") String json);

    @POST("user/feedback")
    Observable<BaseResp> feedback(@Query("content") String json);

    /*---------------------------------------------    设备相关      -----------------------------------------*/

    @POST("device/delete")
    Observable<BaseResp> deleteDevice(@Query("content") String json);

    @GET("device/info")
    Observable<BaseResp> getDeviceInfo(@Query("content") String json);

    @POST("device/info")
    Observable<BaseResp> updateDeviceInfo(@Query("content") String json);

    /*---------------------------------------------    场景相关      -----------------------------------------*/

    @POST("scene/delete")
    Observable<BaseResp> deleteScene(@Query("content") String json);

    @GET("scene/info")
    Observable<BaseResp> getSceneInfo(@Query("content") String json);

    @POST("scene/info")
    Observable<BaseResp> updateSceneInfo(@Query("content") String json);

    /*---------------------------------------------    版本相关      -----------------------------------------*/

    @GET("version/update")
    Observable<BaseResp> updateVersion(@Query("content") String json);
}
