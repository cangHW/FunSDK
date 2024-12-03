package com.proxy.service.core.framework.system.security.aes.base.option

import javax.crypto.spec.IvParameterSpec

/**
 * @author: cangHX
 * @data: 2024/12/2 18:59
 * @desc:
 */
interface IIvParameterSpec<T> {

    /**
     * 设置向量
     * */
    fun setIvParameterSpec(spec: String): T

    /**
     * 设置向量
     * */
    fun setIvParameterSpec(byteArray: ByteArray): T

    /**
     * 设置向量
     * */
    fun setIvParameterSpec(ivSpec: IvParameterSpec): T

}