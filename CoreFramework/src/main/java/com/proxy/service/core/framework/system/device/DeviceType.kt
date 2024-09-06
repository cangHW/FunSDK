package com.proxy.service.core.framework.system.device

import android.os.Build

/**
 * @author: cangHX
 * @data: 2024/7/25 17:27
 * @desc:
 */
enum class DeviceType {

    /**
     * Vivo
     * */
    Vivo {
        override fun check(): Boolean {
            return "ViVo".equals(Build.MANUFACTURER, ignoreCase = true)
        }
    },

    /**
     * Oppo
     * */
    Oppo {
        override fun check(): Boolean {
            return "oppo".equals(Build.MANUFACTURER, ignoreCase = true)
        }
    },

    /**
     * 一加
     * */
    OnePlus {
        override fun check(): Boolean {
            return "OnePlus".equals(Build.MANUFACTURER, ignoreCase = true)
        }
    },

    /**
     * realme
     * */
    Realme {
        override fun check(): Boolean {
            return "realme".equals(Build.MANUFACTURER, ignoreCase = true)
        }
    },

    /**
     * 魅族
     * */
    MeiZu {
        override fun check(): Boolean {
            return "meizu".equals(Build.MANUFACTURER, ignoreCase = true)
        }
    },

    /**
     * 小米
     * */
    Xiaomi {
        override fun check(): Boolean {
            return "Xiaomi".equals(Build.MANUFACTURER, ignoreCase = true)
        }
    },

    /**
     * 华为
     * */
    Huawei {
        override fun check(): Boolean {
            return "huawei".equals(Build.MANUFACTURER, ignoreCase = true)
        }
    },

    /**
     * 荣耀
     * */
    Honor {
        override fun check(): Boolean {
            return "honor".equals(Build.MANUFACTURER, ignoreCase = true);
        }
    },

    /**
     * 未知
     * */
    UnKnown {
        override fun check(): Boolean {
            return true
        }
    };

    abstract fun check(): Boolean
}