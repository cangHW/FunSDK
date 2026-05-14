package com.proxy.service.core.framework.app.message.provider.whitelist

import com.proxy.service.core.framework.app.message.process.whitelist.AbstractWhitelistController

/**
 * @author: cangHX
 * @date: 2026/5/13 14:43
 * @desc:
 */
class ProviderWhitelistController : AbstractWhitelistController() {

    companion object {

        private val _instance by lazy {
            ProviderWhitelistController()
        }

        fun getInstance(): AbstractWhitelistController {
            return _instance
        }

    }

}
