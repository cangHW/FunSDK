package com.proxy.service.core.framework.app.config.controller.store

import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.io.sp.CsSpManager
import com.proxy.service.core.framework.io.sp.SpMode

/**
 * @author: cangHX
 * @data: 2024/12/25 10:43
 * @desc:
 */
abstract class BaseStore<T> : ISerializable<T>, IStore<T> {

    companion object {
        const val TAG = "${CoreConfig.TAG}ConfigStore"

        private const val DEFAULT_VERSION = -1L

        private const val KEY_VERSION_FOLLOWING_SYSTEM = "following_system_version"
        private const val KEY_VERSION_VALUE = "value_version"
    }

    private val name = javaClass.simpleName
    private val diskStore = DiskStore(name, this)
    private val memoryStore = MemoryStore<T>()

    private var followingSystemVersion = DEFAULT_VERSION
    private var valueVersion = DEFAULT_VERSION

    override fun isFollowingSystem(): Boolean {
        val version = getVersionWithKey(KEY_VERSION_FOLLOWING_SYSTEM)
        if (version != followingSystemVersion) {
            followingSystemVersion = version
            diskStore.isFollowingSystem()?.let {
                memoryStore.setFollowSystemLocale(it)
            }
        }

        val isFollowingSystem = memoryStore.isFollowingSystem() ?: diskStore.isFollowingSystem()
        return isFollowingSystem ?: true
    }

    override fun setFollowSystemLocale(value: Boolean, isSave: Boolean) {
        memoryStore.setFollowSystemLocale(value)
        if (isSave) {
            diskStore.setFollowSystemLocale(value)
            updateVersionWidthKey(KEY_VERSION_FOLLOWING_SYSTEM)
        }
        followingSystemVersion = getVersionWithKey(KEY_VERSION_FOLLOWING_SYSTEM)
    }

    override fun getValue(): T? {
        val version = getVersionWithKey(KEY_VERSION_VALUE)
        if (version != valueVersion) {
            valueVersion = version
            diskStore.getValue()?.let {
                memoryStore.setSave(it)
            }
        }

        return memoryStore.getValue() ?: diskStore.getValue()
    }

    override fun setSave(value: T, isSave: Boolean) {
        memoryStore.setSave(value)
        if (isSave) {
            diskStore.setSave(value)
            updateVersionWidthKey(KEY_VERSION_VALUE)
        }
        valueVersion = getVersionWithKey(KEY_VERSION_VALUE)
    }

    private fun getVersionWithKey(key: String): Long {
        return CsSpManager
            .mode(SpMode.MULTI_PROCESS_MODE)
            .name(name)
            .getLong(key, DEFAULT_VERSION)
    }

    private fun updateVersionWidthKey(key: String){
        CsSpManager
            .mode(SpMode.MULTI_PROCESS_MODE)
            .name(name)
            .put(key, System.currentTimeMillis())
    }

    private class DiskStore<T>(
        private val name: String,
        private val serializable: ISerializable<T>
    ) : IStore<T> {

        companion object {
            private const val KEY_FOLLOWING_SYSTEM = "following_system"
            private const val VALUE_FOLLOWING_SYSTEM_ENABLE = 0
            private const val VALUE_FOLLOWING_SYSTEM_UNENABLE = 1
            private const val VALUE_FOLLOWING_SYSTEM_EMPTY = 2

            private const val KEY_VALUE = "value"
        }

        override fun isFollowingSystem(): Boolean? {
            val value = CsSpManager
                .mode(SpMode.MULTI_PROCESS_MODE)
                .name(name)
                .getInt(KEY_FOLLOWING_SYSTEM, VALUE_FOLLOWING_SYSTEM_EMPTY)

            if (value == VALUE_FOLLOWING_SYSTEM_ENABLE) {
                return true
            }
            if (value == VALUE_FOLLOWING_SYSTEM_UNENABLE) {
                return false
            }
            return null
        }

        override fun setFollowSystemLocale(value: Boolean, isSave: Boolean) {
            val isEnable = if (value) {
                VALUE_FOLLOWING_SYSTEM_ENABLE
            } else {
                VALUE_FOLLOWING_SYSTEM_UNENABLE
            }

            CsSpManager
                .mode(SpMode.MULTI_PROCESS_MODE)
                .name(name)
                .put(KEY_FOLLOWING_SYSTEM, isEnable)
        }

        override fun getValue(): T? {
            val str = CsSpManager
                .mode(SpMode.MULTI_PROCESS_MODE)
                .name(name)
                .getString(KEY_VALUE, null)
            if (str != null) {
                return serializable.loadFromString(str)
            }
            return null
        }

        override fun setSave(value: T, isSave: Boolean) {
            val str = serializable.saveToString(value)
            CsSpManager
                .mode(SpMode.MULTI_PROCESS_MODE)
                .name(name)
                .put(KEY_VALUE, str)
        }
    }

    private class MemoryStore<T> : IStore<T> {

        private var mIsFollowingSystem: Boolean? = null
        private var mValue: T? = null

        override fun isFollowingSystem(): Boolean? {
            return mIsFollowingSystem
        }

        override fun setFollowSystemLocale(value: Boolean, isSave: Boolean) {
            this.mIsFollowingSystem = value
        }

        override fun getValue(): T? {
            return mValue
        }

        override fun setSave(value: T, isSave: Boolean) {
            this.mValue = value
        }
    }
}