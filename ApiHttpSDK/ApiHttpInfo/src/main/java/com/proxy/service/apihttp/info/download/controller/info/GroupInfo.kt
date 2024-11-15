package com.proxy.service.apihttp.info.download.controller.info

import com.proxy.service.apihttp.base.download.enums.StatusEnum
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.apihttp.info.download.controller.info.base.BaseGroup
import com.proxy.service.apihttp.info.download.db.DownloadRoom
import com.proxy.service.apihttp.info.download.manager.CallbackManager
import com.proxy.service.apihttp.info.download.utils.FileUtils
import com.proxy.service.apihttp.info.download.utils.TaskUtils
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.file.CsFileUtils
import com.proxy.service.core.service.task.CsTask
import com.proxy.service.threadpool.base.thread.task.ICallable
import java.util.Collections


/**
 * @author: cangHX
 * @data: 2024/11/1 10:12
 * @desc:
 */
open class GroupInfo(val name: String, val priority: Int, val fileDir: String? = null) :
    BaseGroup() {

    /**
     * 添加任务
     * */
    fun addTask(task: DownloadTask) {
        tasks.add(task)
        Collections.sort(tasks, this)

        val oldTask = DownloadRoom.INSTANCE.getTaskDao().query(task.getTaskTag())?.getDownloadTask()
        if (oldTask != null) {
            if (!TaskUtils.checkIsSame(task, oldTask)) {
                if (task.getFilePath() != oldTask.getFilePath()) {
                    CsFileUtils.delete(oldTask.getFilePath())
                    CsFileUtils.delete(FileUtils.getTempPath(oldTask.getFilePath()))
                }
                DownloadRoom.INSTANCE.getTaskDao().updateDownloadTask(task)
                CsLogger.tag(tag)
                    .d("该任务与上次记录信息不一致, 已覆盖上次信息")
            }
        } else {
            DownloadRoom.INSTANCE.getTaskDao().updateDownloadTask(task)
            CsLogger.tag(tag)
                .d("该任务为首次添加, 任务信息记录完成")
        }

        CsLogger.tag(tag).d("任务添加完成. taskTag = ${task.getTaskTag()}")
        callbackWaiting(task)
    }

    /**
     * 移除任务
     * */
    fun removeTask(task: DownloadTask) {
        tasks.removeAll { it.getTaskTag() == task.getTaskTag() }
    }

    /**
     * 基于组信息, 检查是否需要补全任务信息
     * */
    fun checkParams(task: DownloadTask) {
        CsLogger.tag(tag).d("开始检查配置信息. taskTag = ${task.getTaskTag()}")
        if (task.getGroupName().trim().isEmpty()) {
            CsLogger.tag(tag).d("组名称为空, 开始配置默认组. taskTag = ${task.getTaskTag()}")
            task.builder().setGroupName(name)
            CsLogger.tag(tag)
                .d("taskTag = ${task.getTaskTag()}, 配置组名称为: groupName = ${task.getGroupName()}")
        }

        if (task.getFilePath().trim().isEmpty()) {
            CsLogger.tag(tag).d("下载路径为空, 开始配置默认路径. taskTag = ${task.getTaskTag()}")
            if (task.getFileDir().trim().isNotEmpty()) {
                task.builder().setFileName(FileUtils.getDefaultFileName())
            } else if (task.getFileName().trim().isNotEmpty()) {
                if (fileDir?.trim()?.isNotEmpty() == true) {
                    task.builder().setFileDir(fileDir)
                } else {
                    task.builder().setFileDir(FileUtils.getDefaultFileDir())
                }
            }
            CsLogger.tag(tag)
                .d("taskTag = ${task.getTaskTag()}, 配置下载路径为: path = ${task.getFilePath()}")
        }
        CsLogger.tag(tag).d("配置信息检查完成. task = $task")
    }

    /**
     * 下载任务已添加, 当前处于等待执行阶段
     * */
    private fun callbackWaiting(task: DownloadTask) {
        DownloadRoom.INSTANCE.getTaskDao().updateStatus(
            task.getTaskTag(),
            StatusEnum.WAITING.status
        )
        CsTask.mainThread()?.call(object : ICallable<String> {
            override fun accept(): String {
                CallbackManager.getDownloadCallbacks(task.getTaskTag()).forEach {
                    it.onWaiting(task)
                }
                return ""
            }
        })?.start()
    }

}