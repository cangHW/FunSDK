package com.proxy.service.core.framework.io.file.write.log

import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.data.time.CsTimeUtils
import java.util.WeakHashMap
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/10/21 11:26
 * @desc:
 */
interface IWriteStatusLog {

    companion object {
        private val localTime = WeakHashMap<IWriteStatusLog, Long>()
    }

    fun start(tag: String, path: String) {
        CsLogger.tag(tag).d("write start. path : $path")
        localTime[this] = System.currentTimeMillis()
    }

    fun success(tag: String, path: String) {
        val start = localTime[this] ?: return
        val time = CsTimeUtils.toIntervalString(
            System.currentTimeMillis() - start,
            TimeUnit.MILLISECONDS
        )
        CsLogger.tag(tag).d("write success. time : $time, path : $path")
    }

}