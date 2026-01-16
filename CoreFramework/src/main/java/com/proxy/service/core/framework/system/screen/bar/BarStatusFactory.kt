package com.proxy.service.core.framework.system.screen.bar

import com.proxy.service.core.framework.system.device.CsDeviceUtils
import com.proxy.service.core.framework.system.device.DeviceType
import com.proxy.service.core.framework.system.screen.CsBarUtils
import com.proxy.service.core.framework.system.screen.bar.impl.DefaultBarStatus
import com.proxy.service.core.framework.system.screen.bar.impl.MIUIStatusBarImpl
import com.proxy.service.core.framework.system.screen.bar.impl.MeizuStatusBarImpl

/**
 * @author: cangHX
 * @data: 2026/1/15 22:00
 * @desc:
 */
object BarStatusFactory {

    private var impl: DefaultBarStatus? = null

    fun getBarStatus(): DefaultBarStatus {
        if (impl != null) {
            return impl!!
        }

        impl = if (CsDeviceUtils.getDeviceType() == DeviceType.Xiaomi) {
            MIUIStatusBarImpl()
        } else if (CsDeviceUtils.getDeviceType() == DeviceType.MeiZu) {
            MeizuStatusBarImpl()
        } else {
            DefaultBarStatus()
        }
        return impl!!
    }
}