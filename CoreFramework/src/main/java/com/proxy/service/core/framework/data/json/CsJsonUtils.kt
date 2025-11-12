package com.proxy.service.core.framework.data.json

import com.google.gson.Gson
import com.google.gson.JsonDeserializer
import com.google.gson.JsonSerializer
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import org.json.JSONArray
import org.json.JSONObject
import java.io.Reader
import java.io.Writer
import java.lang.reflect.Type


/**
 * json 序列化、反序列化工具
 *
 * @author: cangHX
 * @data: 2024/6/24 09:58
 * @desc:
 */
object CsJsonUtils {

    private const val TAG = "${CoreConfig.TAG}Json"

    private var gson: Gson = Gson()

    /**
     * 设置序列化与反序列化规则, 特定类型（精确匹配）, 处理具体类型（非泛型、无继承）
     *
     * @param type      适配器适配的类型
     * @param adapter   适配器
     * */
    fun registerTypeAdapter(
        type: Type,
        @ObjectDef(value = [JsonSerializer::class, JsonDeserializer::class, TypeAdapter::class]) adapter: Any
    ) {
        val isJs = adapter is JsonSerializer<*>
        val isJd = adapter is JsonDeserializer<*>
        val isTa = adapter is TypeAdapter<*>

        if (!isJs && !isJd && !isTa) {
            return
        }

        gson = gson.newBuilder()
            .registerTypeAdapter(type, adapter)
            .create()
    }

    /**
     * 设置序列化与反序列化规则, 动态判断任意类型（含泛型）, 泛型类型、需要运行时条件判断
     *
     * @param factory   适配器工厂
     * */
    fun registerTypeAdapterFactory(factory: TypeAdapterFactory) {
        gson = gson.newBuilder()
            .registerTypeAdapterFactory(factory)
            .create()
    }

    /**
     * 设置序列化与反序列化规则, 类型及其子类（继承链）, 统一处理继承体系
     *
     * @param baseType      适配器适配的基类类型
     * @param typeAdapter   适配器
     * */
    fun registerTypeHierarchyAdapter(
        baseType: Class<*>,
        @ObjectDef(value = [JsonSerializer::class, JsonDeserializer::class, TypeAdapter::class]) typeAdapter: Any
    ) {
        val isJs = typeAdapter is JsonSerializer<*>
        val isJd = typeAdapter is JsonDeserializer<*>
        val isTa = typeAdapter is TypeAdapter<*>

        if (!isJs && !isJd && !isTa) {
            return
        }

        gson = gson.newBuilder()
            .registerTypeHierarchyAdapter(baseType, typeAdapter)
            .create()
    }

    /**
     * 解析数据
     *
     * @param type 示例: object : TypeToken<MutableList<String>>() {}.type
     * */
    fun <T> fromJson(json: String?, type: Type): T? {
        try {
            if (TypeUtils.getRawType(type) == String::class.java) {
                @Suppress("UNCHECKED_CAST")
                return json as? T?
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }

        if (json.isNullOrEmpty()) {
            return null
        }

        try {
            return gson.fromJson(json, type)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
    }

    /**
     * 解析数据
     * */
    fun <T> fromJson(json: String?, tClass: Class<T>): T? {
        try {
            if (tClass == String::class.java) {
                @Suppress("UNCHECKED_CAST")
                return json as? T?
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }

        if (json.isNullOrEmpty()) {
            return null
        }

        try {
            return gson.fromJson(json, tClass)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
    }

    /**
     * 解析数据
     * */
    fun fromJsonToList(json: String?): MutableList<String>? {
        if (json.isNullOrEmpty()) {
            return null
        }
        try {
            val array = JSONArray(json)
            val list = ArrayList<String>()
            for (index in 0 until array.length()) {
                list.add(array.getString(index))
            }
            return list
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
            return null
        }
    }

    /**
     * 解析数据
     * */
    fun <T> fromJsonToList(json: String?, tClass: Class<T>): MutableList<T>? {
        val listTemp = fromJsonToList(json) ?: return null

        try {
            if (tClass == String::class.java) {
                @Suppress("UNCHECKED_CAST")
                return listTemp as? MutableList<T>?
            }

            val list: MutableList<T> = ArrayList(listTemp.size)
            for (str in listTemp) {
                val value = fromJson(str, tClass) ?: continue
                list.add(value)
            }
            return list
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }

        return null
    }

    /**
     * 解析数据
     * */
    fun fromJsonToMap(json: String?): MutableMap<String, String>? {
        if (json.isNullOrEmpty()) {
            return null
        }

        try {
            val obj = JSONObject(json)
            val map = HashMap<String, String>()
            obj.keys().forEach {
                map.put(it, obj.getString(it))
            }
            return map
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }

        return null
    }

    /**
     * 解析数据
     * */
    fun <V> fromJsonToMap(json: String?, vClass: Class<V>): MutableMap<String, V>? {
        return fromJsonToMap(json, String::class.java, vClass)
    }

    /**
     * 解析数据
     * */
    fun <K, V> fromJsonToMap(json: String?, kClass: Class<K>, vClass: Class<V>): MutableMap<K, V>? {
        val mapTemp: MutableMap<String, String> = fromJsonToMap(json) ?: return null

        try {
            if (kClass == String::class.java && vClass == String::class.java) {
                @Suppress("UNCHECKED_CAST")
                return mapTemp as? MutableMap<K, V>?
            }

            val map: MutableMap<K, V> = HashMap(mapTemp.size, 1.0f)
            for (entry in mapTemp.entries) {
                val key = fromJson(entry.key, kClass) ?: continue
                val value = fromJson(entry.value, vClass) ?: continue
                map.put(key, value)
            }
            return map
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }

        return null
    }

    /**
     * 转为 JsonString
     * */
    fun toJson(src: Any?): String? {
        if (src == null) {
            return null
        }
        try {
            return gson.toJson(src)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
    }

    /**
     * 转为 JsonString
     * */
    fun toJson(src: Any?, type: Type): String? {
        if (src == null) {
            return null
        }
        try {
            return gson.toJson(src, type)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
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