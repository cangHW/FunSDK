package com.proxy.service.apihttp.base.request.annotation

import com.proxy.service.apihttp.base.request.RequestService

/**
 * 网络框架，动态替换 baseurl, 与 [RequestService.addGlobalBaseUrl] 搭配使用.
 * 如果与 [CsBaseUrl] 同时存在，则以 [CsBaseUrl] 为准
 *
 * @author: cangHX
 * @data: 2024/5/21 17:07
 * @desc:
 */
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class CsBaseUrlKey(
    /**
     * baseUrlKey
     * */
    val baseUrlKey: String
)


