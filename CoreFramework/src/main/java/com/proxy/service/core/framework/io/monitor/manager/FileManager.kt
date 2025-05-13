package com.proxy.service.core.framework.io.monitor.manager

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
 * @data: 2025/4/23 11:12
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
        fun create(rootPath: String, callback: FileMonitorCallback): FileManager {
            return FileManager(rootPath, callback)
        }
    }

    private val isStart = AtomicBoolean(false)
    private var handler =
        CsTask.launchTaskGroup("${Constants.TASK_NAME_START}${System.currentTimeMillis()}")
    private val totalFileInfos = ConcurrentHashMap<String, FileInfo>()
    private val actionCallback = object : ActionCallback {
        override fun onFileAdded(path: String) {
            handler?.start {
                val file = File(path)
                val list = ArrayList<String>()

                if (CsFileUtils.isFile(file)) {
                    totalFileInfos.put(path, createFileInfo(file))
                    list.add(path)
                } else if (CsFileUtils.isDir(file)) {
                    file.listFiles(object : FilenameFilter {
                        override fun accept(dir: File?, name: String?): Boolean {
                            return CsFileUtils.isFile(File(dir, name ?: ""))
                        }
                    })?.forEach {
                        list.add(it.absolutePath)
                        totalFileInfos.put(it.absolutePath, createFileInfo(it))
                    }
                }

                if (isStart.get() && list.size > 0) {
                    callback.onFileAdded(ArrayList(totalFileInfos.values), list)
                }
            }
        }

        override fun onFileChanged(path: String) {
            handler?.start {
                val file = File(path)

                if (CsFileUtils.isFile(file)) {
                    totalFileInfos.put(path, createFileInfo(file))
                }

                if (isStart.get()) {
                    callback.onFileChanged(ArrayList(totalFileInfos.values))
                }
            }
        }

        override fun onFileRemove(path: String) {
            handler?.start {
                val list = ArrayList<String>()

                HashMap(totalFileInfos).keys.forEach {
                    if (it.startsWith(path)) {
                        totalFileInfos.remove(it)
                        list.add(it)
                    }
                }

                if (isStart.get() && list.size > 0) {
                    callback.onFileRemoved(ArrayList(totalFileInfos.values), list)
                }
            }

            if (rootPath.startsWith(path)){
                stopWatching()
            }
        }
    }
    private val observer = FileObserverImpl(rootPath, totalFileInfos, actionCallback)

    override fun startWatching() {
        if (isStart.compareAndSet(false, true)) {
            totalFileInfos.clear()
            observer.init()
            callback.onStart(ArrayList(totalFileInfos.values))
            observer.startWatching()
        }
    }

    override fun stopWatching() {
        if (isStart.compareAndSet(true, false)) {
            observer.stopWatching()
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
}