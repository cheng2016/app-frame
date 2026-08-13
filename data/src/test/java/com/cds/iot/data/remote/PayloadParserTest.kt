package com.cds.iot.data.remote

import com.google.gson.Gson
import com.google.gson.JsonParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PayloadParserTest {
    private val parser = PayloadParser(Gson())

    @Test
    fun parseDevices_fromNamedArray() {
        val json = """
            {"devices":[
              {"id":"1","name":"灯","type":"灯光","online":true,"room":"客厅"},
              {"device_id":"2","device_name":"锁","device_type":"安防","is_online":false}
            ]}
        """.trimIndent()
        val list = parser.parseDevices(JsonParser.parseString(json))
        assertEquals(2, list.size)
        assertEquals("灯", list[0].name)
        assertEquals("客厅", list[0].room)
        assertTrue(list[0].online)
        assertEquals("2", list[1].id)
        assertEquals(false, list[1].online)
    }

    @Test
    fun parseDevices_fromRootArray() {
        val json = """[{"id":"a","name":"空调","status":"online"}]"""
        val list = parser.parseDevices(JsonParser.parseString(json))
        assertEquals(1, list.size)
        assertEquals("空调", list[0].name)
        assertTrue(list[0].online)
    }

    @Test
    fun parseScenes_fromSceneList() {
        val json = """
            {"scene_list":[
              {"scene_id":"s1","scene_name":"回家","device_count":3,"enabled":true},
              {"id":"s2","name":"离家","status":"off"}
            ]}
        """.trimIndent()
        val list = parser.parseScenes(JsonParser.parseString(json))
        assertEquals(2, list.size)
        assertEquals("回家", list[0].name)
        assertEquals(3, list[0].deviceCount)
        assertEquals(false, list[1].enabled)
    }
}
