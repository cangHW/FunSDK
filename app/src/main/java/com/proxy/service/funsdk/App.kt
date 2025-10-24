package com.proxy.service.funsdk

import android.app.Application
import com.proxy.service.core.CsCore

/**
 * @author: cangHX
 * @data: 2024/12/19 11:12
 * @desc:
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        CsCore.init(this, true)
    }

}