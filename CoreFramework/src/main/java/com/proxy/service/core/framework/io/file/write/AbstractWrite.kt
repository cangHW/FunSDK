package com.proxy.service.core.framework.io.file.write

import com.proxy.service.core.framework.io.file.base.IWrite
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.io.File

/**
 * @author: cangHX
 * @data: 2024/9/25 15:29
 * @desc:
 */
abstract class AbstractWrite : IWrite {

    override fun writeSync(file: String, append: Boolean) {
        writeSync(File(file), append)
    }

    /**
     * 异步写入文件
     * @param append    是否追加写入
     * */
    override fun writeAsync(file: String, append: Boolean) {
        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                writeSync(file, append)
                return ""
            }
        })?.start()
    }

    /**
     * 异步写入文件
     * @param append    是否追加写入
     * */
    override fun writeAsync(file: File, append: Boolean) {
        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                writeSync(file, append)
                return ""
            }
        })?.start()
    }
}