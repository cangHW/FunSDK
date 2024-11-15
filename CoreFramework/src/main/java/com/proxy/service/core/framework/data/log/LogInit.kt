package com.proxy.service.core.framework.data.log

import com.proxy.service.core.framework.data.log.tree.DebugTree
import com.proxy.service.core.framework.data.log.tree.ReleaseTree
import com.proxy.service.core.framework.data.log.tree.TreeGroup

/**
 * @author: cangHX
 * @data: 2024/4/28 18:53
 * @desc:
 */
object LogInit {

    fun setIsDebug(isDebug: Boolean) {
       val tree = if (isDebug) {
            DebugTree()
        } else {
            ReleaseTree()
        }
        TreeGroup.plant(tree)
    }
}