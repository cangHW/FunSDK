package com.proxy.service.core.framework.app.resource

import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.context.CsContextManager

/**
 * @author: cangHX
 * @data: 2024/9/20 15:06
 * @desc:
 */
object CsResUtils {

    /**
     * 根据资源名称找到 dimen 中对应的值并转化为 px
     *
     * desc: name = "size_2dp"
     * */
    fun getDimenPxByValue(name: String): Int {
        val resources = CsContextManager.getApplication().resources
        val id = resources.getIdentifier(name, "dimen", CsAppUtils.getPackageName())
        return if (id == 0) {
            0
        } else {
            resources.getDimension(id).toInt()
        }
    }

}