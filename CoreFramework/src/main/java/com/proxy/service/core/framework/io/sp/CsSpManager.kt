package com.proxy.service.core.framework.io.sp

import android.os.Parcelable
import com.proxy.service.core.framework.data.log.CsLogger
import com.tencent.mmkv.MMKV

/**
 * 类sp，key-value，键值对存储相关工具
 *
 * @author: cangHX
 * @data: 2024/7/20 14:28
 * @desc:
 */
object CsSpManager : ISpAction {

    private val explicitName: ThreadLocal<String> = ThreadLocal()
    private val explicitMode: ThreadLocal<SpMode> = ThreadLocal()
    private val explicitSecurity: ThreadLocal<String> = ThreadLocal()

    override fun name(tag: String): ISpAction {
        explicitName.set(tag)
        return this
    }

    override fun mode(mode: SpMode): ISpAction {
        explicitMode.set(mode)
        return this
    }

    override fun secretKey(secretKey: String): ISpAction {
        explicitSecurity.set(secretKey)
        return this
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

    private fun getSp(): ISpController {
        val name = explicitName.get()
        if (name != null) {
            explicitName.remove()
        }

        var mode = explicitMode.get()
        if (mode != null) {
            explicitMode.remove()
        } else {
            mode = SpMode.SINGLE_PROCESS_MODE
        }

        val secretKey = explicitSecurity.get()
        if (secretKey != null) {
            explicitSecurity.remove()
        }

        return SpInit.getSp(name, mode, secretKey)
    }
}