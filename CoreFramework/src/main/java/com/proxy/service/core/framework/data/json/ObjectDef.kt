package com.proxy.service.core.framework.data.json

import kotlin.reflect.KClass

/**
 * @author: cangHX
 * @data: 2025/11/5 20:36
 * @desc:
 */
@MustBeDocumented
@Retention(value = AnnotationRetention.SOURCE)
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class ObjectDef(
    val value: Array<KClass<*>>
)
