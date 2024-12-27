package com.proxy.service.core.framework.app.config.controller.store

/**
 * @author: cangHX
 * @data: 2024/12/25 11:38
 * @desc:
 */
interface ISerializable<T> {

    /**
     * 将对象保存成字符串
     * */
    fun saveToString(value: T): String?

    /**
     * 根据字符串生成对象
     * */
    fun loadFromString(value: String): T?

}