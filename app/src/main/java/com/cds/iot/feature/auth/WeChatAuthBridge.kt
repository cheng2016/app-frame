package com.cds.iot.feature.auth

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object WeChatAuthBridge {
    private val _events = MutableSharedFlow<WeChatEvent>(extraBufferCapacity = 1)
    val events: SharedFlow<WeChatEvent> = _events.asSharedFlow()

    fun emitCode(code: String) {
        _events.tryEmit(WeChatEvent.Code(code))
    }

    fun emitError(message: String) {
        _events.tryEmit(WeChatEvent.Error(message))
    }
}

sealed class WeChatEvent {
    data class Code(val code: String) : WeChatEvent()
    data class Error(val message: String) : WeChatEvent()
}
