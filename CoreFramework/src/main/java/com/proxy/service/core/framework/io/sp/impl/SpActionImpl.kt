package com.proxy.service.core.framework.io.sp.impl

import android.os.Parcelable
import com.proxy.service.core.framework.data.log.CsLogger
import com.proxy.service.core.framework.io.sp.SpInit
import com.proxy.service.core.framework.io.sp.SpMode
import com.proxy.service.core.framework.io.sp.ISpAction
import com.proxy.service.core.framework.io.sp.ISpController

/**
 * @author: cangHX
 * @date: 2026/5/14 10:56
 * @desc:
 */
abstract class SpActionImpl : ISpAction {

    protected open fun getSp(): ISpController {
        return SpInit.getSp(null, SpMode.SINGLE_PROCESS_MODE, null)
    }

    override fun sync() {
        getSp().sync()
    }

    override fun async() {
        getSp().async()
    }

    override fun clearMemoryCache() {
        getSp().clearMemoryCache()
    }

    override fun clearAllCache() {
        getSp().clearAllCache()
    }

    override fun close() {
        getSp().close()
    }

    override fun removeValueForKey(key: String) {
        getSp().removeValueForKey(key)
    }

    override fun removeValuesForKeys(keys: Array<String>) {
        getSp().removeValuesForKeys(keys)
    }

    override fun trim() {
        getSp().trim()
    }

    override fun allKeys(): Array<String> {
        return getSp().allKeys()
    }

    override fun getRootDir(): String {
        val dir = SpInit.getRootPath()
        if (dir.isEmpty()) {
            CsLogger.tag(SpInit.TAG).e("ScCore is not init.")
        }
        return dir
    }

    override fun getController(): ISpController {
        return getSp()
    }

    override fun put(key: String, value: Int): Boolean {
        return getSp().put(key, value)
    }

    override fun put(key: String, value: Long): Boolean {
        return getSp().put(key, value)
    }

    override fun put(key: String, value: Float): Boolean {
        return getSp().put(key, value)
    }

    override fun put(key: String, value: Double): Boolean {
        return getSp().put(key, value)
    }

    override fun put(key: String, value: Boolean): Boolean {
        return getSp().put(key, value)
    }

    override fun put(key: String, value: String?): Boolean {
        return getSp().put(key, value)
    }

    override fun put(key: String, value: ByteArray?): Boolean {
        return getSp().put(key, value)
    }

    override fun put(key: String, value: Parcelable?): Boolean {
        return getSp().put(key, value)
    }

    override fun put(key: String, value: Set<String>?): Boolean {
        return getSp().put(key, value)
    }

    override fun getInt(key: String, defaultValue: Int): Int {
        return getSp().getInt(key, defaultValue)
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return getSp().getLong(key, defaultValue)
    }

    override fun getFloat(key: String, defaultValue: Float): Float {
        return getSp().getFloat(key, defaultValue)
    }

    override fun getDouble(key: String, defaultValue: Double): Double {
        return getSp().getDouble(key, defaultValue)
    }

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return getSp().getBoolean(key, defaultValue)
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return getSp().getString(key, defaultValue)
    }

    override fun getByteArray(key: String, defaultValue: ByteArray?): ByteArray? {
        return getSp().getByteArray(key, defaultValue)
    }

    override fun <T : Parcelable> getParcelable(
        key: String,
        tClass: Class<T>,
        defaultValue: T?
    ): T? {
        return getSp().getParcelable(key, tClass, defaultValue)
    }

    override fun getStringSet(key: String, defaultValue: Set<String>?): Set<String>? {
        return getSp().getStringSet(key, defaultValue)
    }

}