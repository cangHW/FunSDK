package com.proxy.service.apihttp.info.retrofit.converter.scalars

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Converter

/**
 * @author: cangHX
 * @data: 2024/6/19 11:20
 * @desc:
 */
class ScalarRequestBodyConverter<T> : Converter<T, RequestBody> {

    companion object {
        val instance by lazy { ScalarRequestBodyConverter<Any>() }
        private val MEDIA_TYPE: MediaType = "text/plain; charset=UTF-8".toMediaType()
    }

    override fun convert(value: T): RequestBody {
        return value.toString().toRequestBody(MEDIA_TYPE)
    }

}