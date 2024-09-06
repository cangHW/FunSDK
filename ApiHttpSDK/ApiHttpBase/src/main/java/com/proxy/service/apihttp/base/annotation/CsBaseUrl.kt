package com.proxy.service.apihttp.base.annotation

/**
 * @author: cangHX
 * @data: 2024/5/21 17:07
 * @desc: 动态替换 baseurl
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


