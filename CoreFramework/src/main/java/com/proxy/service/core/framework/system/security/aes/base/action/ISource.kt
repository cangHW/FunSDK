package com.proxy.service.core.framework.system.security.aes.base.action

/**
 * @author: cangHX
 * @data: 2024/12/2 19:35
 * @desc:
 */
interface ISource<T> {

    /**
     * 重置状态，用于下一次使用
     * */
    fun reset()

    /**
     * 设置元数据
     * */
    fun setSourceString(str: String): T

    /**
     * 设置元数据
     * */
    fun setSourceBase64String(base64: String): T

    /**
     * 设置元数据
     * */
    fun setSourceByteArray(byteArray: ByteArray): T

    /**
     * 开始即时处理, 可用于流处理, 需要开发者处理加解密后的字节数据
     * */
    fun startInstantRun(): IInstantRun

}