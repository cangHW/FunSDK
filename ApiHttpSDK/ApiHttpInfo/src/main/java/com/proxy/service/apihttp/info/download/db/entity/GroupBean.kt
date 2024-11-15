package com.proxy.service.apihttp.info.download.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @author: cangHX
 * @data: 2024/11/5 19:40
 * @desc:
 */
@Entity
class GroupBean {

    /**
     * 组名称
     * */
    @PrimaryKey
    @ColumnInfo(name = "GroupName")
    var groupName: String = ""

    /**
     * 优先级
     * */
    @ColumnInfo(name = "Priority")
    var priority: Int = 0

    /**
     * 默认路径
     * */
    @ColumnInfo(name = "FileDir")
    var fileDir: String = ""

}