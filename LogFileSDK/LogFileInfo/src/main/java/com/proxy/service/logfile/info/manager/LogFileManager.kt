package com.proxy.service.logfile.info.manager

import android.app.Application
import android.text.TextUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.logfile.info.config.LogConfig
import com.proxy.service.logfile.info.constants.Constants
import java.io.File
import java.util.concurrent.atomic.AtomicBoolean

class LogFileManager {

    companion object {
        init {
            System.loadLibrary("logfile")
        }

        private val mInstance by lazy {
            LogFileManager()
        }

        fun getInstance(): LogFileManager {
            return mInstance
        }
    }

    private val isInitSuccess = AtomicBoolean(false)

    fun init(application: Application, logConfig: LogConfig) {
        val config = Strategy()
        config._pkg = application.packageName
        config._isSync = logConfig.getSync()
        config._dir = if (TextUtils.isEmpty(logConfig.getLogDir())) {
            getDefaultDir(application)
        } else {
            logConfig.getLogDir()
        }

        config._namePrefix = logConfig.getFileNamePrefix()
        config._namePostfix = logConfig.getFileNamePostfix()
        config._cacheTime = logConfig.getCacheTime()
        config._type = logConfig.getLogType()
        config._maxFileSize = logConfig.getMaxFileSize()
        config._maxFiles = logConfig.getMaxFiles()
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

    external fun log(level: String, tag: String, msg: String)

    external fun flush()
}