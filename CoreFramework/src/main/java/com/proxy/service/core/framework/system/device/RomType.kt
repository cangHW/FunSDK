package com.proxy.service.core.framework.system.device

import android.os.Build
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.data.log.CsLogger
import java.lang.reflect.Method
import java.util.Properties


/**
 * @author: cangHX
 * @data: 2024/7/25 17:27
 * @desc:
 */
enum class RomType {

    /**
     * 小米
     * */
    Miui {
        private val keyMiuiVersionCode = "ro.miui.ui.version.code"
        private val keyMiuiVersionName = "ro.miui.ui.version.name"
        private val keyMiuiInternalStorage = "ro.miui.internal.storage"

        override fun check(properties: Properties): Boolean {
            try {
                return properties.getProperty(keyMiuiVersionCode) != null
                        || properties.getProperty(keyMiuiVersionName) != null
                        || properties.getProperty(keyMiuiInternalStorage) != null
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).d(throwable)
            }
            return false
        }
    },

    /**
     * 华为
     * */
    Emui {
        private val keyEmuiVersionCode: String = "ro.build.hw_emui_api_level"

        override fun check(properties: Properties): Boolean {
            try {
                return properties.getProperty(keyEmuiVersionCode, null) != null
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).d(throwable)
            }
            return false
        }
    },

    /**
     * 魅族
     * */
    FlyMe{
        override fun check(properties: Properties): Boolean {
            try {
                val method: Method? = Build::class.java.getMethod("hasSmartBar")
                return method !=null
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).d(throwable)
            }
            return false
        }
    },

    /**
     * 鸿蒙
     * */
    HarmonyOs {
        override fun check(properties: Properties): Boolean {
            try {
                val buildExClass = Class.forName("com.huawei.system.BuildEx")
                val osBrand = buildExClass.getMethod("getOsBrand").invoke(buildExClass) as? String?
                return "Harmony".equals(osBrand ?: "", ignoreCase = true)
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).d(throwable)
            }
            return false
        }
    },

    /**
     * 安卓
     * */
    Android {
        override fun check(properties: Properties): Boolean {
            return true
        }
    };

    protected val TAG = "${CoreConfig.TAG}Rom"

    abstract fun check(properties: Properties): Boolean
}