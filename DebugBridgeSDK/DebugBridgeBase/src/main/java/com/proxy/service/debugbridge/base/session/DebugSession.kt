package com.proxy.service.debugbridge.base.session

/**
 * @author: cangHX
 * @date: 2026/6/15
 * @desc: 通用调试会话，各 SDK 插件通过 payload 传递业务数据
 */
data class DebugSession(
    val id: String,
    val pluginId: String,
    val tag: String,
    val payload: String,
    val metadata: Map<String, String> = emptyMap(),
    val createdAt: Long = System.currentTimeMillis()
)
