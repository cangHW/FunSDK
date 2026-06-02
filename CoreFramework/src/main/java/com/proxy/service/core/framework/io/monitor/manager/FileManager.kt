package com.proxy.service.core.framework.io.monitor.manager

import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.monitor.callback.FileMonitorCallback
import com.proxy.service.core.framework.io.monitor.constants.Constants
import com.proxy.service.core.framework.io.monitor.info.FileInfo
import com.proxy.service.core.framework.io.monitor.observer.FileObserverImpl
import com.proxy.service.core.service.task.CsTask
import java.io.File
import java.io.FilenameFilter
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicBoolean

/**
 * @author: cangHX
 * @date: 2025/4/23 11:12
 * @desc:
 */
class FileManager private constructor(
    private val rootPath: String,
    private val callback: FileMonitorCallback
) : IManager {

    interface ActionCallback {
        fun onFileAdded(path: String)
        fun onFileChanged(path: String)
        fun onFileRemove(path: String)
    }

    companion object {
        private const val TAG: String = "${CoreConfig.TAG}FileManager"

        fun create(rootPath: String, callback: FileMonitorCallback): FileManager {
            val manager = FileManager(rootPath, callback)
            if (CsFileUtils.isFile(rootPath) || CsFileUtils.isDir(rootPath)) {
                manager.whenFileAdded(File(rootPath), false, null)
            } else {
                CsLogger.tag(TAG).e("The file or folder does not exist.")
            }
            return manager
        }
    }

    private val groupName = "${Constants.TASK_NAME_START}${System.currentTimeMillis()}"

    private val isStart = AtomicBoolean(false)
    private var handler = CsTask.launchTaskGroup(groupName)

    private val observerMapping = ConcurrentHashMap<String, FileObserverImpl>()
    private val totalFileInfos = ConcurrentHashMap<String, FileInfo>()


    private val actionCallback = object : ActionCallback {
        override fun onFileAdded(path: String) {
            handler?.start {
                val list = ArrayList<String>()
                whenFileAdded(File(path), true, list)

                if (isStart.get() && list.size > 0) {
                    callback.onFileAdded(ArrayList(totalFileInfos.values), list)
                }
            }
        }

        override fun onFileChanged(path: String) {
            handler?.start {
                if (CsFileUtils.isFile(path)) {
                    totalFileInfos.put(path, createFileInfo(File(path)))
                }

                if (isStart.get()) {
                    callback.onFileChanged(ArrayList(totalFileInfos.values))
                }
            }
        }

        override fun onFileRemove(path: String) {
            handler?.start {
                val list = ArrayList<String>()
                whenFileRemoved(File(path), list)

                if (isStart.get() && list.size > 0) {
                    callback.onFileRemoved(ArrayList(totalFileInfos.values), list)
                }

                if (isDirContainsFile(path, rootPath)) {
                    stopWatching()
                }
            }
        }
    }

    override fun startWatching() {
        if (isStart.compareAndSet(false, true)) {
            callback.onStart(ArrayList(totalFileInfos.values))
            observerMapping.forEach {
                it.value.startWatching()
            }
        }
    }

    override fun stopWatching() {
        if (isStart.compareAndSet(true, false)) {
            observerMapping.forEach {
                it.value.stopWatching()
            }
            callback.onClose(ArrayList(totalFileInfos.values))
        }
    }

    private fun createFileInfo(file: File): FileInfo {
        val info = FileInfo()
        info.fileName = file.name
        info.filePath = file.absolutePath
        info.fileLength = file.length()
        info.lastModified = file.lastModified()
        return info
    }

    private fun whenFileAdded(file: File, autoStart: Boolean, changedFiles: ArrayList<String>?) {
        if (CsFileUtils.isFile(file)) {
            totalFileInfos.put(file.absolutePath, createFileInfo(file))
            changedFiles?.add(file.absolutePath)
            return
        }

        if (CsFileUtils.isDir(file)) {
            val observer = FileObserverImpl(file.absolutePath, actionCallback)
            observerMapping.put(file.absolutePath, observer)
            if (autoStart) {
                observer.startWatching()
            }

            file.listFiles()?.forEach {
                whenFileAdded(it, autoStart, changedFiles)
            }
        }
    }

    private fun whenFileRemoved(file: File, changedFiles: ArrayList<String>) {
        HashMap(totalFileInfos).keys.forEach {
            if (isDirContainsFile(file.absolutePath, it)) {
                totalFileInfos.remove(it)
                changedFiles.add(it)
            }
        }

        HashMap(observerMapping).keys.forEach {
            if (isDirContainsFile(file.absolutePath, it)) {
                observerMapping.remove(it)?.stopWatching()
            }
        }
    }

    private fun isDirContainsFile(dir: String, file: String): Boolean {
        val dirStr = if (dir.endsWith(File.separator)) {
            dir
        } else {
            "$dir${File.separator}"
        }
        val fileStr = if (file.endsWith(File.separator)) {
            file
        } else {
            "$file${File.separator}"
        }
        return fileStr == dirStr || fileStr.startsWith(dirStr)
    }
}