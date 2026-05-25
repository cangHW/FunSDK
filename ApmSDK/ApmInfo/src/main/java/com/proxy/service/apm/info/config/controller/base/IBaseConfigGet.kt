package com.proxy.service.apm.info.config.controller.base

/**
 * @author: cangHX
 * @date: 2025/4/22 18:13
 * @desc:
 */
interface IBaseConfigGet : IConfigGet {

    /**
     * 最大文件数量
     * */
    fun getMaxFileCount(): Int

    /**
     * 最大占用存储
     * */
    fun getAllFilesMaxSize(): Long

    /**
     * 保存最长时间
     * */
    fun getMaxCacheTime(): Long

}