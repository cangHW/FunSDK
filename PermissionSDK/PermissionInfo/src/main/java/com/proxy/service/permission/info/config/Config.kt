package com.proxy.service.permission.info.config

import com.proxy.service.permission.base.manager.DialogFactory
import com.proxy.service.permission.info.dialog.DialogFactoryImpl
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author: cangHX
 * @data: 2024/11/18 11:47
 * @desc:
 */
object Config {

    val REQUEST_CODE: AtomicInteger = AtomicInteger(0)

    @Volatile
    var factory: DialogFactory = DialogFactoryImpl()

    fun setDialogFactory(factory: DialogFactory) {
        this.factory = factory
    }

}