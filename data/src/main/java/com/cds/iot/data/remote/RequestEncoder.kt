package com.cds.iot.data.remote

import com.cds.iot.data.dto.BaseReq
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RequestEncoder @Inject constructor(
    private val gson: Gson,
) {
    fun <T> encode(content: T): String = gson.toJson(BaseReq(content))
}
