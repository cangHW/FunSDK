package com.proxy.service.core.framework.io.sp

/**
 * @author: cangHX
 * @data: 2024/7/20 14:59
 * @desc:
 */
interface ISpAction : ISpController {

    /**
     * 指定存储库名称
     * */
    fun name(tag: String): ISpAction

    /**
     * 指定存储库模式, 默认单进程
     * */
    fun mode(mode: SpMode): ISpAction

    /**
     * 指定存储库加密密钥
     * */
    fun secretKey(secretKey: String): ISpAction

    /**
     * 获取存储库对应的本地文件夹路径
     * */
    fun getRootDir(): String

    /**
     * 获取存储管理器, 可以通过管理器进行多次操作
     * */
    fun getController(): ISpController
}