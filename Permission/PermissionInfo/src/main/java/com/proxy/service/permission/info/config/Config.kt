package com.proxy.service.permission.info.config

import com.proxy.service.permission.info.PermissionServiceImpl
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author: cangHX
 * @data: 2024/11/18 11:47
 * @desc:
 */
object Config {

    const val LOG_TAG_START = "Permission_"

    val SERVICE = PermissionServiceImpl()
    val REQUEST_CODE: AtomicInteger = AtomicInteger(0)
}