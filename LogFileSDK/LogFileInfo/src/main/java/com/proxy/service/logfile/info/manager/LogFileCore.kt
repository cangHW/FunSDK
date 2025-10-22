package com.proxy.service.logfile.info.manager

import android.app.Application
import android.text.TextUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.logfile.info.config.LogConfig
import com.proxy.service.logfile.info.constants.Constants
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

class LogFileCore {

    companion object {
        init {
            System.loadLibrary("logfile")
        }

        private val mInstance by lazy {
            LogFileCore()
        }

        fun getInstance(): LogFileCore {
            return mInstance
        }
    }

    private val isInitSuccess = AtomicBoolean(false)

    fun init(application: Application, logConfig: LogConfig) {
        val config = Strategy()
        config._pkg = application.packageName
        config._flushEveryTime = logConfig.getFlushEveryTime()
        config._isSyncMode = logConfig.getSyncMode()

        config._compressionMode = logConfig.getCompressionMode().mode
        config._encryptionMode = logConfig.getEncryptionMode().mode
        config._encryptionKey = logConfig.getEncryptionKey()
        config._dir = if (TextUtils.isEmpty(logConfig.getLogDir())) {
            getDefaultDir(application)
        } else {
            logConfig.getLogDir()
        }
        CsFileUtils.createDir(config._dir)

        config._namePrefix = logConfig.getFileNamePrefix()
        config._namePostfix = logConfig.getFileNamePostfix()
        config._cacheTime = logConfig.getCacheTime()
        config._cleanTaskIntervalTime = logConfig.getCleanTaskIntervalTime()
        config._type = logConfig.getLogType()
        config._singleFileMaxSize = logConfig.getSingleFileMaxSize()
        config._maxFileCount = logConfig.getMaxFileCount()
        config._hour = logConfig.getDailyHour()
        config._minute = logConfig.getDailyMinute()
        if (initTask(config)) {
            isInitSuccess.set(true)
            CsLogger.tag(Constants.TAG).d("init success")
        } else {
            isInitSuccess.set(false)
            CsLogger.tag(Constants.TAG).d("init failed")
        }
    }

    fun isInitSuccess(): Boolean {
        return isInitSuccess.get()
    }

    private fun getDefaultDir(application: Application): String {
        val file = application.getExternalFilesDir("logfile")
        CsFileUtils.createDir(file)
        return "${file?.absolutePath ?: ""}${File.separator}"
    }

    private external fun initTask(logConfig: Strategy): Boolean

    external fun logV(level: String, tag: String, msg: String)

    external fun logD(level: String, tag: String, msg: String)

    external fun logI(level: String, tag: String, msg: String)

    external fun logW(level: String, tag: String, msg: String)

    external fun logE(level: String, tag: String, msg: String)

    external fun logA(level: String, tag: String, msg: String)

    external fun flush()
}