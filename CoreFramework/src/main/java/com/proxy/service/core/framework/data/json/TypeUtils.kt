package com.proxy.service.core.framework.data.json

import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import java.lang.reflect.Array
import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import java.lang.reflect.WildcardType

/**
 * @author: cangHX
 * @data: 2025/11/5 18:10
 * @desc:
 */
object TypeUtils {

    private const val TAG = "${CoreConfig.TAG}TypeUtils"

    fun getRawType(type: Type?): Class<*>? {
        var result: Class<*>? = null
        try {
            when (type) {
                is Class<*> -> {
                    result = type
                }

                is ParameterizedType -> {
                    val rawType = type.rawType
                    result = rawType as Class<*>
                }

                is GenericArrayType -> {
                    val componentType = type.genericComponentType
                    result = getRawType(componentType)?.let { Array.newInstance(it, 0).javaClass }
                }

                is TypeVariable<*> -> {
                    result = Any::class.java
                }

                is WildcardType -> {
                    val bounds = type.upperBounds
                    result = getRawType(bounds[0])
                }

                else -> {
                    val className = if (type == null) "null" else type.javaClass.name
                    CsLogger.tag(TAG).e(
                        IllegalArgumentException(
                            "Expected a Class, ParameterizedType, or "
                                    + "GenericArrayType, but <" + type + "> is of type " + className
                        )
                    )
                }
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return result
    }

}