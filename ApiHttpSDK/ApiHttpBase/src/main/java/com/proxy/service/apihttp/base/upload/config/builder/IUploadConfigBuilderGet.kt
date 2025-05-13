package com.proxy.service.apihttp.base.upload.config.builder

import com.proxy.service.apihttp.base.common.config.base.IBaseConfigGet

/**
 * @author: cangHX
 * @data: 2024/12/17 15:20
 * @desc:
 */
interface IUploadConfigBuilderGet : IBaseConfigGet {

    /**
     * 获取连接超时时间
     * */
    fun getConnectTimeOut(): Long

    /**
     * 获取最大并发请求数
     * */
    fun getMaxTask(): Int

}