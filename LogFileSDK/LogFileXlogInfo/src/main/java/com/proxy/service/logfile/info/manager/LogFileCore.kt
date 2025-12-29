package com.proxy.service.logfile.info.manager

import android.app.Application
import android.text.TextUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.logfile.info.config.LogConfig
import com.proxy.service.logfile.info.constants.Constants
import com.tencent.mars.xlog.Xlog.XLogConfig
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @data: 2025/12/29 10:56
 * @desc:
 */
class LogFileCore private constructor() {

    companion object {
        init {
            System.loadLibrary("logfile_xlog")
        }

        private val _instance by lazy {
            LogFileCore()
        }

        fun getInstance(): LogFileCore {
            return _instance
        }
    }

    private val isInitSuccess = AtomicBoolean(false)

    fun init(application: Application, logConfig: LogConfig) {
//        val config = Strategy()
//        config._pkg = application.packageName
//        config._flushEveryTime = logConfig.getFlushEveryTime()
//        config._isSyncMode = if (logConfig.getSyncMode()){
//            1
//        }else{
//            0
//        }
//
//        config._compressionMode = logConfig.getCompressionMode().mode
//        config._encryptionMode = logConfig.getEncryptionMode().mode
//        config._encryptionKey = logConfig.getEncryptionKey()
//        config._dir = if (TextUtils.isEmpty(logConfig.getLogDir())) {
//            getDefaultDir(application)
//        } else {
//            logConfig.getLogDir()
//        }
//        CsFileUtils.createDir(config._dir)
//
//        config._namePrefix = logConfig.getFileNamePrefix()
//        config._namePostfix = logConfig.getFileNamePostfix()
//        config._cacheTime = logConfig.getCacheTime()
//        config._cleanTaskIntervalTime = logConfig.getCleanTaskIntervalTime()
//        config._type = logConfig.getLogType()
//        config._singleFileMaxSize = logConfig.getSingleFileMaxSize()
//        config._maxFileCount = logConfig.getMaxFileCount()
//        config._hour = logConfig.getDailyHour()
//        config._minute = logConfig.getDailyMinute()
//        if (initTask(config)) {
//            isInitSuccess.set(true)
//            CsLogger.tag(Constants.TAG).d("init success")
//        } else {
//            isInitSuccess.set(false)
//            CsLogger.tag(Constants.TAG).d("init failed")
//        }
    }

    fun isInitSuccess(): Boolean {
        return isInitSuccess.get()
    }

    private fun getDefaultDir(application: Application): String {
        val file = application.getExternalFilesDir("logfile")
        CsFileUtils.createDir(file)
        return "${file?.absolutePath ?: ""}${File.separator}"
    }

    // ==================== Native 方法 ====================
    /**
     * 打开 appender
     * @param config 配置
     */
    private external fun initTask(strategy: Strategy):Boolean

}