package com.proxy.service.core.framework.system.security.aes.base.action

/**
 * @author: cangHX
 * @date: 2024/12/2 19:36
 * @desc:
 */
interface IDecryptResult {

    /**
     * 获取解密之后的数据, 失败返回 null
     * */
    fun getString(): String?

    /**
     * 获取解密之后的数据, 失败返回 null
     * */
    fun getByteArray(): ByteArray?

}
