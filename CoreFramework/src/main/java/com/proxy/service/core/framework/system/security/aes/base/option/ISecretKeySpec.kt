package com.proxy.service.core.framework.system.security.aes.base.option

import javax.crypto.SecretKey

/**
 * @author: cangHX
 * @data: 2024/12/2 19:34
 * @desc:
 */
interface ISecretKeySpec<T> {

    /**
     * 设置密钥
     * */
    fun setSecretKeySpec(key: String): T

    /**
     * 设置密钥
     * */
    fun setSecretKeySpec(byteArray: ByteArray): T

    /**
     * 设置密钥
     * */
    fun setSecretKeySpec(secretKey: SecretKey): T

}