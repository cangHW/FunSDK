package com.proxy.service.core.framework.app.resource

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.context.CsContextManager

/**
 * 资源，基于名称获取相关工具
 *
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
    fun getDimenPxByName(name: String): Int {
        val resources = CsContextManager.getApplication().resources
        val id = resources.getIdentifier(name, "dimen", CsAppUtils.getPackageName())
        return if (id == 0) {
            0
        } else {
            resources.getDimension(id).toInt()
        }
    }

    /**
     * 根据资源名称找到 drawable 中对应的资源 ID
     *
     * desc: name = "ic_launcher"
     */
    fun getDrawableIdByName(name: String): Int {
        val resources = CsContextManager.getApplication().resources
        val id = resources.getIdentifier(name, "drawable", CsAppUtils.getPackageName())
        return if (id == 0) {
            0
        } else {
            id
        }
    }

    /**
     * 根据资源名称找到 drawable 中对应的 Drawable 对象
     *
     * desc: name = "ic_launcher"
     */
    fun getDrawableByName(name: String): Drawable? {
        val resources = CsContextManager.getApplication().resources
        val id = getDrawableIdByName(name)
        return if (id == 0) {
            null
        } else {
            ContextCompat.getDrawable(CsContextManager.getApplication(), id)
        }
    }

    /**
     * 根据资源名称找到 string 中对应的值
     *
     * desc: name = "app_name"
     */
    fun getStringByName(name: String): String {
        val resources = CsContextManager.getApplication().resources
        val id = resources.getIdentifier(name, "string", CsAppUtils.getPackageName())
        return if (id == 0) {
            ""
        } else {
            resources.getString(id)
        }
    }

    /**
     * 根据资源名称找到 color 中对应的值
     *
     * desc: name = "colorPrimary"
     */
    fun getColorByName(name: String): Int {
        val resources = CsContextManager.getApplication().resources
        val id = resources.getIdentifier(name, "color", CsAppUtils.getPackageName())
        return if (id == 0) {
            0
        } else {
            ContextCompat.getColor(CsContextManager.getApplication(), id)
        }
    }

    /**
     * 根据资源名称找到 anim 中对应的资源 ID
     *
     * desc: name = "slide_in_left"
     */
    fun getAnimIdByName(name: String): Int {
        val resources = CsContextManager.getApplication().resources
        val id = resources.getIdentifier(name, "anim", CsAppUtils.getPackageName())
        return id
    }

    /**
     * 根据资源名称找到 id 中对应的资源 ID
     *
     * desc: name = "button_submit"
     */
    fun getIdByName(name: String): Int {
        val resources = CsContextManager.getApplication().resources
        val id = resources.getIdentifier(name, "id", CsAppUtils.getPackageName())
        return id
    }
}