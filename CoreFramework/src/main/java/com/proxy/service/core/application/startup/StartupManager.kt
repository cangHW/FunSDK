package com.proxy.service.core.application.startup

import android.app.Application
import android.util.SparseArray
import com.proxy.service.core.application.base.CsBaseApplication

/**
 * @author: cangHX
 * @data: 2025/9/24 20:20
 * @desc:
 */
class StartupManager {

    companion object {
        private val _instance by lazy { StartupManager() }

        fun getInstance(): StartupManager {
            return _instance
        }
    }



    fun initializer(
        sparse: SparseArray<ArrayList<CsBaseApplication>>,
        application: Application,
        isDebug: Boolean
    ){

    }

}