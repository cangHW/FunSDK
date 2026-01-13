package com.proxy.service.core.framework.ui.view.action.exposure.params

/**
 * @author: cangHX
 * @data: 2026/1/9 15:43
 * @desc:
 */
class ExposureParams private constructor(
    private val isExposure: Boolean,
    private val tag: Any?
) {

    companion object {
        fun create(isExposure: Boolean, tag: Any?): ExposureParams {
            return ExposureParams(isExposure, tag)
        }
    }

    /**
     * 是否曝光状态
     * */
    fun isExposureState(): Boolean {
        return isExposure
    }

    /**
     * 标记信息, 创建时设置的自定义对象
     * */
    fun getTag(): Any? {
        return tag
    }
}