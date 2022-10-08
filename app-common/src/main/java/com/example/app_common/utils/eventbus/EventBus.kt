package com.example.app_common.utils.eventbus

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object EventBus {
    private val events = MutableSharedFlow<Any>()

    val subject = events.asSharedFlow()

    suspend fun invokeEvent(event: Any) = events.emit(event)
}
