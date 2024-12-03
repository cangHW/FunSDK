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
     * 添加元数据
     *
     * @return 当前添加的数据进行加解密后的值
     * */
    fun addSourceString(str: String): ByteArray

    /**
     * 添加元数据
     *
     * @return 当前添加的数据进行加解密后的值
     * */
    fun addSourceBase64String(base64: String): ByteArray

    /**
     * 添加元数据
     *
     * @return 当前添加的数据进行加解密后的值
     * */
    fun addSourceByteArray(byteArray: ByteArray): ByteArray

}