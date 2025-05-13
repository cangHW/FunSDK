package com.proxy.service.apihttp.info.download.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.proxy.service.apihttp.base.download.config.DownloadGroup
import com.proxy.service.apihttp.info.download.db.entity.GroupBean

/**
 * @author: cangHX
 * @data: 2024/11/4 20:09
 * @desc:
 */
@Dao
interface GroupDao {

    @Query("SELECT * FROM GroupBean WHERE GroupBean.GroupName == :groupName")
    fun query(groupName: String): GroupBean?

    @Update
    fun update(bean: GroupBean)

    @Insert()
    fun insert(bean: GroupBean)

    @Query("DELETE FROM GroupBean WHERE GroupBean.GroupName == :groupName")
    fun delete(groupName: String)

    /**
     * 查询数据
     * */
    fun queryDownloadGroup(groupName: String): DownloadGroup? {
        return query(groupName)?.let {
            DownloadGroup.builder(it.groupName)
                .setPriority(it.priority)
                .setDir(it.fileDir).build()
        }
    }

    /**
     * 更新或插入一条数据
     * */
    @Transaction
    fun updateDownloadGroup(task: DownloadGroup) {
        val bean = GroupBean()
        bean.groupName = task.groupName
        bean.priority = task.priority
        bean.fileDir = task.fileDir

        if (query(task.groupName) == null) {
            insert(bean)
        } else {
            update(bean)
        }
    }

}