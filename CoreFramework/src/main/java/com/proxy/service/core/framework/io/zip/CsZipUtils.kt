package com.proxy.service.core.framework.io.zip

import androidx.annotation.WorkerThread
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.callback.MultiRunnableEmitter
import com.proxy.service.threadpool.base.thread.task.IConsumer
import com.proxy.service.threadpool.base.thread.task.IMultiRunnable
import net.lingala.zip4j.ZipFile
import net.lingala.zip4j.model.ZipParameters
import net.lingala.zip4j.model.enums.AesKeyStrength
import net.lingala.zip4j.model.enums.CompressionLevel
import net.lingala.zip4j.model.enums.CompressionMethod
import net.lingala.zip4j.model.enums.EncryptionMethod
import net.lingala.zip4j.progress.ProgressMonitor
import java.io.File

/**
 * 压缩、解压缩相关工具
 *
 * @author: cangHX
 * @date: 2024/4/30 10:26
 * @desc:
 */
@WorkerThread
object CsZipUtils {

    private const val TAG = "${CoreConfig.TAG}Zip"
    private const val RESULT_SUCCESS = -1
    private const val RESULT_FAILED = -2

    private val callbackThreadLocal = ThreadLocal<ZipCallback>()
    private val passwordThreadLocal = ThreadLocal<String>()

    /**
     * 设置操作回调
     * */
    fun setCallback(callback: ZipCallback): CsZipUtils {
        callbackThreadLocal.set(callback)
        return this
    }

    /**
     * 配置文件密码
     * */
    fun setPassword(password: String): CsZipUtils {
        passwordThreadLocal.set(password)
        return this
    }

    /**
     * 同步解压缩
     * @param src 压缩文件路径
     * @param dest 解压缩路径
     * */
    fun unzipSync(src: String, dest: String): Boolean {
        val callback = getCallback()
        val password = getPassword()

        var zipFile: ZipFile? = null
        try {
            zipFile = ZipFile(src)
            if (!zipFile.isValidZipFile) {
                callback?.onFailed()
                return false
            }
            if (zipFile.isEncrypted) {
                if (password.isNullOrBlank() || password.isEmpty()) {
                    callback?.onFailed()
                    return false
                }
                zipFile.setPassword(password.toCharArray())
            }
            zipFile.extractAll(dest)
            callback?.onSuccess()
            return true
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        } finally {
            try {
                zipFile?.close()
            } catch (_: Throwable) {
            }
        }
        callback?.onFailed()
        return false
    }

    /**
     * 异步解压缩
     * @param src 压缩文件路径
     * @param dest 解压缩路径
     * @param progressCallback 进度回调
     * */
    fun unzipAsync(src: String, dest: String, progressCallback: ZipProgressCallback? = null) {
        val callback = getCallback()
        val password = getPassword()

        try {
            val zipFile = ZipFile(src)
            if (!zipFile.isValidZipFile) {
                callback?.onFailed()
                return
            }
            if (zipFile.isEncrypted) {
                if (password.isNullOrBlank() || password.isEmpty()) {
                    callback?.onFailed()
                    return
                }
                zipFile.setPassword(password.toCharArray())
            }

            zipFile.isRunInThread = true
            val progressMonitor = zipFile.progressMonitor
            zipFile.extractAll(dest)

            CsTask.ioThread()?.call(object : IMultiRunnable<Int> {
                override fun accept(emitter: MultiRunnableEmitter<Int>) {
                    while (progressMonitor.state == ProgressMonitor.State.BUSY) {
                        emitter.onNext(progressMonitor.percentDone)
                        try {
                            Thread.sleep(50)
                        } catch (throwable: Throwable) {
                            CsLogger.tag(TAG).e(throwable)
                        }
                    }

                    if (progressMonitor.result == ProgressMonitor.Result.SUCCESS) {
                        emitter.onNext(RESULT_SUCCESS)
                    } else {
                        CsLogger.tag(TAG)
                            .e("unzipAsync failed. result=${progressMonitor.result}, exception=${progressMonitor.exception}")
                        emitter.onNext(RESULT_FAILED)
                    }
                    emitter.onComplete()

                    try {
                        zipFile.close()
                    } catch (_: Throwable) {
                    }
                }
            })?.mainThread()?.doOnNext(object : IConsumer<Int> {
                override fun accept(value: Int) {
                    when (value) {
                        RESULT_SUCCESS -> {
                            progressCallback?.onProgress(100)
                            callback?.onSuccess()
                        }
                        RESULT_FAILED -> {
                            callback?.onFailed()
                        }
                        else -> {
                            progressCallback?.onProgress(value)
                        }
                    }
                }
            })?.start()
        } catch (throwable: Throwable) {
            callback?.onFailed()
            CsLogger.tag(TAG).e(throwable)
        }
    }

    /**
     * 同步压缩
     * @param fileList 待压缩文件路径集合
     * @param dest 压缩文件路径
     * */
    fun zipSync(fileList: List<String>, dest: String) {
        val callback = getCallback()
        val password = getPassword()

        val destFile = File(dest)
        destFile.parentFile?.let {
            CsFileUtils.createDir(it)
        }

        val parameters = ZipParameters()
        parameters.compressionMethod = CompressionMethod.DEFLATE
        parameters.compressionLevel = CompressionLevel.NORMAL

        var zipFile: ZipFile? = null
        try {
            zipFile = if (!password.isNullOrBlank() && password.isNotEmpty()) {
                parameters.isEncryptFiles = true
                parameters.encryptionMethod = EncryptionMethod.AES
                parameters.aesKeyStrength = AesKeyStrength.KEY_STRENGTH_128
                ZipFile(dest, password.toCharArray())
            } else {
                ZipFile(dest)
            }

            fileList.forEach { path ->
                if (CsFileUtils.isFile(path)) {
                    zipFile.addFile(path, parameters)
                } else if (CsFileUtils.isDir(path)) {
                    zipFile.addFolder(File(path), parameters)
                }
            }
            callback?.onSuccess()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            callback?.onFailed()
        } finally {
            try {
                zipFile?.close()
            } catch (_: Throwable) {
            }
        }
    }

    /**
     * 异步压缩
     * @param fileList 待压缩文件路径集合
     * @param dest 压缩文件路径
     * @param progressCallback 进度回调
     * */
    fun zipAsync(fileList: List<String>, dest: String, progressCallback: ZipProgressCallback? = null) {
        val callback = getCallback()
        val password = getPassword()

        val destFile = File(dest)
        destFile.parentFile?.let {
            CsFileUtils.createDir(it)
        }

        val parameters = ZipParameters()
        parameters.compressionMethod = CompressionMethod.DEFLATE
        parameters.compressionLevel = CompressionLevel.NORMAL

        try {
            val zipFile = if (!password.isNullOrBlank() && password.isNotEmpty()) {
                parameters.isEncryptFiles = true
                parameters.encryptionMethod = EncryptionMethod.AES
                parameters.aesKeyStrength = AesKeyStrength.KEY_STRENGTH_128
                ZipFile(dest, password.toCharArray())
            } else {
                ZipFile(dest)
            }

            zipFile.isRunInThread = true
            val progressMonitor = zipFile.progressMonitor

            fileList.forEach { path ->
                if (CsFileUtils.isFile(path)) {
                    zipFile.addFile(path, parameters)
                } else if (CsFileUtils.isDir(path)) {
                    zipFile.addFolder(File(path), parameters)
                }
            }

            CsTask.ioThread()?.call(object : IMultiRunnable<Int> {
                override fun accept(emitter: MultiRunnableEmitter<Int>) {
                    while (progressMonitor.state == ProgressMonitor.State.BUSY) {
                        emitter.onNext(progressMonitor.percentDone)
                        try {
                            Thread.sleep(50)
                        } catch (throwable: Throwable) {
                            CsLogger.tag(TAG).e(throwable)
                        }
                    }

                    if (progressMonitor.result == ProgressMonitor.Result.SUCCESS) {
                        emitter.onNext(RESULT_SUCCESS)
                    } else {
                        CsLogger.tag(TAG)
                            .e("zipAsync failed. result=${progressMonitor.result}, exception=${progressMonitor.exception}")
                        emitter.onNext(RESULT_FAILED)
                    }
                    emitter.onComplete()

                    try {
                        zipFile.close()
                    } catch (_: Throwable) {
                    }
                }
            })?.mainThread()?.doOnNext(object : IConsumer<Int> {
                override fun accept(value: Int) {
                    when {
                        value == RESULT_SUCCESS -> {
                            progressCallback?.onProgress(100)
                            callback?.onSuccess()
                        }

                        value == RESULT_FAILED -> {
                            callback?.onFailed()
                        }

                        else -> {
                            progressCallback?.onProgress(value)
                        }
                    }
                }
            })?.start()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            callback?.onFailed()
        }
    }

    private fun getCallback(): ZipCallback? {
        val value = callbackThreadLocal.get()
        if (value != null) {
            callbackThreadLocal.remove()
        }
        return value
    }

    private fun getPassword(): String? {
        val value = passwordThreadLocal.get()
        if (value != null) {
            passwordThreadLocal.remove()
        }
        return value
    }

}