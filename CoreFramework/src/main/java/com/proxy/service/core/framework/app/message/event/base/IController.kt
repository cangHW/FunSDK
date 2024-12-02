package com.proxy.service.core.framework.app.message.event.base

/**
 * @author: cangHX
 * @data: 2024/11/29 15:29
 * @desc:
 */
interface IController {

    /**
     * 添加缓存
     * */
    fun addCache(any: Any)

    /**
     * 检查当前数据是否可用
     * */
    fun use(any: Any): Boolean

    /**
     * 遍历数据
     * */
    fun forEachCache(callback: (any: Any) -> Unit)

}