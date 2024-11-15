package com.proxy.service.apihttp.base.download.task

import com.proxy.service.apihttp.base.constants.Constants
import com.proxy.service.apihttp.base.download.task.base.IBuilder
import com.proxy.service.apihttp.base.download.task.base.IBuilderGet
import com.proxy.service.core.framework.convert.CsStorageUnit
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.system.security.CsMd5Utils
import java.io.File

/**
 * @author: cangHX
 * @data: 2024/10/30 18:34
 * @desc:
 */
class DownloadTask private constructor(private val builder: Builder) : IBuilderGet {

    override fun getGroupName(): String {
        return builder.getGroupName()
    }

    override fun getTaskTag(): String {
        return builder.getTaskTag()
    }

    override fun getPriority(): Int {
        return builder.getPriority()
    }

    override fun getDownloadUrl(): String {
        return builder.getDownloadUrl()
    }

    override fun getFilePath(): String {
        return builder.getFilePath()
    }

    override fun getFileDir(): String {
        return builder.getFileDir()
    }

    override fun getFileName(): String {
        return builder.getFileName()
    }

    override fun getFileMd5(): String {
        return builder.getFileMd5()
    }

    override fun getFileSize(): Long {
        return builder.getFileSize()
    }

    override fun getMultiPartEnable(): Boolean {
        return builder.getMultiPartEnable()
    }

    fun builder(): IBuilder {
        return builder
    }

    override fun toString(): String {
        return "Builder(groupName='${getGroupName()}', taskTag='${getTaskTag()}', priority=${getPriority()}, downloadUrl='${getDownloadUrl()}', filePath='${getFilePath()}', fileDir='${getFileDir()}', fileName='${getFileName()}', fileMd5='${getFileMd5()}', fileSize=${getFileSize()})"
    }

    override fun hashCode(): Int {
        return getTaskTag().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other is DownloadTask) {
            return getTaskTag() == other.getTaskTag()
        }
        return super.equals(other)
    }

    companion object {
        private const val TAG = "${Constants.LOG_DOWNLOAD_TAG_START}DownloadTask"

        fun builder(downloadUrl: String): IBuilder {
            return Builder(downloadUrl)
        }
    }

    private class Builder(downloadUrl: String) : IBuilder, IBuilderGet {

        private var groupName: String = ""

        private var taskTag: String = ""
        private var priority: Int = 0

        private var downloadUrl: String = ""

        private var filePath: String = ""
        private var fileDir: String = ""
        private var fileName: String = ""

        private var fileMd5: String = ""
        private var fileSize: Long = 0

        private var multiPartEnable: Boolean = false

        init {
            this.downloadUrl = downloadUrl
        }

        override fun setGroupName(groupName: String): IBuilder {
            if (groupName.isEmpty() || groupName.isBlank()) {
                CsLogger.tag(TAG).e("groupName cannot be empty or blank. groupName = $groupName")
            } else {
                this.groupName = groupName
            }
            return this
        }

        override fun setTaskTag(tag: String): IBuilder {
            if (tag.isEmpty() || tag.isBlank()) {
                CsLogger.tag(TAG).e("tag cannot be empty or blank. tag = $tag")
            } else {
                this.taskTag = tag
            }
            return this
        }

        override fun setPriority(priority: Int): IBuilder {
            this.priority = priority
            return this
        }

        override fun setFilePath(filePath: String): IBuilder {
            if (filePath.isEmpty() || filePath.isBlank()) {
                CsLogger.tag(TAG).e("filePath cannot be empty or blank. filePath = $filePath")
            } else if (filePath.endsWith(File.separator)) {
                CsLogger.tag(TAG).e("filePath is error. filePath = $filePath")
            } else {
                this.filePath = filePath
                File(filePath).let {
                    fileDir = it.parent ?: ""
                    fileName = it.name
                }
            }
            return this
        }

        override fun setFileDir(fileDir: String): IBuilder {
            if (fileDir.isEmpty() || fileDir.isBlank()) {
                CsLogger.tag(TAG).e("fileDir cannot be empty or blank. fileDir = $fileDir")
            } else {
                this.fileDir = fileDir
                if (fileName.isNotEmpty() && fileName.isNotBlank()) {
                    filePath = File(fileDir, fileName).absolutePath
                }
            }
            return this
        }

        override fun setFileName(fileName: String): IBuilder {
            if (fileName.isEmpty() || fileName.isBlank()) {
                CsLogger.tag(TAG).e("fileName cannot be empty or blank. fileName = $fileName")
            } else {
                this.fileName = fileName
                if (fileDir.isNotEmpty() && fileDir.isNotBlank()) {
                    filePath = File(fileDir, fileName).absolutePath
                }
            }
            return this
        }

        override fun setFileMd5(fileMd5: String): IBuilder {
            if (fileMd5.trim().isNotEmpty()) {
                this.fileMd5 = fileMd5
            }
            return this
        }

        override fun setFileSize(fileSize: Long): IBuilder {
            this.fileSize = fileSize
            return this
        }

        override fun setMultiPartEnable(enable: Boolean): IBuilder {
            this.multiPartEnable = enable
            return this
        }

        override fun build(): DownloadTask {
            if (taskTag.isEmpty()) {
                taskTag = CsMd5Utils.getMD5(downloadUrl)
            }
            if (multiPartEnable) {
                val minSizeForMultiPart =
                    Constants.Download.FILE_PART_SIZE * (Constants.Download.MIN_PART_NUM - 1)
                if (fileSize <= minSizeForMultiPart) {
                    CsLogger.tag(TAG).i(
                        "启动分片下载, 需要文件长度大于: ${
                            CsStorageUnit.B.toMbString(
                                minSizeForMultiPart,
                                2
                            )
                        }MB, 当前长度为: $fileSize, 不满足条件, 自动回退为普通下载"
                    )
                    multiPartEnable = false
                }
            }
            return DownloadTask(this)
        }

        override fun getGroupName(): String {
            return groupName
        }

        override fun getTaskTag(): String {
            return taskTag
        }

        override fun getPriority(): Int {
            return priority
        }

        override fun getDownloadUrl(): String {
            return downloadUrl
        }

        override fun getFilePath(): String {
            return filePath
        }

        override fun getFileDir(): String {
            return fileDir
        }

        override fun getFileName(): String {
            return fileName
        }

        override fun getFileMd5(): String {
            return fileMd5
        }

        override fun getFileSize(): Long {
            return fileSize
        }

        override fun getMultiPartEnable(): Boolean {
            return multiPartEnable
        }

    }

}