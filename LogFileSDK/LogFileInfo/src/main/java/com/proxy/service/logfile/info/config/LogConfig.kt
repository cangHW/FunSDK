package com.proxy.service.logfile.info.config

import android.text.TextUtils
import com.proxy.service.logfile.info.constants.Constants
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2025/1/16 19:39
 * @desc:
 */
class LogConfig private constructor(private val builder: IBuilderGet) : IBuilderGet {

    override fun getSync(): Boolean {
        return builder.getSync()
    }

    override fun getLogDir(): String {
        return builder.getLogDir()
    }

    override fun getFileNamePrefix(): String {
        return builder.getFileNamePrefix()
    }

    override fun getFileNamePostfix(): String {
        return builder.getFileNamePostfix()
    }

    override fun getCacheTime(): Long {
        return builder.getCacheTime()
    }

    override fun getLogType(): Int {
        return builder.getLogType()
    }

    override fun getMaxFileSize(): Long {
        return builder.getMaxFileSize()
    }

    override fun getMaxFiles(): Int {
        return builder.getMaxFiles()
    }

    override fun getDailyHour(): Int {
        return builder.getDailyHour()
    }

    override fun getDailyMinute(): Int {
        return builder.getDailyMinute()
    }

    companion object {
        fun builder(): IBuilder {
            return Builder()
        }
    }

    class Builder : IBuilder, IBuilderGet {
        private var isSync: Boolean = Constants.IS_SYNC

        private var dir: String = ""

        private var namePrefix: String = Constants.NAME_PREFIX
        private var namePostfix: String = Constants.NAME_POSTFIX

        private var cacheTime: Long = Constants.CACHE_TIME

        private var type: Int = Constants.TYPE_NORMAL

        private var maxFileSize: Long = Constants.MAX_FILE_SIZE
        private var maxFiles: Int = Constants.MAX_FILES

        private var hour: Int = Constants.HOUR
        private var minute: Int = Constants.MINUTE

        override fun setSync(isSync: Boolean): IBuilder {
            this.isSync = isSync
            return this
        }

        override fun setLogDir(dir: String): IBuilder {
            this.dir = if (dir.endsWith(File.separator)) {
                dir
            } else {
                "$dir${File.separator}"
            }
            return this
        }

        override fun setFileNamePrefix(namePrefix: String): IBuilder {
            this.namePrefix = if (TextUtils.isEmpty(namePrefix)) {
                Constants.NAME_PREFIX
            } else {
                namePrefix
            }
            return this
        }

        override fun setFileNamePostfix(namePostfix: String): IBuilder {
            this.namePostfix = if (TextUtils.isEmpty(namePostfix)) {
                Constants.NAME_POSTFIX
            } else if (namePostfix.startsWith(".")) {
                namePostfix
            } else {
                ".$namePostfix"
            }
            return this
        }

        override fun setCacheTime(time: Long, unit: TimeUnit): IBuilder {
            this.cacheTime = unit.toMillis(time)
            return this
        }

        override fun createNormalType(): LogConfig {
            this.type = Constants.TYPE_NORMAL
            return LogConfig(this)
        }

        override fun createRotatingType(maxFileSize: Long, maxFiles: Int): LogConfig {
            this.type = Constants.TYPE_ROTATING
            this.maxFileSize = if (maxFileSize < 0) {
                Constants.MAX_FILE_SIZE
            } else {
                maxFileSize
            }
            this.maxFiles = if (maxFiles < 0) {
                Constants.MAX_FILES
            } else {
                maxFiles
            }
            return LogConfig(this)
        }

        override fun createDailyType(hour: Int, minute: Int): LogConfig {
            this.type = Constants.TYPE_DAILY
            this.hour = if (hour < 0) {
                0
            } else {
                hour
            }
            this.minute = if (minute < 0) {
                0
            } else {
                minute
            }
            return LogConfig(this)
        }

        override fun getSync(): Boolean {
            return isSync
        }

        override fun getLogDir(): String {
            return dir
        }

        override fun getFileNamePrefix(): String {
            return namePrefix
        }

        override fun getFileNamePostfix(): String {
            return namePostfix
        }

        override fun getCacheTime(): Long {
            return cacheTime
        }

        override fun getLogType(): Int {
            return type
        }

        override fun getMaxFileSize(): Long {
            return maxFileSize
        }

        override fun getMaxFiles(): Int {
            return maxFiles
        }

        override fun getDailyHour(): Int {
            return hour
        }

        override fun getDailyMinute(): Int {
            return minute
        }
    }

}