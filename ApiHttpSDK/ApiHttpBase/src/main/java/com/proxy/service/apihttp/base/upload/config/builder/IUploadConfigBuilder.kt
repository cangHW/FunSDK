package com.proxy.service.apihttp.base.upload.config.builder

import com.proxy.service.apihttp.base.common.config.base.IBaseConfig
import com.proxy.service.apihttp.base.upload.config.UploadConfig
import java.util.concurrent.TimeUnit

/**
 * @author: cangHX
 * @data: 2024/12/17 15:20
 * @desc:
 */
interface IUploadConfigBuilder : IBaseConfig<IUploadConfigBuilder> {

    /**
     * 设置连接超时时间
     * */
    fun setConnectTimeOut(timeout: Long, unit: TimeUnit): IUploadConfigBuilder

    /**
     * 设置最大并发请求数
     * */
    fun setMaxTask(maxTasks: Int): IUploadConfigBuilder

    /**
     * 创建配置
     * */
    fun build(): UploadConfig

}