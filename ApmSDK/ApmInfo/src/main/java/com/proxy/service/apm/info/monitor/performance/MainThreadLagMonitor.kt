package com.proxy.service.apm.info.monitor.performance

import android.app.Application
import android.os.Looper
import android.os.SystemClock
import android.util.Printer
import com.proxy.service.apm.info.cache.CacheManager
import com.proxy.service.apm.info.common.CommonLog
import com.proxy.service.apm.info.config.controller.MonitorConfig
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.utils.FileUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.data.time.CsTimeManager
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.io.File


/**
 * 主线程卡顿监测器
 *
 * @author: cangHX
 * @data: 2025/4/12 17:31
 * @desc:
 */
class MainThreadLagMonitor private constructor() {

    companion object {
        private const val TAG: String = "${Constants.TAG}MainThreadLag"
        private const val LAG_THRESHOLD: Long = 1000

        private val mInstance by lazy { MainThreadLagMonitor() }

        fun getInstance(): MainThreadLagMonitor {
            return mInstance
        }
    }

    private var fileDir: String = ""
    private var startTime: Long = 0

    fun init(application: Application, config: MonitorConfig) {
        if (!config.getEnable()){
            return
        }

        fileDir = FileUtils.getDefaultDir(application, "performance/main_thread_lag/")
        CacheManager.getInstance().startWatch(fileDir, config)

        Looper.getMainLooper().setMessageLogging { x ->
            if (x?.startsWith(">>>>> Dispatching to") == true) {
                startTime = SystemClock.uptimeMillis()
            } else if (x?.startsWith("<<<<< Finished to") == true) {
                val endTime = SystemClock.uptimeMillis()
                checkIsLag(endTime - startTime)
            }
        }
    }

    private fun checkIsLag(duration: Long) {
        if (duration < LAG_THRESHOLD) {
            return
        }
        val thread = Thread.currentThread()
        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                val stackTrace = thread.stackTrace.joinToString("\n")

                val time = System.currentTimeMillis()

                var timeString = CsTimeManager.createFactory(time)
                    .get("yyyy-MM-dd HH:mm:ss.SSS")

                val builder = StringBuilder()
                builder.append("卡顿发生时间: $timeString").append("\n").append("\n")
                builder.append(Constants.DIVIDER).append("\n")
                builder.append("卡顿时长: ${duration}ms").append("\n")
                builder.append("卡顿位置").append("\n").append("\n")
                builder.append(stackTrace).append("\n").append("\n")

                timeString = CsTimeManager.createFactory(time)
                    .get("yyyy年MM月dd日HH时mm分ss秒SSS毫秒")
                val file = File(fileDir, "$timeString.txt")
                CsFileWriteUtils.setSourceString(builder.toString()).writeSync(file)

                CommonLog.logAll(file.absolutePath) {
                    if (it) {
                        CsLogger.tag(TAG).i("触发主线程卡顿，日志记录成功")
                    } else {
                        CsLogger.tag(TAG).i("触发主线程卡顿，日志记录失败")
                    }
                }
                return ""
            }
        })?.start()
    }

}