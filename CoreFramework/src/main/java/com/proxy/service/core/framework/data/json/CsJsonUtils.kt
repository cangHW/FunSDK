package com.proxy.service.core.framework.data.json

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import com.proxy.service.core.constants.Constants
import com.proxy.service.core.framework.data.log.CsLogger
import java.io.Reader
import java.io.Writer
import java.lang.reflect.Type

/**
 * @author: cangHX
 * @data: 2024/6/24 09:58
 * @desc:
 */
object CsJsonUtils {

    private const val TAG = "${Constants.TAG}Json"

    private var gson: Gson = Gson()

    /**
     * 解析数据
     * */
    fun <T> fromJson(json: String?, type: Type): T? {
        if (json.isNullOrEmpty()) {
            return null
        }
        return try {
            gson.fromJson(json, type)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            null
        }
    }

    /**
     * 解析数据
     * */
    fun <T> fromJson(json: String?, type: Class<T>): T? {
        if (json.isNullOrEmpty()) {
            return null
        }
        return try {
            gson.fromJson(json, type)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            null
        }
    }

    /**
     * 转为 JsonString
     * */
    fun toJson(src: Any?): String? {
        if (src == null) {
            return null
        }
        return try {
            gson.toJson(src)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            null
        }
    }

    /**
     * 转为 JsonString
     * */
    fun toJson(src: Any?, typeOfSrc: Type): String? {
        if (src == null) {
            return null
        }
        return try {
            gson.toJson(src, typeOfSrc)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            null
        }
    }

    /**
     * 读取数据并解析
     * */
    @Suppress("UNCHECKED_CAST")
    fun <T> readFromReader(reader: Reader?, type: Type): T? {
        if (reader == null) {
            return null
        }
        try {
            val adapter: TypeAdapter<T> = gson.getAdapter(TypeToken.get(type)) as TypeAdapter<T>
            val jsonReader = gson.newJsonReader(reader)
            val result = adapter.read(jsonReader)
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                CsLogger.tag(TAG).e("JSON document was not fully consumed.")
                return null
            }
            return result
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            return null
        }
    }

    /**
     * 写入数据
     * */
    @Suppress("UNCHECKED_CAST")
    fun <T> writeToWriter(value: T?, type: Type, writer: Writer) {
        if (value == null) {
            return
        }
        try {
            val adapter: TypeAdapter<T> = gson.getAdapter(TypeToken.get(type)) as TypeAdapter<T>
            val jsonWriter = gson.newJsonWriter(writer)
            adapter.write(jsonWriter, value)
            jsonWriter.close()
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
    }
}