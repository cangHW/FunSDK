package com.proxy.service.apihttp.base.request.annotation

/**
 * 网络框架，动态替换 baseurl
 *
 * @author: cangHX
 * @data: 2024/5/21 17:07
 * @desc:
 */
@MustBeDocumented
@Target(
    AnnotationTarget.FUNCTION,
    AnnotationTarget.PROPERTY_GETTER,
    AnnotationTarget.PROPERTY_SETTER
)
@Retention(
    AnnotationRetention.RUNTIME
)
annotation class CsBaseUrl(
    /**
     * baseurl
     * */
    val baseUrl: String
)


