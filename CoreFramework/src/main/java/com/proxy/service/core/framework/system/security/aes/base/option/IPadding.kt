package com.proxy.service.core.framework.system.security.aes.base.option

/**
 * 填充模式
 *
 * @author: cangHX
 * @data: 2024/12/2 19:48
 * @desc:
 */
interface IPadding<T> {

    /**
     * 不使用任何填充。如果输入的数据长度不是块大小的整数倍，那么在没有显式填充的情况下，加密操作将会失败。
     * */
    fun noPadding(): T

    /**
     * 填充值为缺少的字节数。特定于8字节块大小的填充方法。
     * */
    fun pkcs5padding(): T

    /**
     * 填充值为缺少的字节数。通用的填充方法，可以用于任意块大小。
     * */
    fun pkcs7padding(): T

    /**
     * 用零值字节进行填充。但这种方法有一个潜在问题，即如果原始数据本身以一个或多个零结尾，则解密后无法区分填充值和真实数据。
     * */
    fun zeroPadding(): T

    /**
     * 使用随机字节进行填充，但最后一个字节表示填充的数量。
     * */
    fun iso10126padding(): T

}