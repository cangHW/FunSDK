package com.proxy.service.core.framework.io.monitor.observer

import android.os.FileObserver
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.framework.io.monitor.constants.Constants
import com.proxy.service.core.framework.io.monitor.info.FileInfo
import com.proxy.service.core.framework.io.monitor.manager.FileManager
import java.io.File
import java.io.FilenameFilter
import java.util.concurrent.ConcurrentHashMap

/**
 * @author: cangHX
 * @data: 2025/4/23 10:58
 * @desc:
 */
class FileObserverImpl(
    private val rootPath: String,
    private val fileMapping: ConcurrentHashMap<String, FileInfo>,
    private val callback: FileManager.ActionCallback
) : FileObserver(rootPath, Constants.MASK) {

    companion object {
        private const val TAG: String = "${CoreConfig.TAG}FileObserver"
    }

    private val observerMapping = ConcurrentHashMap<String, FileObserverImpl>()

    fun init() {
        observerMapping.clear()
        val rootFile = File(rootPath)
        rootFile.listFiles()?.forEach {
            if (CsFileUtils.isFile(it)) {
                val info = FileInfo()
                info.filePath = it.absolutePath
                info.fileName = it.name
                info.fileLength = it.length()
                info.lastModified = it.lastModified()
                fileMapping.put(info.filePath, info)
            } else if (CsFileUtils.isDir(it)) {
                val observer = FileObserverImpl(it.absolutePath, fileMapping, callback)
                observer.init()
                observerMapping.put(it.absolutePath, observer)
            }
        }
    }

    override fun startWatching() {
        super.startWatching()
        observerMapping.forEach {
            it.value.startWatching()
        }
    }

    override fun stopWatching() {
        super.stopWatching()
        observerMapping.forEach {
            it.value.stopWatching()
        }
    }

    override fun onEvent(event: Int, path: String?) {
        val fileName = path ?: ""
        val file = File(rootPath, fileName)

        if ((event and CREATE) != 0) {
            CsLogger.tag(TAG).d("文件或目录被创建 event=$event, fileName=$fileName")
            fileAdd(file)
        } else if (
            (event and MODIFY) != 0 ||
            (event and ATTRIB) != 0 ||
            (event and CLOSE_WRITE) != 0 ||
            (event and CLOSE_NOWRITE) != 0
        ) {
            if (!file.exists()) {
                fileRemove(file)
                return
            }

            if (!CsFileUtils.isFile(file)) {
                return
            }
            CsLogger.tag(TAG).d("文件内容或属性变化 event=$event, fileName=$fileName")
            callback.onFileChanged(file.absolutePath)
        } else if ((event and DELETE) != 0 || (event and DELETE_SELF) != 0) {
            CsLogger.tag(TAG)
                .d("文件或目录被删除 rootPath=$rootPath, filePath=${file.absolutePath}")
            fileRemove(file)
        } else if ((event and MOVED_FROM) != 0) {
            CsLogger.tag(TAG).d("文件或目录被移出监视的目录 event=$event, fileName=$fileName")
            fileRemove(file)
        } else if ((event and MOVED_TO) != 0) {
            CsLogger.tag(TAG).d("文件或目录被移入监视的目录 event=$event, fileName=$fileName")
            fileAdd(file)
        }
    }


    private fun fileAdd(file: File) {
        callback.onFileAdded(file.absolutePath)
        if (CsFileUtils.isDir(file)) {
            var dirPath = file.absolutePath
            var observer = FileObserverImpl(dirPath, fileMapping, callback)
            observerMapping.put(dirPath, observer)
            observer.startWatching()

            file.listFiles(object : FilenameFilter {
                override fun accept(dir: File?, name: String?): Boolean {
                    return CsFileUtils.isDir(File(dir, name ?: ""))
                }
            })?.forEach {
                dirPath = it.absolutePath
                observer = FileObserverImpl(dirPath, fileMapping, callback)
                observerMapping.put(dirPath, observer)
                observer.startWatching()
            }
        }
    }

    private fun fileRemove(file: File) {
        callback.onFileRemove(file.absolutePath)
        HashMap(observerMapping).keys.forEach {
            if (it.startsWith(file.absolutePath)) {
                observerMapping.remove(it)?.stopWatching()
            }
        }
    }

}