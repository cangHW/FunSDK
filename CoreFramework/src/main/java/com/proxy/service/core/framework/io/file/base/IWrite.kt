package com.proxy.service.core.framework.io.file.base

import com.proxy.service.core.framework.io.file.callback.IoCallback
import java.io.File

/**
 * @author: cangHX
 * @data: 2024/9/25 10:37
 * @desc:
 */
interface IWrite {

    /**
     * 同步写入文件
     * @param append    是否追加写入
     * */
    fun writeSync(file: String, append: Boolean = false): Boolean

    /**
     * 异步写入文件
     * @param append    是否追加写入
     * */
    fun writeAsync(file: String, append: Boolean = false, callback: IoCallback? = null)

    /**
     * 同步写入文件
     * @param append    是否追加写入
     * */
    fun writeSync(file: File, append: Boolean = false): Boolean

    /**
     * 异步写入文件
     * @param append    是否追加写入
     * */
    fun writeAsync(file: File, append: Boolean = false, callback: IoCallback? = null)

}