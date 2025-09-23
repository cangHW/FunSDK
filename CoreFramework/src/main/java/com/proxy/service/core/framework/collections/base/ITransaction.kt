package com.proxy.service.core.framework.collections.base

/**
 * @author: cangHX
 * @data: 2025/9/18 10:24
 * @desc:
 */
interface ITransaction {

    /**
     * 开启事物, 将一段操作转为原子操作
     * */
    fun runInTransaction(runnable: Runnable)

}