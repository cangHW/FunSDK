package com.proxy.service.apihttp.info.download.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.proxy.service.apihttp.base.download.task.DownloadTask

/**
 * @author: cangHX
 * @data: 2024/11/4 16:56
 * @desc:
 */
@Entity
class TaskBean {

    /**
     * 组名称
     * */
    @ColumnInfo(name = "GroupName")
    var groupName: String = ""

    /**
     * 任务标识
     * */
    @PrimaryKey
    @ColumnInfo(name = "TaskTag")
    var taskTag: String = ""

    /**
     * 任务标识优先级
     * */
    @ColumnInfo(name = "Priority")
    var priority: Int = 0

    /**
     * 任务下载链接
     * */
    @ColumnInfo(name = "DownloadUrl")
    var downloadUrl: String = ""

    /**
     * 下载文件全路径
     * */
    @ColumnInfo(name = "FilePath")
    var filePath: String = ""

    /**
     * 下载文件文件夹路径
     * */
    @ColumnInfo(name = "FileDir")
    var fileDir: String = ""

    /**
     * 下载文件名称
     * */
    @ColumnInfo(name = "FileName")
    var fileName: String = ""

    /**
     * 下载文件 md5 信息
     * */
    @ColumnInfo(name = "FileMd5")
    var fileMd5: String = ""

    /**
     * 下载文件总大小
     * */
    @ColumnInfo(name = "FileSize")
    var fileSize: Long = 0

    /**
     * 是否允许分片下载
     * */
    @ColumnInfo(name = "MultiPartEnable")
    var multiPartEnable: Boolean = false

    /**
     * 下载状态
     * */
    @ColumnInfo(name = "Status")
    var status: Int = 0


    /**
     * 获取下载任务信息对象
     * */
    fun getDownloadTask(): DownloadTask {
        return DownloadTask.builder(downloadUrl)
            .setGroupName(groupName)
            .setTaskTag(taskTag)
            .setPriority(priority)
            .setFilePath(filePath)
            .setFileDir(fileDir)
            .setFileName(fileName)
            .setFileMd5(fileMd5)
            .setFileSize(fileSize)
            .setMultiPartEnable(multiPartEnable)
            .build()
    }

    companion object {
        fun create(task: DownloadTask): TaskBean {
            val bean = TaskBean()
            bean.groupName = task.getGroupName()
            bean.taskTag = task.getTaskTag()
            bean.priority = task.getPriority()
            bean.downloadUrl = task.getDownloadUrl()
            bean.filePath = task.getFilePath()
            bean.fileDir = task.getFileDir()
            bean.fileName = task.getFileName()
            bean.fileMd5 = task.getFileMd5()
            bean.fileSize = task.getFileSize()
            bean.multiPartEnable = task.getMultiPartEnable()
            return bean
        }
    }

}