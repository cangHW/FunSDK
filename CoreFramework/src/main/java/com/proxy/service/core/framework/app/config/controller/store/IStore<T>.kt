package com.proxy.service.core.framework.app.config.controller.store

/**
 * @author: cangHX
 * @data: 2024/12/25 10:54
 * @desc:
 */
interface IStore<T> {

    /**
     * 判断是否跟随系统
     * */
    fun isFollowingSystem(): Boolean?

    /**
     * 设置是否跟随系统
     *
     * @param isSave 是否持久化存储
     * */
    fun setFollowSystem(value: Boolean, isSave: Boolean = true)

    /**
     * 获取存储数据
     * */
    fun getValue(): T?

    /**
     * 存储数据
     *
     * @param isSave 是否持久化存储
     * */
    fun setSave(value: T, isSave: Boolean = true)

}