package com.proxy.service.apihttp.info.download.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.proxy.service.apihttp.info.download.db.dao.GroupDao
import com.proxy.service.apihttp.info.download.db.entity.TaskBean
import com.proxy.service.apihttp.info.download.db.dao.TaskDao
import com.proxy.service.apihttp.info.download.db.entity.GroupBean
import com.proxy.service.core.framework.app.context.CsContextManager

/**
 * @author: cangHX
 * @data: 2024/11/4 16:46
 * @desc:
 */
@Database(entities = [TaskBean::class, GroupBean::class], version = 1, exportSchema = false)
abstract class DownloadRoom : RoomDatabase() {

    companion object {
        private const val DATABASE_NAME = "download.db"

        val INSTANCE: DownloadRoom by lazy {
            Room.databaseBuilder(
                CsContextManager.getApplication(),
                DownloadRoom::class.java, DATABASE_NAME
            ).fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .addMigrations(*DatabaseMigrations.migrations)
                .build()
        }
    }

    abstract fun getTaskDao(): TaskDao

    abstract fun getGroupDao(): GroupDao

}