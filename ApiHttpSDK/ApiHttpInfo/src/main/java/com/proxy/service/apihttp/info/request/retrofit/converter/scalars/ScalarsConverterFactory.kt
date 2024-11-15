package com.proxy.service.apihttp.info.request.retrofit.converter.scalars

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * @author: cangHX
 * @data: 2024/6/19 11:18
 * @desc:
 */
class ScalarsConverterFactory private constructor() : Converter.Factory() {

    companion object {
        fun create(): ScalarsConverterFactory {
            return ScalarsConverterFactory()
        }
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        return if (
            type === String::class.java
            || type === Boolean::class.javaPrimitiveType || type === Boolean::class.java
            || type === Byte::class.javaPrimitiveType || type === Byte::class.java
            || type === Char::class.javaPrimitiveType || type === Char::class.java
            || type === Double::class.javaPrimitiveType || type === Double::class.java
            || type === Float::class.javaPrimitiveType || type === Float::class.java
            || type === Int::class.javaPrimitiveType || type === Int::class.java
            || type === Long::class.javaPrimitiveType || type === Long::class.java
            || type === Short::class.javaPrimitiveType || type === Short::class.java
        ) {
            ScalarRequestBodyConverter.instance
        } else null
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        if (type === String::class.java) {
            return ScalarResponseBodyConverters.stringResponseBodyConverter
        }
        if (type === Boolean::class.java || type === Boolean::class.javaPrimitiveType) {
            return ScalarResponseBodyConverters.booleanResponseBodyConverter
        }
        if (type === Byte::class.java || type === Byte::class.javaPrimitiveType) {
            return ScalarResponseBodyConverters.byteResponseBodyConverter
        }
        if (type === Char::class.java || type === Char::class.javaPrimitiveType) {
            return ScalarResponseBodyConverters.charResponseBodyConverter
        }
        if (type === Double::class.java || type === Double::class.javaPrimitiveType) {
            return ScalarResponseBodyConverters.doubleResponseBodyConverter
        }
        if (type === Float::class.java || type === Float::class.javaPrimitiveType) {
            return ScalarResponseBodyConverters.floatResponseBodyConverter
        }
        if (type === Int::class.java || type === Int::class.javaPrimitiveType) {
            return ScalarResponseBodyConverters.intResponseBodyConverter
        }
        if (type === Long::class.java || type === Long::class.javaPrimitiveType) {
            return ScalarResponseBodyConverters.longResponseBodyConverter
        }
        return if (type === Short::class.java || type === Short::class.javaPrimitiveType) {
            ScalarResponseBodyConverters.shortResponseBodyConverter
        } else null
    }

}