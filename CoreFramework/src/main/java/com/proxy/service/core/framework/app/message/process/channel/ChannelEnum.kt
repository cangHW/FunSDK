package com.proxy.service.core.framework.app.message.process.channel

import com.proxy.service.core.framework.app.install.CsInstallUtils

/**
 * @author: cangHX
 * @data: 2025/9/18 17:50
 * @desc:
 */
enum class SendChannel {

    /**
     * 自动处理, 基于策略自动找到最适合方案
     * */
    AUTO,

    /**
     * ContentProvider, 需要配置目标应用可见, 配置方式参考 [CsInstallUtils.isInstallApp]
     * */
    PROVIDER,

    /**
     * Broadcast
     * */
    BROADCAST;

}

enum class ReceiveChannel {
    /**
     * 自动处理, 基于策略自动找到最适合方案
     * */
    AUTO,

    /**
     * 不需要返回值
     * */
    NONE,

    /**
     * ContentProvider, 需要目标应用配置当前应用可见, 配置方式参考 [CsInstallUtils.isInstallApp]
     * */
    PROVIDER,

    /**
     * Broadcast
     * */
    BROADCAST;

    companion object {
        fun valueOf(name: String): ReceiveChannel {
            return when (name) {
                AUTO.name -> {
                    AUTO
                }

                PROVIDER.name -> {
                    PROVIDER
                }

                BROADCAST.name -> {
                    BROADCAST
                }

                else -> {
                    NONE
                }
            }
        }
    }
}


