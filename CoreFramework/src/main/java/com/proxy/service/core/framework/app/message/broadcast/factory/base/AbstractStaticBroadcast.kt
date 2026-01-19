package com.proxy.service.core.framework.app.message.broadcast.factory.base

/**
 * @author: cangHX
 * @data: 2026/1/19 11:19
 * @desc:
 */
abstract class AbstractStaticBroadcast(
    action: String
) : AbstractBroadcast<IStaticBroadcast>(action), IStaticBroadcast {

}