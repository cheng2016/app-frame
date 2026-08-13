package com.cds.iot.data.remote

import com.cds.iot.data.dto.DeviceDto
import com.cds.iot.data.dto.SceneDto
import com.cds.iot.domain.model.DeviceItem
import com.cds.iot.domain.model.SceneItem
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PayloadParser @Inject constructor(
    private val gson: Gson,
) {
    fun parseDevices(data: JsonElement?): List<DeviceItem> {
        val array = extractArray(data, DEVICE_KEYS) ?: return emptyList()
        val type = object : TypeToken<List<DeviceDto>>() {}.type
        val dtos: List<DeviceDto> = gson.fromJson(array, type) ?: emptyList()
        return dtos.mapNotNull { it.toDomain() }
    }

    fun parseScenes(data: JsonElement?): List<SceneItem> {
        val array = extractArray(data, SCENE_KEYS) ?: return emptyList()
        val type = object : TypeToken<List<SceneDto>>() {}.type
        val dtos: List<SceneDto> = gson.fromJson(array, type) ?: emptyList()
        return dtos.mapNotNull { it.toDomain() }
    }

    private fun extractArray(data: JsonElement?, keys: List<String>): JsonArray? {
        if (data == null || data.isJsonNull) return null
        if (data.isJsonArray) return data.asJsonArray
        if (!data.isJsonObject) return null
        val obj = data.asJsonObject
        for (key in keys) {
            val child = obj.get(key) ?: continue
            when {
                child.isJsonArray -> return child.asJsonArray
                child.isJsonObject -> {
                    // Single item wrapped as object
                    val arr = JsonArray()
                    arr.add(child)
                    return arr
                }
            }
        }
        // Fallback: first array-valued field
        obj.entrySet().forEach { (_, value) ->
            if (value.isJsonArray) return value.asJsonArray
        }
        return null
    }

    private fun DeviceDto.toDomain(): DeviceItem? {
        val id = id ?: deviceId ?: return null
        val displayName = name ?: deviceName ?: return null
        val onlineFlag = when {
            online != null -> online
            isOnline != null -> isOnline
            status != null -> status.equals("online", true) || status == "1"
            else -> true
        }
        return DeviceItem(
            id = id,
            name = displayName,
            type = type ?: deviceType ?: "设备",
            online = onlineFlag,
            room = room ?: roomName.orEmpty(),
        )
    }

    private fun SceneDto.toDomain(): SceneItem? {
        val id = id ?: sceneId ?: return null
        val displayName = name ?: sceneName ?: return null
        val enabledFlag = when {
            enabled != null -> enabled
            isEnabled != null -> isEnabled
            status != null -> !status.equals("off", true) && status != "0"
            else -> true
        }
        return SceneItem(
            id = id,
            name = displayName,
            deviceCount = deviceCount ?: 0,
            enabled = enabledFlag,
        )
    }

    companion object {
        private val DEVICE_KEYS = listOf(
            "devices", "device_list", "list", "deviceList", "items", "rows",
        )
        private val SCENE_KEYS = listOf(
            "scenes", "scene_list", "list", "sceneList", "items", "rows",
        )
    }
}

/** Visible for unit tests without DI. */
fun JsonObject.firstArrayOrNull(vararg keys: String): JsonArray? {
    keys.forEach { key ->
        val child = get(key)
        if (child != null && child.isJsonArray) return child.asJsonArray
    }
    return null
}
