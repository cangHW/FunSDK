package com.proxy.service.core.framework.system.security.aes.base.action

/**
 * @author: cangHX
 * @data: 2024/12/2 19:36
 * @desc:
 */
interface IDecryptResult {

    /**
     * 获取解密之后的数据
     * */
    fun getString(): String

    /**
     * 获取解密之后的数据
     * */
    fun getByteArray(): ByteArray

}