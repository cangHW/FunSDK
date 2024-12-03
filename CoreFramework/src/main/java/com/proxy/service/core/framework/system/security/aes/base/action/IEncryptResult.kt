package com.proxy.service.core.framework.system.security.aes.base.action

/**
 * @author: cangHX
 * @data: 2024/12/2 19:36
 * @desc:
 */
interface IEncryptResult {

    /**
     * 获取加密之后的数据
     * */
    fun getString(): String

    /**
     * 获取加密之后的数据
     * */
    fun getBase64String(): String

    /**
     * 获取加密之后的数据
     * */
    fun getByteArray(): ByteArray

}