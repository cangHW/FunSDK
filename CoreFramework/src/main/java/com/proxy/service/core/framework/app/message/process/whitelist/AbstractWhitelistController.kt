package com.proxy.service.core.framework.app.message.process.whitelist

import android.content.Context
import com.proxy.service.core.framework.collections.CsExcellentList
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * @author: cangHX
 * @date: 2026/5/13 20:20
 * @desc:
 */
abstract class AbstractWhitelistController {

    private val factoryList = CsExcellentList<WhitelistFactory>()

    fun addWhitelistFactory(factory: WhitelistFactory) {
        factoryList.putSync(factory)
    }

    fun isAllowedSender(tag: String, context: Context, pkg: String): Boolean {
        // 同包名（同 App 多进程）直接放行
        if (pkg == context.packageName) {
            CsLogger.tag(tag).d("The same application.")
            return true
        }

        if (factoryList.size() == 0) {
            CsLogger.tag(tag).w("The whitelist policy has not yet been configured.")
            return true
        }
        for (factory in factoryList.getAll()) {
            if (factory.isAllowedSender(context, pkg)) {
                return true
            }
        }
        return false
    }

}