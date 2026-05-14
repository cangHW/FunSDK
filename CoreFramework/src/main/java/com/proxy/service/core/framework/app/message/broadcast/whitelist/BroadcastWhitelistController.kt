package com.proxy.service.core.framework.app.message.broadcast.whitelist

import com.proxy.service.core.framework.app.message.process.whitelist.AbstractWhitelistController

/**
 * @author: cangHX
 * @date: 2026/5/13 14:43
 * @desc:
 */
class BroadcastWhitelistController : AbstractWhitelistController() {

    companion object {

        private val _instance by lazy {
            BroadcastWhitelistController()
        }

        fun getInstance(): AbstractWhitelistController {
            return _instance
        }

    }

}
