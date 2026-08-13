package com.cds.iot.data.repository

import com.cds.iot.core.result.AppResult
import com.cds.iot.data.demo.DemoDataSource
import com.cds.iot.data.dto.DeviceReq
import com.cds.iot.data.local.SessionDataStore
import com.cds.iot.data.remote.ApiService
import com.cds.iot.data.remote.PayloadParser
import com.cds.iot.data.remote.RequestEncoder
import com.cds.iot.domain.model.DeviceItem
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepository @Inject constructor(
    private val api: ApiService,
    private val encoder: RequestEncoder,
    private val sessionStore: SessionDataStore,
    private val demo: DemoDataSource,
    private val parser: PayloadParser,
) {
    suspend fun listDevices(): AppResult<List<DeviceItem>> = runCatching {
        if (sessionStore.demoMode.first()) {
            AppResult.Success(demo.devices())
        } else {
            val userId = sessionStore.session.first().userId.toString()
            val resp = api.getDeviceInfo(encoder.encode(DeviceReq(userId)))
            if (!resp.isSuccess) error(resp.message)
            AppResult.Success(parser.parseDevices(resp.data))
        }
    }.getOrElse { AppResult.Error(it.message ?: "加载设备失败") }

    suspend fun addDevice(name: String, type: String): AppResult<DeviceItem> = runCatching {
        if (sessionStore.demoMode.first()) {
            AppResult.Success(demo.addDevice(name, type))
        } else {
            val userId = sessionStore.session.first().userId.toString()
            val resp = api.updateDeviceInfo(
                encoder.encode(DeviceReq(userId, deviceName = name, type = type)),
            )
            if (!resp.isSuccess) error(resp.message)
            val parsed = parser.parseDevices(resp.data).firstOrNull()
            AppResult.Success(
                parsed ?: DeviceItem(
                    id = resp.userId().takeIf { it > 0 }?.toString() ?: "remote-${System.currentTimeMillis()}",
                    name = name,
                    type = type.ifBlank { "通用设备" },
                    online = true,
                ),
            )
        }
    }.getOrElse { AppResult.Error(it.message ?: "添加失败") }

    suspend fun deleteDevice(id: String): AppResult<Unit> = runCatching {
        if (sessionStore.demoMode.first()) {
            demo.deleteDevice(id)
        } else {
            val userId = sessionStore.session.first().userId.toString()
            val resp = api.deleteDevice(encoder.encode(DeviceReq(userId, deviceId = id)))
            if (!resp.isSuccess) error(resp.message)
        }
        AppResult.Success(Unit)
    }.getOrElse { AppResult.Error(it.message ?: "删除失败") }
}
