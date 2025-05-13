package com.proxy.service.apm.info.cache

import com.proxy.service.apm.info.config.controller.Controller
import com.proxy.service.apm.info.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.monitor.CsFileMonitorUtils
import com.proxy.service.core.framework.io.monitor.callback.FileMonitorCallback
import com.proxy.service.core.framework.io.monitor.info.FileInfo
import com.proxy.service.core.framework.io.monitor.manager.IManager
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.util.Collections


/**
 * @author: cangHX
 * @data: 2025/4/22 19:23
 * @desc:
 */
class CacheManager {

    companion object {
        private const val TAG: String = "${Constants.TAG}CacheManager"

        private val mInstance by lazy { CacheManager() }

        fun getInstance(): CacheManager {
            return mInstance
        }
    }

    private val map = HashMap<String, IManager>()

    fun startWatch(path: String, controller: Controller) {
        if (map.containsKey(path)) {
            return
        }
        val manager = CsFileMonitorUtils.createMonitor(
            path,
            FileMonitorCallbackImpl(
                controller.getMaxFileCount(),
                controller.getAllFilesMaxSize(),
                controller.getMaxCacheTime()
            )
        )
        manager.startWatching()
        map.put(path, manager)
    }

    private class FileMonitorCallbackImpl(
        private val maxFileCount: Int,
        private val maxFilesSize: Long,
        private val maxCacheTime: Long
    ) : FileMonitorCallback {
        override fun onStart(totalFileInfos: ArrayList<FileInfo>) {
            super.onStart(totalFileInfos)
            CsLogger.tag(TAG).i("onStart fileSize = ${totalFileInfos.size}")
            checkFiles(totalFileInfos)
        }

        override fun onFileAdded(totalFileInfos: ArrayList<FileInfo>, filePaths: List<String>) {
            super.onFileAdded(totalFileInfos, filePaths)
            CsLogger.tag(TAG).i("onFileAdded fileSize = ${totalFileInfos.size}")
            checkFiles(totalFileInfos)
        }

        override fun onFileChanged(totalFileInfos: ArrayList<FileInfo>) {
            super.onFileChanged(totalFileInfos)
            CsLogger.tag(TAG).i("onFileChanged fileSize = ${totalFileInfos.size}")
            checkFiles(totalFileInfos)
        }

        override fun onFileRemoved(totalFileInfos: ArrayList<FileInfo>, filePaths: List<String>) {
            super.onFileRemoved(totalFileInfos, filePaths)
            CsLogger.tag(TAG).i("onFileRemoved fileSize = ${totalFileInfos.size}")
        }

        private fun checkFiles(totalFileInfos: ArrayList<FileInfo>) {
            CsTask.ioThread()?.call(object : ICallable<String> {
                override fun accept(): String {
                    Collections.sort(totalFileInfos, comparatorImpl)

                    // 校验文件数量
                    if (maxFileCount > 0) {
                        while (totalFileInfos.size > maxFileCount) {
                            totalFileInfos.removeFirstOrNull()?.let {
                                CsFileUtils.delete(it.filePath)
                            }
                        }
                    }

                    // 校验文件缓存时间
                    if (maxCacheTime > 0) {
                        val lastTime = System.currentTimeMillis() - maxCacheTime
                        var firstFileLastModified = totalFileInfos.firstOrNull()?.lastModified
                        while ((firstFileLastModified ?: lastTime) < lastTime) {
                            totalFileInfos.removeFirstOrNull()?.let {
                                CsFileUtils.delete(it.filePath)
                            }
                            firstFileLastModified = totalFileInfos.firstOrNull()?.lastModified
                        }
                    }

                    // 校验全部文件总大小
                    if (maxFilesSize > 0) {
                        var currentDirSize = 0L
                        totalFileInfos.reversed().forEach {
                            if (currentDirSize + it.fileLength < maxFilesSize) {
                                currentDirSize += it.fileLength
                            } else {
                                CsFileUtils.delete(it.filePath)
                            }
                        }
                    }

                    return ""
                }
            })?.start()
        }

        private val comparatorImpl = object : Comparator<FileInfo> {
            override fun compare(o1: FileInfo?, o2: FileInfo?): Int {
                if ((o1?.lastModified ?: 0) > (o2?.lastModified ?: 0)) {
                    return 1
                } else if ((o1?.lastModified ?: 0) < (o2?.lastModified ?: 0)) {
                    return -1
                }
                return 0
            }
        }
    }

}