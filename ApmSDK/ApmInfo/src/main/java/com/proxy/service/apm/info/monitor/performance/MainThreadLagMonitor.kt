package com.proxy.service.apm.info.monitor.performance

import android.app.Application
import android.os.Looper
import android.os.SystemClock
import android.util.Printer
import com.proxy.service.apm.info.cache.CacheManager
import com.proxy.service.apm.info.common.CommonLog
import com.proxy.service.apm.info.config.controller.Controller
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

    fun init(application: Application, controller: Controller) {
        fileDir = FileUtils.getDefaultDir(application, "performance/main_thread_lag/")
        CacheManager.getInstance().startWatch(fileDir, controller)

        Looper.getMainLooper().setMessageLogging(object : Printer {
            override fun println(x: String?) {
                if (x?.startsWith(">>>>> Dispatching to") == true) {
                    startTime = SystemClock.uptimeMillis()
                } else if (x?.startsWith("<<<<< Finished to") == true) {
                    val endTime = SystemClock.uptimeMillis()
                    checkIsLag(endTime - startTime)
                }
            }
        })
    }

    private fun checkIsLag(duration: Long) {
        if (duration < LAG_THRESHOLD) {
            return
        }
        val thread = Thread.currentThread()
        CsTask.ioThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                val stackTrace = thread.stackTrace
                    .joinToString("\n")

                val builder = StringBuilder()
                builder.append(
                    "卡顿发生时间: ${
                        CsTimeManager.createFactory().get("yyyy-MM-dd HH:mm:ss.SSS")
                    }"
                ).append("\n").append("\n")

                builder.append(Constants.DIVIDER).append("\n")
                builder.append("卡顿时长: ${duration}ms").append("\n")
                builder.append("卡顿位置").append("\n").append("\n")
                builder.append(stackTrace).append("\n").append("\n")

                val fileName = "${
                    CsTimeManager.createFactory()
                        .get("yyyy年MM月dd日HH时mm分ss秒SSS毫秒")
                }.txt"
                val file = File(fileDir, fileName)
                CsFileWriteUtils.setSourceString(builder.toString()).writeSync(file)

                CommonLog.logAll(file.absolutePath) {
                    CsLogger.tag(TAG)
                        .i("触发主线程卡顿，日志记录${if (it) "成功" else "失败"}")
                }
                return ""
            }
        })?.start()
    }

}