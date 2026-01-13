package com.proxy.service.core.framework.app.resource

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.proxy.service.core.constants.CoreConfig
import com.proxy.service.core.framework.app.CsAppUtils
import com.proxy.service.core.framework.app.context.CsContextManager
import com.proxy.service.core.framework.data.log.CsLogger

/**
 * 资源，基于名称获取相关工具
 *
 * @author: cangHX
 * @data: 2024/9/20 15:06
 * @desc:
 */
object CsResUtils {

    private const val TAG = "${CoreConfig.TAG}Res"

    /**
     * 根据资源名称找到对应的 id 类型的资源 ID. R.id.<name>
     *
     * desc: name = "button_submit"
     */
    fun getIdIdByName(name: String): Int {
        val resources = CsContextManager.getApplication().resources
        return resources.getIdentifier(name, "id", CsAppUtils.getPackageName())
    }

    /**
     * 根据资源名称找到对应的 anim 类型的资源 ID. R.anim.<name>
     *
     * desc: name = "slide_in_left"
     */
    fun getAnimIdByName(name: String): Int {
        val resources = CsContextManager.getApplication().resources
        return resources.getIdentifier(name, "anim", CsAppUtils.getPackageName())
    }

    /**
     * 根据资源名称找到对应的 animator 类型的资源 ID. R.animator.<name>
     *
     * desc: name = "slide_in_left"
     */
    fun getAnimatorIdByName(name: String): Int {
        val resources = CsContextManager.getApplication().resources
        return resources.getIdentifier(name, "animator", CsAppUtils.getPackageName())
    }

    /**
     * 根据资源名称找到对应的 xml 类型的资源 ID. R.xml.<name>
     *
     * desc: name = "file"
     */
    fun getXmlIdByName(name: String): Int {
        val resources = CsContextManager.getApplication().resources
        return resources.getIdentifier(name, "xml", CsAppUtils.getPackageName())
    }

    /**
     * 根据资源名称找到对应的 layout 类型的资源 ID. R.layout.<name>
     *
     * desc: name = "file"
     */
    fun getLayoutIdByName(name: String): Int {
        val resources = CsContextManager.getApplication().resources
        return resources.getIdentifier(name, "layout", CsAppUtils.getPackageName())
    }

    /**
     * 根据资源名称找到对应的 drawable 类型的资源 ID. R.drawable.<name>
     *
     * desc: name = "ic_launcher"
     */
    fun getDrawableIdByName(name: String): Int {
        val resources = CsContextManager.getApplication().resources
        return resources.getIdentifier(name, "drawable", CsAppUtils.getPackageName())
    }

    /**
     * 根据资源名称找到对应的 drawable 类型的资源并生成对应的 Drawable 对象. R.drawable.<name>
     *
     * desc: name = "ic_launcher"
     */
    fun getDrawableByName(name: String): Drawable? {
        val id = getDrawableIdByName(name)
        if (id == 0) {
            CsLogger.tag(TAG).e("The target resource does not exist. res name= $name")
            return null
        }

        try {
            return ContextCompat.getDrawable(CsContextManager.getApplication(), id)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return null
    }

    /**
     * 根据资源名称找到对应的 color 类型的资源 ID. R.color.<name>
     *
     * desc: name = "ic_launcher"
     */
    fun getColorIdByName(name: String): Int {
        val resources = CsContextManager.getApplication().resources
        return resources.getIdentifier(name, "color", CsAppUtils.getPackageName())
    }

    /**
     * 根据资源名称找到对应的 color 类型的资源并获取对应的数据. R.color.<name>
     *
     * desc: name = "colorPrimary"
     */
    fun getColorByName(name: String): Int {
        val id = getColorIdByName(name)
        if (id == 0) {
            CsLogger.tag(TAG).e("The target resource does not exist. res name= $name")
            return 0
        }

        try {
            return ContextCompat.getColor(CsContextManager.getApplication(), id)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return 0
    }

    /**
     * 根据资源名称找到对应的 dimen 类型的资源 ID. R.dimen.<name>
     *
     * desc: name = "ic_launcher"
     */
    fun getDimenIdByName(name: String): Int {
        val resources = CsContextManager.getApplication().resources
        return resources.getIdentifier(name, "dimen", CsAppUtils.getPackageName())
    }

    /**
     * 根据资源名称找到对应的 dimen 类型的资源的值并转化为 px. R.dimen.<name>
     *
     * desc: name = "size_2dp"
     * */
    fun getDimenPxByName(name: String): Int {
        val id = getDimenIdByName(name)
        if (id == 0) {
            CsLogger.tag(TAG).e("The target resource does not exist. res name= $name")
            return 0
        }
        return CsDpUtils.res2px(id)
    }

    /**
     * 根据资源名称找到对应的 raw 类型的资源 ID. R.raw.<name>
     *
     * desc: name = "ic_launcher"
     */
    fun getRawIdByName(name: String): Int {
        val resources = CsContextManager.getApplication().resources
        return resources.getIdentifier(name, "raw", CsAppUtils.getPackageName())
    }

    /**
     * 根据资源名称找到对应的 string 类型的资源 ID. R.string.<name>
     *
     * desc: name = "ic_launcher"
     */
    fun getStringIdByName(name: String): Int {
        val resources = CsContextManager.getApplication().resources
        return resources.getIdentifier(name, "string", CsAppUtils.getPackageName())
    }

    /**
     * 根据资源名称找到对应的 string 类型的资源并获取对应的数据. R.string.<name>
     *
     * desc: name = "app_name"
     */
    fun getStringByName(name: String): String {
        val id = getStringIdByName(name)
        if (id == 0) {
            CsLogger.tag(TAG).e("The target resource does not exist. res name= $name")
            return ""
        }

        try {
            val resources = CsContextManager.getApplication().resources
            return resources.getString(id)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return ""
    }

    /**
     * 根据资源名称找到对应的 style 类型的资源 ID. R.style.<name>
     *
     * desc: name = "ic_launcher"
     */
    fun getStyleIdByName(name: String): Int {
        val resources = CsContextManager.getApplication().resources
        return resources.getIdentifier(name, "style", CsAppUtils.getPackageName())
    }

    /**
     * 根据资源名称与资源类型找到对应的资源 ID. R.<type>.<name>
     *
     * desc: name = "file"
     * desc: name = "xml"
     */
    fun getCustomIdByName(name: String, type: String): Int {
        val resources = CsContextManager.getApplication().resources
        return resources.getIdentifier(name, type, CsAppUtils.getPackageName())
    }
}