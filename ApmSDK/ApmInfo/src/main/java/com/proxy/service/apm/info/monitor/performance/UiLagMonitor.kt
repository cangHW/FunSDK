package com.proxy.service.apm.info.monitor.performance

import android.app.Activity
import android.app.Application
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Choreographer
import android.view.FrameMetrics
import android.view.Window
import androidx.annotation.RequiresApi
import com.proxy.service.apm.info.cache.CacheManager
import com.proxy.service.apm.info.config.controller.MonitorConfig
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.utils.FileUtils
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.app.context.callback.AbstractActivityLifecycle
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.data.time.CsTimeManager
import com.proxy.service.core.framework.io.file.write.CsFileWriteUtils
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.handler.option.IHandlerOption
import java.io.File
import java.util.WeakHashMap


/**
 * @author: cangHX
 * @data: 2025/4/21 20:48
 * @desc:
 */
class UiLagMonitor {

    companion object {
        private const val TAG: String = "${Constants.TAG}UiLag"

        private const val UNIT = 1000000

        private const val DEFAULT_DURATION = 16666666
        private const val MEDIUM_DURATION = 50000000
        private const val HIGH_DURATION = 100000000

        private val mInstance by lazy { UiLagMonitor() }

        fun getInstance(): UiLagMonitor {
            return mInstance
        }
    }

    private var fileDir: String = ""
    private var handler: IHandlerOption? = null

    fun init(application: Application, config: MonitorConfig) {
        if (!config.getEnable()){
            return
        }

        fileDir = FileUtils.getDefaultDir(application, "performance/ui_lag/")
        CacheManager.getInstance().startWatch(fileDir, config)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            checkUiLagN()
        } else {
            checkUiLag()
        }
    }

    private fun checkUiLagN() {
        CsContextManager.addActivityLifecycleCallback(
            null,
            true,
            object : AbstractActivityLifecycle() {
                private val handlerThread = HandlerThread("UiLagMonitor-FrameMetricsAvailable")

                init {
                    handlerThread.start()
                }

                private val handler = Handler(handlerThread.looper)

                private val weakHashMap =
                    WeakHashMap<Activity, Window.OnFrameMetricsAvailableListener>()

                override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
                    super.onActivityPreCreated(activity, savedInstanceState)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        val listener =
                            FrameMetricsAvailableListenerImpl(activity, this@UiLagMonitor)
                        weakHashMap.put(activity, listener)
                        activity.window.addOnFrameMetricsAvailableListener(listener, handler)
                    }
                }

                override fun onActivityPostDestroyed(activity: Activity) {
                    super.onActivityPostDestroyed(activity)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        weakHashMap.get(activity)?.let {
                            activity.window.removeOnFrameMetricsAvailableListener(it)
                        }
                        weakHashMap.remove(activity)
                    }
                }
            }
        )
    }

    private var lastFrameTimeNanos: Long = 0
    private fun checkUiLag() {
        Choreographer.getInstance().postFrameCallback(object : Choreographer.FrameCallback {
            override fun doFrame(frameTimeNanos: Long) {
                if (frameTimeNanos != 0L && lastFrameTimeNanos != 0L) {
                    val frameDurationNanos: Long = frameTimeNanos - lastFrameTimeNanos
                    if (frameDurationNanos > MEDIUM_DURATION) {
                        CsLogger.tag(TAG).i("Frame took too long: ${frameDurationNanos / UNIT}ms")
                    }
                }
                lastFrameTimeNanos = frameTimeNanos
                Choreographer.getInstance().postFrameCallback(this)
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private class FrameMetricsAvailableListenerImpl(
        private val activity: Activity,
        private val uiLag: UiLagMonitor
    ) : Window.OnFrameMetricsAvailableListener {

        override fun onFrameMetricsAvailable(
            window: Window?,
            frameMetrics: FrameMetrics?,
            dropCountSinceLastInvocation: Int
        ) {
            val totalDuration = frameMetrics?.getMetric(FrameMetrics.TOTAL_DURATION) ?: 0
            val layoutMeasureDuration =
                frameMetrics?.getMetric(FrameMetrics.LAYOUT_MEASURE_DURATION) ?: 0
            val drawDuration = frameMetrics?.getMetric(FrameMetrics.DRAW_DURATION) ?: 0

            if (totalDuration <= DEFAULT_DURATION) {
                return
            }

            var type = 0
            val builder = StringBuilder("")
            if (layoutMeasureDuration > HIGH_DURATION || drawDuration > HIGH_DURATION) {
                type = 3
                builder.append("视图渲染触发严重卡顿, activity = $activity").append("\n")
            } else if (layoutMeasureDuration > MEDIUM_DURATION || drawDuration > MEDIUM_DURATION) {
                type = 2
                builder.append("视图渲染触发明显卡顿, activity = $activity").append("\n")
            } else if (layoutMeasureDuration > DEFAULT_DURATION || drawDuration > DEFAULT_DURATION) {
                type = 1
                builder.append("视图渲染触发轻微卡顿, activity = $activity").append("\n")
            }

            if (builder.isNotEmpty()) {
                val content = builder.append("Total duration: ${totalDuration / UNIT}ms")
                    .append("\n")
                    .append("Layout and measure duration: ${layoutMeasureDuration / UNIT}ms")
                    .append("\n")
                    .append("Draw duration: ${drawDuration / UNIT}ms")
                    .append("\n").toString()
                CsLogger.tag(TAG).e(content)

                if (type >= 2) {
                    uiLag.logfile(content)
                }
            }
        }
    }

    private fun logfile(content: String) {
        if (handler == null) {
            handler = CsTask.launchTaskGroup("UiLagMonitor-log")
        }
        handler?.start {
            val timeString = CsTimeManager.createFactory().get("yyyy年MM月dd日")
            val file = File(fileDir, "$timeString.txt")
            try {
                CsFileWriteUtils.setSourceString(content)
                    .writeSync(file, append = true, shouldThrow = true)
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).d(throwable)
            }
        }
    }
}