package com.proxy.service.apihttp.info.request.retrofit.converter.gson

import com.proxy.service.core.framework.data.json.CsJsonUtils
import okhttp3.ResponseBody
import retrofit2.Converter
import java.io.IOException
import java.lang.IllegalArgumentException
import java.lang.reflect.Type

/**
 * @author: cangHX
 * @data: 2024/6/22 18:00
 * @desc:
 */
internal class GsonResponseBodyConverter<T>(
    private val type: Type
) : Converter<ResponseBody, T> {
    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T {
        val result: T?
        value.use {
            result = CsJsonUtils.readFromReader(it.charStream(), type)
        }
        if (result == null) {
            throw IllegalArgumentException("Data parsing exception.")
        }
        return result
    }
}