package com.proxy.service.core.framework.file.sp

import android.os.Parcelable

/**
 * @author: cangHX
 * @data: 2024/7/20 14:59
 * @desc:
 */
interface ISpAction {

    /**
     * 指定存储库名称
     * */
    fun name(tag: String): ISpAction

    /**
     * 指定存储库模式, 默认单进程
     * */
    fun mode(mode: SpMode): ISpAction

    /**
     * 指定存储库加密密钥
     * */
    fun secretKey(secretKey: String): ISpAction

    /**
     * 获取存储库对应的本地文件夹路径
     * */
    fun getRootDir(): String

    /**
     * 将指定存储库尚未写入到磁盘的数据进行一次同步写入
     * */
    fun sync()

    /**
     * 将指定存储库尚未写入到磁盘的数据进行一次异步写入
     * */
    fun async()

    /**
     * 清除指定存储库在内存中的数据,
     * 注意：如果数据尚未写入磁盘将导致丢失, 确保清除之前已经保存了需要保存的数据
     * */
    fun clearMemoryCache()

    /**
     * 清除指定存储库的数据,
     * 注意：将清除指定存储库在内存以及磁盘的全部数据, 请确认是否真的需要清除
     * */
    fun clearAllCache()

    /**
     * 关闭指定存储库的实例,
     * 注意：1、如果数据尚未写入磁盘将导致丢失, 确保关闭之前已经保存了需要保存的数据.
     * 2、关闭存储库可以有效释放内存占用，但是频繁的开启关闭会带来额外的性能开销
     * */
    fun close()

    /**
     * 清除指定存储库中对应的数据
     * */
    fun removeValueForKey(key: String)

    /**
     * 清除指定存储库中对应的数据
     * */
    fun removeValuesForKeys(keys: Array<String>)

    /**
     * 整理和优化指定存储库的存储文件, 用于减少磁盘占用
     * */
    fun trim()

    fun put(key: String, value: Int): Boolean

    fun getInt(key: String, defaultValue: Int = 0): Int

    fun put(key: String, value: Long): Boolean

    fun getLong(key: String, defaultValue: Long = 0L): Long

    fun put(key: String, value: Float): Boolean

    fun getFloat(key: String, defaultValue: Float = 0.0f): Float

    fun put(key: String, value: Double): Boolean

    fun getDouble(key: String, defaultValue: Double = 0.0): Double

    fun put(key: String, value: Boolean): Boolean

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean

    fun put(key: String, value: String?): Boolean

    fun getString(key: String, defaultValue: String? = null): String?

    fun put(key: String, value: ByteArray?): Boolean

    fun getByteArray(key: String, defaultValue: ByteArray? = null): ByteArray?

    fun put(key: String, value: Parcelable?): Boolean

    fun <T : Parcelable> getParcelable(key: String, tClass: Class<T>, defaultValue: T? = null): T?

    fun put(key: String, value: Set<String>?): Boolean

    fun getStringSet(key: String, defaultValue: Set<String>? = null): Set<String>?
}