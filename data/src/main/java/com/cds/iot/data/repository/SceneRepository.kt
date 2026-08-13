package com.cds.iot.data.repository

import com.cds.iot.core.result.AppResult
import com.cds.iot.data.demo.DemoDataSource
import com.cds.iot.data.dto.SceneReq
import com.cds.iot.data.local.SessionDataStore
import com.cds.iot.data.remote.ApiService
import com.cds.iot.data.remote.PayloadParser
import com.cds.iot.data.remote.RequestEncoder
import com.cds.iot.domain.model.SceneItem
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SceneRepository @Inject constructor(
    private val api: ApiService,
    private val encoder: RequestEncoder,
    private val sessionStore: SessionDataStore,
    private val demo: DemoDataSource,
    private val parser: PayloadParser,
) {
    suspend fun listScenes(): AppResult<List<SceneItem>> = runCatching {
        if (sessionStore.demoMode.first()) {
            AppResult.Success(demo.scenes())
        } else {
            val userId = sessionStore.session.first().userId.toString()
            val resp = api.getSceneInfo(encoder.encode(SceneReq(userId)))
            if (!resp.isSuccess) error(resp.message)
            AppResult.Success(parser.parseScenes(resp.data))
        }
    }.getOrElse { AppResult.Error(it.message ?: "加载场景失败") }

    suspend fun saveScene(name: String): AppResult<SceneItem> = runCatching {
        if (sessionStore.demoMode.first()) {
            AppResult.Success(demo.saveScene(name))
        } else {
            val userId = sessionStore.session.first().userId.toString()
            val resp = api.updateSceneInfo(
                encoder.encode(SceneReq(userId, sceneName = name)),
            )
            if (!resp.isSuccess) error(resp.message)
            val parsed = parser.parseScenes(resp.data).firstOrNull()
            AppResult.Success(
                parsed ?: SceneItem(
                    id = "remote-${System.currentTimeMillis()}",
                    name = name,
                    deviceCount = 0,
                    enabled = true,
                ),
            )
        }
    }.getOrElse { AppResult.Error(it.message ?: "保存失败") }

    suspend fun deleteScene(id: String): AppResult<Unit> = runCatching {
        if (sessionStore.demoMode.first()) {
            demo.deleteScene(id)
        } else {
            val userId = sessionStore.session.first().userId.toString()
            val resp = api.deleteScene(encoder.encode(SceneReq(userId, sceneId = id)))
            if (!resp.isSuccess) error(resp.message)
        }
        AppResult.Success(Unit)
    }.getOrElse { AppResult.Error(it.message ?: "删除失败") }
}
