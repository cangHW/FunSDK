package com.proxy.service.core.framework.system.security.aes.base.action

/**
 * 流处理
 *
 * @author: cangHX
 * @date: 2024/12/4 10:01
 * @desc:
 */
interface IInstantRun {

    /**
     * 添加元数据
     *
     * @return 当前添加的数据进行加解密后的值, 失败返回 null
     * */
    fun updateSourceString(str: String): ByteArray?

    /**
     * 添加元数据
     *
     * @return 当前添加的数据进行加解密后的值, 失败返回 null
     * */
    fun updateSourceBase64String(base64: String): ByteArray?

    /**
     * 添加元数据
     *
     * @return 当前添加的数据进行加解密后的值, 失败返回 null
     * */
    fun updateSourceByteArray(byteArray: ByteArray): ByteArray?


    /**
     * 结束即时处理, 失败返回 null
     * */
    fun endInstantRun(): ByteArray?
}
