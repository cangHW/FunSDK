package com.proxy.service.core.framework.io.sp

import android.os.Parcelable
import com.proxy.service.core.framework.data.log.CsLogger
import com.tencent.mmkv.MMKV

/**
 * @author: cangHX
 * @data: 2025/10/15 20:29
 * @desc:
 */
class SpControllerImpl(private val mmkv: MMKV) : ISpController {

    override fun sync() {
        mmkv.sync()
    }

    override fun async() {
        mmkv.async()
    }

    override fun clearMemoryCache() {
        mmkv.clearMemoryCache()
    }

    override fun clearAllCache() {
        mmkv.clearAll()
        SpInit.remove(this)
    }

    override fun close() {
        mmkv.close()
        SpInit.remove(this)
    }

    override fun removeValueForKey(key: String) {
        mmkv.removeValueForKey(key)
    }

    override fun removeValuesForKeys(keys: Array<String>) {
        mmkv.removeValuesForKeys(keys)
    }

    override fun trim() {
        mmkv.trim()
    }

    override fun allKeys(): Array<String> {
        try {
            val keys = mmkv.allKeys()
            if (keys != null) {
                return keys.filterNotNull().toTypedArray()
            }
        } catch (throwable: Throwable) {
            CsLogger.tag(SpInit.TAG).d(throwable)
        }
        return arrayOf()
    }

    override fun put(key: String, value: Int): Boolean {
        return mmkv.encode(key, value)
    }

    override fun put(key: String, value: Long): Boolean {
        return mmkv.encode(key, value)
    }

    override fun put(key: String, value: Float): Boolean {
        return mmkv.encode(key, value)
    }

    override fun put(key: String, value: Double): Boolean {
        return mmkv.encode(key, value)
    }

    override fun put(key: String, value: Boolean): Boolean {
        return mmkv.encode(key, value)
    }

    override fun put(key: String, value: String?): Boolean {
        return mmkv.encode(key, value)
    }

    override fun put(key: String, value: ByteArray?): Boolean {
        return mmkv.encode(key, value)
    }

    override fun put(key: String, value: Parcelable?): Boolean {
        return mmkv.encode(key, value)
    }

    override fun put(key: String, value: Set<String>?): Boolean {
        return mmkv.encode(key, value)
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return mmkv.decodeInt(key, defaultValue)
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return mmkv.decodeLong(key, defaultValue)
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return mmkv.decodeFloat(key, defaultValue)
    }

    override fun getDouble(key: String, defaultValue: Double): Double {
        return mmkv.decodeDouble(key, defaultValue)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return mmkv.decodeBool(key, defaultValue)
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return mmkv.decodeString(key, defaultValue)
    }

    override fun getByteArray(key: String, defaultValue: ByteArray?): ByteArray? {
        return mmkv.decodeBytes(key, defaultValue)
    }

    override fun <T : Parcelable> getParcelable(
        key: String,
        tClass: Class<T>,
        defaultValue: T?
    ): T? {
        return mmkv.decodeParcelable(key, tClass, defaultValue)
    }

    override fun getStringSet(key: String, defaultValue: Set<String>?): Set<String>? {
        return mmkv.decodeStringSet(key, defaultValue)
    }
}