package com.proxy.service.core.framework.app.resource

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
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

    private const val TYPE_ID = "id"
    private const val TYPE_ANIM = "anim"
    private const val TYPE_ANIMATOR = "animator"
    private const val TYPE_XML = "xml"
    private const val TYPE_LAYOUT = "layout"
    private const val TYPE_DRAWABLE = "drawable"
    private const val TYPE_COLOR = "color"
    private const val TYPE_DIMEN = "dimen"
    private const val TYPE_RAW = "raw"
    private const val TYPE_STRING = "string"
    private const val TYPE_STYLE = "style"


    /**
     * 根据资源名称找到对应的 id 类型的资源 ID. R.id.<name>
     * desc: name = "button_submit"
     *
     * @param name          资源名称
     * @param packageName   包名，为空默认为当前应用
     */
    fun getIdIdByName(name: String, packageName: String? = null): Int {
        val context = CsContextManager.getApplication()
        val currentPkg = CsAppUtils.getPackageName()

        if (packageName.isNullOrEmpty() || packageName == currentPkg) {
            return context.resources.getIdentifier(name, TYPE_ID, currentPkg)
        }

        try {
            val targetContext = context.createPackageContext(
                packageName,
                Context.CONTEXT_IGNORE_SECURITY
            )
            val targetResources = targetContext.resources
            return targetResources.getIdentifier(name, TYPE_ID, packageName)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return 0
    }

    /**
     * 根据资源名称找到对应的 anim 类型的资源 ID. R.anim.<name>
     * desc: name = "slide_in_left"
     *
     * @param name          资源名称
     * @param packageName   包名，为空默认为当前应用
     */
    fun getAnimIdByName(name: String, packageName: String? = null): Int {
        val context = CsContextManager.getApplication()
        val currentPkg = CsAppUtils.getPackageName()

        if (packageName.isNullOrEmpty() || packageName == currentPkg) {
            return context.resources.getIdentifier(name, TYPE_ANIM, currentPkg)
        }

        try {
            val targetContext = context.createPackageContext(
                packageName,
                Context.CONTEXT_IGNORE_SECURITY
            )
            val targetResources = targetContext.resources
            return targetResources.getIdentifier(name, TYPE_ANIM, packageName)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return 0
    }

    /**
     * 根据资源名称找到对应的 animator 类型的资源 ID. R.animator.<name>
     * desc: name = "slide_in_left"
     *
     * @param name          资源名称
     * @param packageName   包名，为空默认为当前应用
     */
    fun getAnimatorIdByName(name: String, packageName: String? = null): Int {
        val context = CsContextManager.getApplication()
        val currentPkg = CsAppUtils.getPackageName()

        if (packageName.isNullOrEmpty() || packageName == currentPkg) {
            return context.resources.getIdentifier(name, TYPE_ANIMATOR, currentPkg)
        }

        try {
            val targetContext = context.createPackageContext(
                packageName,
                Context.CONTEXT_IGNORE_SECURITY
            )
            val targetResources = targetContext.resources
            return targetResources.getIdentifier(name, TYPE_ANIMATOR, packageName)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return 0
    }

    /**
     * 根据资源名称找到对应的 xml 类型的资源 ID. R.xml.<name>
     * desc: name = "file"
     *
     * @param name          资源名称
     * @param packageName   包名，为空默认为当前应用
     */
    fun getXmlIdByName(name: String, packageName: String? = null): Int {
        val context = CsContextManager.getApplication()
        val currentPkg = CsAppUtils.getPackageName()

        if (packageName.isNullOrEmpty() || packageName == currentPkg) {
            return context.resources.getIdentifier(name, TYPE_XML, currentPkg)
        }

        try {
            val targetContext = context.createPackageContext(
                packageName,
                Context.CONTEXT_IGNORE_SECURITY
            )
            val targetResources = targetContext.resources
            return targetResources.getIdentifier(name, TYPE_XML, packageName)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return 0
    }

    /**
     * 根据资源名称找到对应的 layout 类型的资源 ID. R.layout.<name>
     * desc: name = "file"
     *
     * @param name          资源名称
     * @param packageName   包名，为空默认为当前应用
     */
    fun getLayoutIdByName(name: String, packageName: String? = null): Int {
        val context = CsContextManager.getApplication()
        val currentPkg = CsAppUtils.getPackageName()

        if (packageName.isNullOrEmpty() || packageName == currentPkg) {
            return context.resources.getIdentifier(name, TYPE_LAYOUT, currentPkg)
        }

        try {
            val targetContext = context.createPackageContext(
                packageName,
                Context.CONTEXT_IGNORE_SECURITY
            )
            val targetResources = targetContext.resources
            return targetResources.getIdentifier(name, TYPE_LAYOUT, packageName)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return 0
    }

    /**
     * 根据资源名称找到对应的 drawable 类型的资源 ID. R.drawable.<name>
     * desc: name = "ic_launcher"
     *
     * @param name          资源名称
     * @param packageName   包名，为空默认为当前应用
     */
    fun getDrawableIdByName(name: String, packageName: String? = null): Int {
        val context = CsContextManager.getApplication()
        val currentPkg = CsAppUtils.getPackageName()

        if (packageName.isNullOrEmpty() || packageName == currentPkg) {
            return context.resources.getIdentifier(name, TYPE_DRAWABLE, currentPkg)
        }

        try {
            val targetContext = context.createPackageContext(
                packageName,
                Context.CONTEXT_IGNORE_SECURITY
            )
            val targetResources = targetContext.resources
            return targetResources.getIdentifier(name, TYPE_DRAWABLE, packageName)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return 0
    }

    /**
     * 根据资源名称找到对应的 drawable 类型的资源并生成对应的 Drawable 对象. R.drawable.<name>
     * desc: name = "ic_launcher"
     *
     * @param name          资源名称
     * @param packageName   包名，为空默认为当前应用
     */
    fun getDrawableByName(name: String, packageName: String? = null): Drawable? {
        val context = CsContextManager.getApplication()
        val currentPkg = CsAppUtils.getPackageName()

        if (packageName.isNullOrEmpty() || packageName == currentPkg) {
            val id = getDrawableIdByName(name, packageName)
            if (id == 0) {
                CsLogger.tag(TAG).e("The target resource does not exist. res name= $name")
                return null
            }

            try {
                return ContextCompat.getDrawable(context, id)
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
            return null
        }

        try {
            val targetContext = context.createPackageContext(
                packageName,
                Context.CONTEXT_IGNORE_SECURITY
            )
            val targetResources = targetContext.resources
            val id = targetResources.getIdentifier(name, TYPE_DRAWABLE, packageName)

            if (id == 0) {
                CsLogger.tag(TAG).e("The target resource does not exist. res name= $name")
                return null
            }

            return ResourcesCompat.getDrawable(targetResources, id, targetContext.theme)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }

        return null
    }

    /**
     * 根据资源名称找到对应的 color 类型的资源 ID. R.color.<name>
     * desc: name = "colorPrimary"
     *
     * @param name          资源名称
     * @param packageName   包名，为空默认为当前应用
     */
    fun getColorIdByName(name: String, packageName: String? = null): Int {
        val context = CsContextManager.getApplication()
        val currentPkg = CsAppUtils.getPackageName()

        if (packageName.isNullOrEmpty() || packageName == currentPkg) {
            return context.resources.getIdentifier(name, TYPE_COLOR, currentPkg)
        }

        try {
            val targetContext = context.createPackageContext(
                packageName,
                Context.CONTEXT_IGNORE_SECURITY
            )
            val targetResources = targetContext.resources
            return targetResources.getIdentifier(name, TYPE_COLOR, packageName)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return 0
    }

    /**
     * 根据资源名称找到对应的 color 类型的资源并获取对应的数据. R.color.<name>
     * desc: name = "colorPrimary"
     *
     * @param name          资源名称
     * @param packageName   包名，为空默认为当前应用
     */
    fun getColorByName(name: String, packageName: String? = null): Int {
        val context = CsContextManager.getApplication()
        val currentPkg = CsAppUtils.getPackageName()

        if (packageName.isNullOrEmpty() || packageName == currentPkg) {
            val id = getColorIdByName(name)
            if (id == 0) {
                CsLogger.tag(TAG).e("The target resource does not exist. res name= $name")
                return 0
            }

            try {
                return ContextCompat.getColor(context, id)
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
            return 0
        }

        try {
            val targetContext = context.createPackageContext(
                packageName,
                Context.CONTEXT_IGNORE_SECURITY
            )
            val targetResources = targetContext.resources
            val id = targetResources.getIdentifier(name, TYPE_COLOR, packageName)

            if (id == 0) {
                CsLogger.tag(TAG).e("The target resource does not exist. res name= $name")
                return 0
            }

            return targetResources.getColor(id, targetContext.theme)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }

        return 0
    }

    /**
     * 根据资源名称找到对应的 dimen 类型的资源 ID. R.dimen.<name>
     * desc: name = "ic_launcher"
     *
     * @param name          资源名称
     * @param packageName   包名，为空默认为当前应用
     */
    fun getDimenIdByName(name: String, packageName: String? = null): Int {
        val context = CsContextManager.getApplication()
        val currentPkg = CsAppUtils.getPackageName()

        if (packageName.isNullOrEmpty() || packageName == currentPkg) {
            return context.resources.getIdentifier(name, TYPE_DIMEN, currentPkg)
        }

        try {
            val targetContext = context.createPackageContext(
                packageName,
                Context.CONTEXT_IGNORE_SECURITY
            )
            val targetResources = targetContext.resources
            return targetResources.getIdentifier(name, TYPE_DIMEN, packageName)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return 0
    }

    /**
     * 根据资源名称找到对应的 dimen 类型的资源的值并转化为 px. R.dimen.<name>
     * desc: name = "size_2dp"
     *
     * @param name          资源名称
     * @param packageName   包名，为空默认为当前应用
     */
    fun getDimenPxByName(name: String, packageName: String? = null): Int {
        val context = CsContextManager.getApplication()
        val currentPkg = CsAppUtils.getPackageName()

        if (packageName.isNullOrEmpty() || packageName == currentPkg) {
            val id = getDimenIdByName(name)
            if (id == 0) {
                CsLogger.tag(TAG).e("The target resource does not exist. res name= $name")
                return 0
            }

            return CsDpUtils.res2px(id)
        }

        try {
            val targetContext = context.createPackageContext(
                packageName,
                Context.CONTEXT_IGNORE_SECURITY
            )
            val targetResources = targetContext.resources
            val id = targetResources.getIdentifier(name, TYPE_DIMEN, packageName)

            if (id == 0) {
                CsLogger.tag(TAG).e("The target resource does not exist. res name= $name")
                return 0
            }

            return targetResources.getDimensionPixelSize(id)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }

        return 0
    }

    /**
     * 根据资源名称找到对应的 raw 类型的资源 ID. R.raw.<name>
     * desc: name = "ic_launcher"
     *
     * @param name          资源名称
     * @param packageName   包名，为空默认为当前应用
     */
    fun getRawIdByName(name: String, packageName: String? = null): Int {
        val context = CsContextManager.getApplication()
        val currentPkg = CsAppUtils.getPackageName()

        if (packageName.isNullOrEmpty() || packageName == currentPkg) {
            return context.resources.getIdentifier(name, TYPE_RAW, currentPkg)
        }

        try {
            val targetContext = context.createPackageContext(
                packageName,
                Context.CONTEXT_IGNORE_SECURITY
            )
            val targetResources = targetContext.resources
            return targetResources.getIdentifier(name, TYPE_RAW, packageName)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return 0
    }

    /**
     * 根据资源名称找到对应的 string 类型的资源 ID. R.string.<name>
     * desc: name = "app_name"
     *
     * @param name          资源名称
     * @param packageName   包名，为空默认为当前应用
     */
    fun getStringIdByName(name: String, packageName: String? = null): Int {
        val context = CsContextManager.getApplication()
        val currentPkg = CsAppUtils.getPackageName()

        if (packageName.isNullOrEmpty() || packageName == currentPkg) {
            return context.resources.getIdentifier(name, TYPE_STRING, currentPkg)
        }

        try {
            val targetContext = context.createPackageContext(
                packageName,
                Context.CONTEXT_IGNORE_SECURITY
            )
            val targetResources = targetContext.resources
            return targetResources.getIdentifier(name, TYPE_STRING, packageName)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return 0
    }

    /**
     * 根据资源名称找到对应的 string 类型的资源并获取对应的数据. R.string.<name>
     * desc: name = "app_name"
     *
     * @param name          资源名称
     * @param packageName   包名，为空默认为当前应用
     */
    fun getStringByName(name: String, packageName: String? = null): String {
        val context = CsContextManager.getApplication()
        val currentPkg = CsAppUtils.getPackageName()

        if (packageName.isNullOrEmpty() || packageName == currentPkg) {
            val id = getStringIdByName(name)
            if (id == 0) {
                CsLogger.tag(TAG).e("The target resource does not exist. res name= $name")
                return ""
            }

            try {
                return context.resources.getString(id)
            } catch (throwable: Throwable) {
                CsLogger.tag(TAG).e(throwable)
            }
            return ""
        }

        try {
            val targetContext = context.createPackageContext(
                packageName,
                Context.CONTEXT_IGNORE_SECURITY
            )
            val targetResources = targetContext.resources
            val id = targetResources.getIdentifier(name, TYPE_STRING, packageName)

            if (id == 0) {
                CsLogger.tag(TAG).e("The target resource does not exist. res name= $name")
                return ""
            }

            return targetResources.getString(id, targetContext.theme)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }

        return ""
    }

    /**
     * 根据资源名称找到对应的 style 类型的资源 ID. R.style.<name>
     * desc: name = "app_name"
     *
     * @param name          资源名称
     * @param packageName   包名，为空默认为当前应用
     */
    fun getStyleIdByName(name: String, packageName: String? = null): Int {
        val context = CsContextManager.getApplication()
        val currentPkg = CsAppUtils.getPackageName()

        if (packageName.isNullOrEmpty() || packageName == currentPkg) {
            return context.resources.getIdentifier(name, TYPE_STYLE, currentPkg)
        }

        try {
            val targetContext = context.createPackageContext(
                packageName,
                Context.CONTEXT_IGNORE_SECURITY
            )
            val targetResources = targetContext.resources
            return targetResources.getIdentifier(name, TYPE_STYLE, packageName)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return 0
    }

    /**
     * 根据资源名称与资源类型找到对应的资源 ID. R.<type>.<name>
     * desc: name = "file"
     * desc: type = "xml"
     *
     * @param name          资源名称
     * @param packageName   包名，为空默认为当前应用
     */
    fun getCustomIdByName(name: String, type: String, packageName: String? = null): Int {
        val context = CsContextManager.getApplication()
        val currentPkg = CsAppUtils.getPackageName()

        if (packageName.isNullOrEmpty() || packageName == currentPkg) {
            return context.resources.getIdentifier(name, type, currentPkg)
        }

        try {
            val targetContext = context.createPackageContext(
                packageName,
                Context.CONTEXT_IGNORE_SECURITY
            )
            val targetResources = targetContext.resources
            return targetResources.getIdentifier(name, type, packageName)
        } catch (throwable: Throwable) {
            CsLogger.tag(TAG).e(throwable)
        }
        return 0
    }
}