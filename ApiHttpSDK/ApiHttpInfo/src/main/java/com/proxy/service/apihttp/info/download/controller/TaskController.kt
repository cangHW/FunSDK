package com.proxy.service.apihttp.info.download.controller

import com.proxy.service.apihttp.base.constants.Constants
import com.proxy.service.apihttp.base.download.config.DownloadGroup
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.apihttp.info.download.controller.base.BaseController
import com.proxy.service.apihttp.info.download.controller.info.GroupInfo
import com.proxy.service.apihttp.info.download.db.DownloadRoom
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.service.task.CsTask

/**
 * @author: cangHX
 * @data: 2024/11/1 10:11
 * @desc:
 */
object TaskController : BaseController() {

    /**
     * 添加组信息
     * */
    fun addGroup(list: ArrayList<DownloadGroup>) {
        CsTask.launchTaskGroup(Constants.Download.TASK_LOOP_THREAD_NAME)?.start {
            list.forEach {
                if (groupMapper.containsKey(it.groupName)) {
                    return@forEach
                }
                addGroup(it)
            }
        }
    }

    /**
     * 添加任务
     * */
    fun addTask(task: DownloadTask) {
        CsTask.launchTaskGroup(Constants.Download.TASK_LOOP_THREAD_NAME)?.start {
            val group = getGroup(task.getGroupName())
            // 参数补全
            group.checkParams(task)
            // 检查是否允许添加任务
            if (checkIsTaskCanAdd(task)) {
                // 添加待下载
                taskWeakMap[task] = task.getTaskTag()
                group.addTask(task)
            } else {
                CsLogger.tag(tag).i("任务已存在, 无需重复添加. taskTag = ${task.getTaskTag()}")
            }
            tryStartTask()
        }
    }

    /**
     * 根据 taskTag 获取对应 task 任务
     * */
    fun getTask(taskTag: String): DownloadTask? {
        return DownloadRoom.INSTANCE.getTaskDao().query(taskTag)?.getDownloadTask()
    }

    /**
     * 取消任务
     * */
    fun cancelTask(taskTag: String) {
        CsTask.launchTaskGroup(Constants.Download.TASK_LOOP_THREAD_NAME)?.start {
            val task = getTask(taskTag)
            if (task == null) {
                CsLogger.tag(tag).i("准备取消的任务不存在 taskTag = $taskTag")
                return@start
            }
            CsLogger.tag(tag).i("取消任务 taskTag = $taskTag")
            cancel(task, true)
        }
    }

    /**
     * 取消组任务
     * */
    fun cancelTaskByGroup(groupName: String) {
        CsTask.launchTaskGroup(Constants.Download.TASK_LOOP_THREAD_NAME)?.start {
            val group = TaskController.getGroup(groupName)
            if (group.name != groupName) {
                CsLogger.tag(tag).i("准备取消的任务组不存在 groupName = $groupName")
                return@start
            }
            CsLogger.tag(tag)
                .i("取消任务组 groupName = $groupName, taskNum = ${group.getTaskNum()}")
            group.getAllTask().forEach {
                CsLogger.tag(tag).i("取消任务 taskTag = ${it.getTaskTag()}")
                cancel(it, true)
            }
        }
    }

    /**
     * 取消全部任务
     * */
    fun cancelAllTask() {
        CsTask.launchTaskGroup(Constants.Download.TASK_LOOP_THREAD_NAME)?.start {
            CsLogger.tag(tag).i("取消全部任务 taskNum = ${taskWeakMap.size}")
            taskWeakMap.iterator().forEach {
                CsLogger.tag(tag).i("取消任务 taskTag = ${it.key.getTaskTag()}")
                cancel(it.key, true)
            }
        }
    }
}