package com.proxy.service.core.framework.system.security.aes.base.action

/**
 * @author: cangHX
 * @date: 2024/12/2 19:36
 * @desc:
 */
interface IEncryptResult {

    /**
     * 获取加密之后的数据, 失败返回 null
     * */
    fun getString(): String?

    /**
     * 获取加密之后的数据, 失败返回 null
     * */
    fun getBase64String(): String?

    /**
     * 获取加密之后的数据, 失败返回 null
     * */
    fun getByteArray(): ByteArray?

}
