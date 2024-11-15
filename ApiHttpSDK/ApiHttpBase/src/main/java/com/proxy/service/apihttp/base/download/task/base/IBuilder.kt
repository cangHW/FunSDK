package com.proxy.service.apihttp.base.download.task.base

import com.proxy.service.apihttp.base.download.task.DownloadTask

/**
 * @author: cangHX
 * @data: 2024/5/21 20:43
 * @desc:
 */
interface IBuilder {

    /**
     * 设置组名称, 如果对应组不存在，则自动转为默认组
     * */
    fun setGroupName(groupName: String): IBuilder

    /**
     * 设置任务唯一标签, 如为空则默认生成, 默认生成规则为下载 url 的 md5
     * */
    fun setTaskTag(tag: String): IBuilder

    /**
     * 设置任务优先级, 与组优先级联动, 先计算组优先级, 组内计算任务优先级
     * */
    fun setPriority(priority: Int): IBuilder

    /**
     * 设置下载文件全路径, 例如：/sdcard/Android/data/<PackageName>/files/xxx.txt,
     * 最终路径基于 [setFilePath]、[setFileDir]、[setFileName] 与组信息计算得出
     * */
    fun setFilePath(filePath: String): IBuilder

    /**
     * 设置下载文件文件夹路径, 例如：/sdcard/Android/data/<PackageName>/files/,
     * 最终文件夹路径基于 [setFilePath]、[setFileDir] 与组信息计算得出
     * */
    fun setFileDir(fileDir: String): IBuilder

    /**
     * 设置下载文件名称, 例如：xxx.txt,
     * 最终名称基于 [setFilePath]、[setFileName] 与默认值计算得出
     * */
    fun setFileName(fileName: String): IBuilder

    /**
     * 设置下载文件 md5 信息, 用于校验文件完整性
     * */
    fun setFileMd5(fileMd5: String): IBuilder

    /**
     * 设置下载文件总大小, 用于校验文件完整性
     * */
    fun setFileSize(fileSize: Long): IBuilder

    /**
     * 是否开启多线程分片下载, 默认关闭, 开启后如果条件不满足会自动回退到普通下载.
     * <br/>
     * @desc    作用:
     * 1. 加速下载, 原理为使用多线程去占用更多的网络份额, 如果当前只有一个任务使用网络, 则失去加速意义.<br/>
     * 2. 优化下载, 如果当前网络不稳定, 依靠多线程能尽最大可能下载更多的内容.
     * */
    fun setMultiPartEnable(enable: Boolean): IBuilder

    /**
     * 创建配置
     * */
    fun build(): DownloadTask

}