package com.proxy.service.apihttp.info.download.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.proxy.service.apihttp.base.download.enums.StatusEnum
import com.proxy.service.apihttp.base.download.task.DownloadTask
import com.proxy.service.apihttp.info.download.db.entity.TaskBean

/**
 * @author: cangHX
 * @data: 2024/11/4 20:09
 * @desc:
 */
@Dao
interface TaskDao {

    /**
     * 查询一条数据
     * */
    @Query("SELECT * FROM TaskBean WHERE TaskBean.TaskTag == :taskTag")
    fun query(taskTag: String): TaskBean?

    /**
     * 查询状态码不大于目标值的任务
     * */
    @Query("SELECT * FROM TaskBean WHERE TaskBean.status <= :status")
    fun queryTasksByStatusUpTo(status: Int): List<TaskBean>

    /**
     * 查询状态码等于目标值的任务
     * */
    @Query("SELECT * FROM TaskBean WHERE TaskBean.status == :status")
    fun queryTasksByStatus(status: Int): List<TaskBean>

    /**
     * 查询下载状态
     * */
    @Query("SELECT TaskBean.Status FROM TaskBean WHERE TaskBean.TaskTag == :taskTag")
    fun queryStatus(taskTag: String): Int

    @Deprecated("请使用 updateDownloadTask")
    @Update
    fun update(bean: TaskBean)

    /**
     * 更新下载状态
     * */
    @Query("UPDATE TaskBean SET Status = :status WHERE TaskTag = :taskTag")
    fun updateStatus(taskTag: String, status: Int)

    @Deprecated("请使用 updateDownloadTask")
    @Insert()
    fun insert(bean: TaskBean)

    /**
     * 删除下载信息
     * */
    @Query("DELETE FROM TaskBean WHERE TaskBean.TaskTag == :taskTag")
    fun delete(taskTag: String)

    /**
     * 更新或插入一条数据
     * */
    @Transaction
    fun updateDownloadTask(task: DownloadTask) {
        val bean = TaskBean.create(task)
        query(task.getTaskTag())?.let {
            bean.status = it.status
            update(bean)
        } ?: let {
            bean.status = StatusEnum.RECORDED.status
            insert(bean)
        }
    }

}