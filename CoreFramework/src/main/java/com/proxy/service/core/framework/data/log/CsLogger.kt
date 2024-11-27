package com.proxy.service.core.framework.data.log

import com.proxy.service.api.log.base.IL
import com.proxy.service.api.log.base.LogTree
import com.proxy.service.api.log.tree.DebugTree
import com.proxy.service.api.log.tree.TreeGroup


/**
 * 日志工具
 *
 * @author: cangHX
 * @data: 2024/4/28 17:26
 * @desc:
 */
object CsLogger : IL {

    private val tree = TreeGroup()

    /**
     * 设置日志回调
     * */
    fun addLogCallback(tree: LogTree) {
        TreeGroup.plant(tree)
    }

    /**
     * 设置是否允许打印到控制台，默认允许
     * */
    fun setPrintEnable(enable: Boolean) {
        DebugTree.setPrintEnable(enable)
    }

    fun tag(tag: String): IL {
        tree.setTag(tag)
        return this
    }

    override fun v(throwable: Throwable) {
        tree.v(throwable)
    }

    override fun v(message: String, vararg args: Any) {
        tree.v(message, args)
    }

    override fun v(throwable: Throwable, message: String, vararg args: Any) {
        tree.v(throwable, message, args)
    }

    override fun d(throwable: Throwable) {
        tree.d(throwable)
    }

    override fun d(message: String, vararg args: Any) {
        tree.d(message, args)
    }

    override fun d(throwable: Throwable, message: String, vararg args: Any) {
        tree.d(throwable, message, args)
    }

    override fun i(throwable: Throwable) {
        tree.i(throwable)
    }

    override fun i(message: String, vararg args: Any) {
        tree.i(message, args)
    }

    override fun i(throwable: Throwable, message: String, vararg args: Any) {
        tree.i(throwable, message, args)
    }

    override fun w(throwable: Throwable) {
        tree.w(throwable)
    }

    override fun w(message: String, vararg args: Any) {
        tree.w(message, args)
    }

    override fun w(throwable: Throwable, message: String, vararg args: Any) {
        tree.w(throwable, message, args)
    }

    override fun e(throwable: Throwable) {
        tree.e(throwable)
    }

    override fun e(message: String, vararg args: Any) {
        tree.e(message, args)
    }

    override fun e(throwable: Throwable, message: String, vararg args: Any) {
        tree.e(throwable, message, args)
    }

    override fun wtf(message: String, vararg args: Any) {
        tree.wtf(message, args)
    }

    override fun wtf(throwable: Throwable, message: String, vararg args: Any) {
        tree.wtf(throwable, message, args)
    }

    override fun wtf(throwable: Throwable) {
        tree.wtf(throwable)
    }

    override fun log(priority: Int, message: String, vararg args: Any) {
        tree.log(priority, message, args)
    }

    override fun log(priority: Int, throwable: Throwable, message: String, vararg args: Any) {
        tree.log(priority, throwable, message, args)
    }

    override fun log(priority: Int, throwable: Throwable) {
        tree.log(priority, throwable)
    }
}