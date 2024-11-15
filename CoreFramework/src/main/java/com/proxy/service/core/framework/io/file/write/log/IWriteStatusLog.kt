package com.proxy.service.core.framework.io.file.write.log

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.data.time.CsTimeUtils
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/10/21 11:26
 * @desc:
 */
interface IWriteStatusLog {

    companion object {
        private val localTime = ThreadLocal<Long>()
    }

    fun start(tag: String, path: String) {
        CsLogger.tag(tag).d("write start. path : $path")
        localTime.set(System.currentTimeMillis())
    }

    fun success(tag: String, path: String) {
        localTime.get()?.let {
            val time = CsTimeUtils.toIntervalString(
                System.currentTimeMillis() - it,
                TimeUnit.MILLISECONDS
            )
            CsLogger.tag(tag).d("write success. time : $time, path : $path")
        }
    }

}