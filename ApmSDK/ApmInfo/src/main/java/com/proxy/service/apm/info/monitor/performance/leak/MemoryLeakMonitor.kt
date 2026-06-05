package com.proxy.service.apm.info.monitor.performance.leak

import android.app.Application
import com.proxy.service.apm.info.config.ApmConfig
import com.proxy.service.apm.info.config.controller.performance.leak.MemoryLeakConfig
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.apm.info.monitor.base.AbstractMonitor
import com.proxy.service.apm.info.monitor.performance.leak.composite.AppBackgroundCallback
import com.proxy.service.apm.info.monitor.performance.leak.composite.FullCompositeReporter
import com.proxy.service.apm.info.monitor.performance.leak.composite.LiteCompositeReporter
import com.proxy.service.apm.info.monitor.performance.leak.reporter.MemoryLeakFileReporter
import com.proxy.service.apm.info.monitor.performance.leak.reporter.MemoryLeakNotifyReporter
import com.proxy.service.apm.info.monitor.performance.leak.watcher.LifecycleWatcherInstaller
import com.proxy.service.apm.info.monitor.performance.leak.watcher.ObjectWatcher
import com.proxy.service.apm.info.report.impl.CallbackReporter
import com.proxy.service.apm.info.utils.FileUtils
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.io.file.CsFileUtils

/**
 * @author: cangHX
 * @date: 2026/6/4 16:49
 * @desc:
 */
class MemoryLeakMonitor private constructor() : AbstractMonitor<MemoryLeakConfig>() {

    companion object {
        private const val TAG = "${Constants.TAG}MemoryLeakMonitor"

        private val mInstance by lazy { MemoryLeakMonitor() }

        fun getInstance(): MemoryLeakMonitor = mInstance
    }

    @Volatile
    private var activeWatcher: ObjectWatcher? = null

    private var installer: LifecycleWatcherInstaller? = null

    /**
     * 手动监控自定义对象（需 Monitor 已启动）。
     */
    fun watchObject(obj: Any, description: String) {
        activeWatcher?.watch(obj, description)
    }

    override fun start(application: Application, apmConfig: ApmConfig, config: MemoryLeakConfig) {
        val tempDir = getTempLogFileDir()
        CsFileUtils.delete(tempDir)
        CsFileUtils.createDir(tempDir)

        val reporter = if (CsAppUtils.isDebuggable()) {
            FullCompositeReporter(
                tempDir,
                config,
                MemoryLeakFileReporter(getLogFileDir(application), config),
                CallbackReporter(apmConfig.getMemoryLeakReporter())
            )
        } else {
            LiteCompositeReporter(
                MemoryLeakNotifyReporter(config),
                CallbackReporter(apmConfig.getMemoryLeakReporter())
            )
        }

        val watcher = ObjectWatcher(config, reporter)
        installer = LifecycleWatcherInstaller(watcher, reporter as? AppBackgroundCallback?)
        installer?.install()
        activeWatcher = watcher
    }

    override fun getLogFileDir(application: Application): String {
        return FileUtils.getDefaultDir(application, "performance/leak/")
    }

    private fun getTempLogFileDir(): String {
        val application = CsContextManager.getApplication()
        return FileUtils.getDefaultDir(application, "${Constants.TEMP_DIR_NAME}/leak/")
    }

    override fun stop() {
        super.stop()
        activeWatcher = null
        installer?.uninstall()
    }
}