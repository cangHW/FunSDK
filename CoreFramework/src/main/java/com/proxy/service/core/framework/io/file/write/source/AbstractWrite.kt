package com.proxy.service.core.framework.io.file.write.source

import com.proxy.service.core.framework.io.file.base.IWrite
import com.proxy.service.core.framework.io.file.callback.IoCallback
import com.proxy.service.core.framework.io.file.write.log.IWriteStatusLog
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.io.File
import java.io.OutputStream

/**
 * @author: cangHX
 * @data: 2024/9/25 15:29
 * @desc:
 */
abstract class AbstractWrite : IWrite, IWriteStatusLog {

    /**
     * 同步写入文件
     * @param append    是否追加写入
     * */
    override fun writeSync(file: String, append: Boolean, shouldThrow: Boolean): Boolean {
        return writeSync(File(file), append, shouldThrow)
    }

    /**
     * 异步写入文件
     * @param append    是否追加写入
     * */
    override fun writeAsync(
        file: String,
        callback: IoCallback?,
        append: Boolean,
        shouldThrow: Boolean
    ) {
        writeAsync(File(file), callback, append, shouldThrow)
    }

    /**
     * 异步写入文件
     * @param append    是否追加写入
     * */
    override fun writeAsync(
        file: File,
        callback: IoCallback?,
        append: Boolean,
        shouldThrow: Boolean
    ) {
        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                if (writeSync(file, append, shouldThrow)) {
                    callback?.onSuccess()
                } else {
                    callback?.onFailed()
                }
                return ""
            }
        })?.start()
    }

    override fun writeAsync(stream: OutputStream, callback: IoCallback?, shouldThrow: Boolean) {
        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                if (writeSync(stream, shouldThrow)) {
                    callback?.onSuccess()
                } else {
                    callback?.onFailed()
                }
                return ""
            }
        })?.start()
    }
}