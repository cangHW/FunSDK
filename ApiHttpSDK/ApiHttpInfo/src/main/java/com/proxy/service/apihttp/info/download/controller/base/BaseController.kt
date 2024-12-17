package com.proxy.service.apihttp.info.download.controller.base

import android.util.SparseArray
import com.proxy.service.apihttp.base.constants.Constants
import com.proxy.service.apihttp.base.download.config.DownloadGroup
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.apihttp.info.config.Config
import com.proxy.service.apihttp.info.download.controller.info.GroupInfo
import com.proxy.service.apihttp.info.download.db.DownloadRoom
import com.proxy.service.apihttp.info.download.dispatcher.TaskDispatcher
import com.proxy.service.apihttp.info.download.dispatcher.base.BaseDispatcher
import com.proxy.service.apihttp.info.download.utils.FileUtils
import com.proxy.service.apihttp.info.download.utils.TaskUtils
import com.proxy.service.apihttp.info.download.utils.ThreadUtils
import com.proxy.service.core.framework.data.log.CsLogger
import java.util.WeakHashMap

/**
 * @author: cangHX
 * @data: 2024/11/5 20:28
 * @desc:
 */
abstract class BaseController {

    protected val tag = "${Constants.LOG_DOWNLOAD_TAG_START}TaskController"

    protected val groupMapper = HashMap<String, GroupInfo>()
    protected val groups = SparseArray<GroupInfo>()

    protected val taskWeakMap = WeakHashMap<DownloadTask, String>()

    private val onWorkerIdleCallback = object : BaseDispatcher.OnWorkerIdleCallback {
        override fun onWorkerIdle(task: DownloadTask?) {
            ThreadUtils.checkCurrentThread()
            taskWeakMap.remove(task)
            tryStartTask()
        }
    }

    init {
        TaskDispatcher.setOnWorkerIdleCallback(onWorkerIdleCallback)
    }

    /**
     * 添加组信息
     * */
    protected fun addGroup(group: DownloadGroup, shouldWriteToDb: Boolean = true): GroupInfo {
        ThreadUtils.checkCurrentThread()

        val groupInfo = GroupInfo(
            group.groupName,
            group.priority,
            group.fileDir
        )

        groups.put(groupInfo.priority, groupInfo)
        groupMapper[groupInfo.name] = groupInfo
        DownloadRoom.INSTANCE.getGroupDao().updateDownloadGroup(group)
        return groupInfo
    }

    protected fun tryStartTask() {
        if (TaskDispatcher.isTaskFull()) {
            return
        }
        ThreadUtils.checkCurrentThread()
        CsLogger.tag(tag).i("尝试执行执行一个高优先级下载任务")
        for (index in 0 until groups.size()) {
            val priority = groups.keyAt(index)
            val groupInfo = groups.get(priority)
            CsLogger.tag(tag)
                .i("检查任务组 GroupName = ${groupInfo.name}, GroupPriority = ${groupInfo.priority}, TaskNum = ${groupInfo.getTaskNum()}")

            val task = groupInfo.getFirstDownloadTask()
            if (task != null) {
                if (TaskDispatcher.startTask(task)) {
                    groupInfo.removeTask(task)
                    return
                }
            }
        }
        CsLogger.tag(tag).i("没有可执行任务")
    }

    /**
     * 检查任务是否允许添加，如果存在则校验关键参数是否一致，如果一致不做处理，如果不一致，则移除旧任务并添加新任务
     *
     * @return 是否允许添加
     * */
    protected fun checkIsTaskCanAdd(task: DownloadTask): Boolean {
        ThreadUtils.checkCurrentThread()
        val oldTask = taskWeakMap.filter {
            it.key.getTaskTag() == task.getTaskTag()
        }.keys.firstOrNull()

        if (oldTask == null) {
            return true
        }
        if (TaskUtils.checkIsSame(task, oldTask)) {
            return false
        }
        cancel(oldTask, false)
        return true
    }

    /**
     * 取消任务
     * */
    protected fun cancel(task: DownloadTask, isNeedCallback: Boolean) {
        ThreadUtils.checkCurrentThread()
        val group = getGroup(task.getGroupName())
        group.removeTask(task)
        TaskDispatcher.cancelRunningTask(task.getTaskTag(), isNeedCallback)
    }

    /**
     * 基于组名称获取对应组
     * */
    protected fun getGroup(groupName: String): GroupInfo {
        ThreadUtils.checkCurrentThread()
        if (groupName.trim().isEmpty()) {
            return getDefaultGroup()
        }

        val group = groupMapper[groupName]

        if (group != null) {
            return group
        }

        CsLogger.tag(tag).e("目标 Group 不存在，查询本地数据库. groupName = $groupName")
        val downloadGroup = DownloadRoom.INSTANCE.getGroupDao().queryDownloadGroup(groupName)

        if (downloadGroup != null) {
            return addGroup(downloadGroup, false)
        }

        CsLogger.tag(tag).e("目标 Group 不存在，使用默认 Group. groupName = $groupName")
        return getDefaultGroup()
    }

    /**
     * 获取默认组
     * */
    private fun getDefaultGroup(): GroupInfo {
        ThreadUtils.checkCurrentThread()
        val group = groupMapper[Config.GROUP_DEFAULT_NAME]
        if (group != null) {
            return group
        }
        return addGroup(
            DownloadGroup
                .builder(Config.GROUP_DEFAULT_NAME)
                .setPriority(0)
                .setDir(FileUtils.getDefaultFileDir())
                .build()
        )
    }

}