package com.proxy.service.apm.info

import android.annotation.SuppressLint
import android.app.Application
import com.proxy.service.annotations.CloudApiService
import com.proxy.service.apm.info.config.ApmConfig
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.anr.AnrMonitor
import com.proxy.service.apm.info.monitor.crash.java_crash.JavaCrashMonitor
import com.proxy.service.apm.info.monitor.crash.native_crash.NativeCrashMonitor
import com.proxy.service.apm.info.monitor.performance.lag.mainthread.MainThreadLagMonitor
import com.proxy.service.apm.info.monitor.performance.lag.ui.UiLagMonitor
import com.proxy.service.apm.info.utils.FileUtils
import com.proxy.service.core.application.base.CsBaseConfig
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.monitor.info.FileInfo
import java.io.File

/**
 * APM 模块 Cloud 自动配置入口。
 *
 * 在 Application 初始化时拉起各监控器，优先级 -999 尽早执行。
 *
 * @author: cangHX
 * @date: 2025/4/12 16:31
 */
@CloudApiService(serviceTag = "cs_config/apm")
class ApmMonitorConfig : CsBaseConfig() {

    companion object {
        private const val TAG = "${Constants.TAG}ApmMonitorConfig"

         fun checkMonitorFileCache(application: Application?) {
            try {
                val handler = CsApmMonitor.getExceptionHandler() ?: return

                var ctx = application
                if (application == null && CoreConfig.isFrameworkInitFinish) {
                    ctx = CsContextManager.getApplication()
                }

                if (ctx == null) {
                    return
                }

                val rootFile = FileUtils.getDefaultDir(ctx, "")
                val tempDir = FileUtils.getDefaultDir(ctx, Constants.TEMP_DIR_NAME)
                val fileList = ArrayList<FileInfo>()

                CsFileUtils.listFiles(rootFile)?.forEach {
                    if (it.absolutePath.startsWith(tempDir)) {
                        return@forEach
                    }
                    if (CsFileUtils.isFile(it)) {
                        val info = FileInfo()
                        info.filePath = it.absolutePath
                        info.fileName = it.name
                        info.fileLength = it.length()
                        info.lastModified = it.lastModified()
                        fileList.add(info)
                    }
                }

                handler.onException(fileList)
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
        }
    }

    override fun moduleType(): ModuleType {
        return ModuleType.SDK
    }

    @SuppressLint("Range")
    override fun priority(): Int {
        return -985
    }

    override fun onCreate(application: Application, isDebug: Boolean) {
        val config = CsApmMonitor.getConfig()

        checkMonitorFileCache(application)

        initCrashMonitor(application, config)
        initPerformanceMonitor(application, config)
        initAnrMonitor(application, config)
    }

    private fun initCrashMonitor(application: Application, config: ApmConfig) {
        val cConfig = config.getJavaCrashMonitorConfig()
        JavaCrashMonitor.getInstance().init(application, config, cConfig)

        val ncConfig = config.getNativeCrashMonitorConfig()
        NativeCrashMonitor.getInstance().init(application, config, ncConfig)
    }

    private fun initPerformanceMonitor(application: Application, config: ApmConfig) {
        val mtlConfig = config.getMainThreadLagMonitorConfig()
        MainThreadLagMonitor.getInstance().init(application, config, mtlConfig)

        val uilConfig = config.getUiLagMonitorConfig()
        UiLagMonitor.getInstance().init(application, config, uilConfig)
    }

    private fun initAnrMonitor(application: Application, config: ApmConfig) {
        val anrConfig = config.getAnrMonitorConfig()
        AnrMonitor.getInstance().init(application, config, anrConfig)
    }


}