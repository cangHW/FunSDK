package com.proxy.service.core.framework.app.config.base

/**
 * @author: cangHX
 * @data: 2024/12/25 10:22
 * @desc:
 */
interface IAction<T> {

    /**
     * 改动是否持久化保存, 如果持久化保存则应用重启也会自动生效, 默认为 true
     * */
    fun save(isSave: Boolean): T

}