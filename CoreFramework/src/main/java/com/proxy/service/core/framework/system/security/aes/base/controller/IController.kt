package com.proxy.service.core.framework.system.security.aes.base.controller

/**
 * @author: cangHX
 * @data: 2024/12/3 10:51
 * @desc:
 */
interface IController {

    /**
     * 重置状态
     * */
    fun reset()

    /**
     * 更新数据
     * */
    fun update(byteArray: ByteArray): ByteArray

    /**
     * 结束使用
     * */
    fun finish(): ByteArray

}