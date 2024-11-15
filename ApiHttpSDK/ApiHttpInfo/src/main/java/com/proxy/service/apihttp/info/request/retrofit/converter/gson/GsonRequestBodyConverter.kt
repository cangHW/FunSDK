package com.proxy.service.apihttp.info.request.retrofit.converter.gson

import com.proxy.service.core.framework.data.json.CsJsonUtils
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import retrofit2.Converter
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer
import java.lang.reflect.Type
import java.nio.charset.StandardCharsets

/**
 * @author: cangHX
 * @data: 2024/6/22 18:00
 * @desc:
 */
internal class GsonRequestBodyConverter<T>(
    private val type:Type
) : Converter<T, RequestBody> {
    companion object {
        private val MEDIA_TYPE: MediaType = "application/json; charset=UTF-8".toMediaType()
        private val UTF_8 = StandardCharsets.UTF_8
    }

    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        val buffer = Buffer()
        val writer: Writer = OutputStreamWriter(buffer.outputStream(), UTF_8)
        CsJsonUtils.writeToWriter(value, type, writer)
        return buffer.readByteString().toRequestBody(MEDIA_TYPE)
    }
}